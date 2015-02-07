package cz.matejkripner.watch;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Matěj Kripner
 * @version 1.0
 */
public class ListenerGroup {
    private static final long DEFAULT_MAX_REFRESHING_DELAY = 1000 * 60 * 5;
    public static ListenerGroup newGroup(Supplier<Document> getDocument, long maxRefreshingDelay) {
        ListenerGroup group = new ListenerGroup(getDocument, maxRefreshingDelay);
        return group;
    }
    public static ListenerGroup newGroup(Supplier<Document> getDocument) {
        return newGroup(getDocument, DEFAULT_MAX_REFRESHING_DELAY);
    }

    private final LinkedList<Listener> listeners;
    private final Supplier<Document> toWork;

    private final Worker worker;

    private final long maxRefreshingDelay;

    private ListenerGroup(Supplier<Document> toWork, long maxRefreshingDelay) {
        this(toWork, new LinkedList<>(), maxRefreshingDelay);
    }
    private ListenerGroup(Supplier<Document> toWork, LinkedList<Listener> iniListeners, long maxRefreshingDelay) {
        listeners = iniListeners;
        this.toWork = toWork;
        this.maxRefreshingDelay = maxRefreshingDelay;
        Function<Worker, Integer> delaySupplier = w -> w.autoRefreshings.stream() // supply the minimum of all listener's delays
                .mapToInt(lt -> lt.listener.config().autoChecking())
                .min()
                .getAsInt();
        worker = new Worker(delaySupplier);
        worker.start();
    }
    public synchronized void addListener(Listener l) {
        configure(l);
        listeners.add(l);
        if(l.config().autoCheckingOn()) {
            worker.addToAutoRefreshing(l);
        }
        synchronized (worker) {
            worker.notify();
        }
    }
    private Listener configure(Listener l) {
        l.config().setOnAutoCheckingChange((oldValue, newValue) -> {
            if(newValue > 0) {
                worker.addToAutoRefreshing(l);
            }
        });
        return l;
    }

    public void check() {
        check(l -> true);
    }
    public synchronized void check(Predicate<Listener> update) {
        if (System.currentTimeMillis() - worker.getLastDocsRefresh() > maxRefreshingDelay) {
            refresh();
        }
        listeners.stream()
                .filter(update)
                .forEach(this::applyListening);
    }
    public synchronized void refresh() {
        worker.refreshDocs();
    }

    private void applyListening(Listener l) {
        System.out.println("applyListening(" + l + ")");
        Element newElementToListen = l.supplyElement().apply(worker.getNewestDoc());
        Element oldElementToListen = l.supplyElement().apply(worker.getOldDoc());

        if(l.changed().test(oldElementToListen, newElementToListen)) {
            l.action().accept(oldElementToListen, newElementToListen);
        }
    }

    private class Worker extends Thread {
        private final Function<Worker, Integer> delay;
        private final LinkedList<ListenerTime> autoRefreshings;

        private Document oldDoc;
        private Document newestDoc;

        public Worker(Function<Worker, Integer> delay) {
            this.delay = delay;
            autoRefreshings = new LinkedList<>();
            refreshDocs(); // not a mistake, need to fill oldDoc and newestDoc
            refreshDocs();
//            setDaemon(true); // TODO: uncomment
        }

        private void addToAutoRefreshing(Listener l) {
            autoRefreshings.add(configure(new ListenerTime(l)));
        }

        private ListenerTime configure(ListenerTime lt) {
            lt.listener.config().setOnAutoCheckingChange((oldValue, newValue) -> {
                if (newValue == 0) {
                    autoRefreshings.remove(lt);
                }
            });
            return lt;
        }
        @Override
        public void run() {
            while(!interrupted()) {
                System.out.println("worker's loop");
                synchronized (this) {
                    if (!autoRefreshings.isEmpty()) {
                        System.out.println("worker working");
                        work();
                    } else {
                        waitUntilListenersEmpty();
                    }
                }
                try {
                    Thread.sleep(delay.apply(this));
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }

        private void work() {
            autoRefreshings
                    .stream()
                    .filter(ListenerTime::needRefresh)
                    .forEach(lt -> {
                        applyListening(lt.listener);
                        lt.refreshDone();
                    });
            refreshDocs();
        }

        private long lastDocsRefresh; {
            lastDocsRefresh = 0;
        }
        public long getLastDocsRefresh() {
            return lastDocsRefresh;
        }
        private synchronized void refreshDocs() {
            oldDoc = newestDoc;
            newestDoc = toWork.get();
            lastDocsRefresh = System.currentTimeMillis();
        }
        public synchronized Document getNewestDoc() {
            return newestDoc;
        }

        public synchronized Document getOldDoc() {
            return oldDoc;
        }

        private void waitUntilListenersEmpty() {
            while(autoRefreshings.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }

        private class ListenerTime {
            private final Listener listener;
            private long lastRefresh;

            public ListenerTime(Listener listener) {
                this.listener = listener;
                lastRefresh = 0;
            }
            public boolean needRefresh() {
                return (System.currentTimeMillis() - lastRefresh) > listener.config().autoChecking();
            }
            public void refreshDone() {
                lastRefresh = System.currentTimeMillis();
            }
        }
    }
}
