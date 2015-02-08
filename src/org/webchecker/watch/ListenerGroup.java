package org.webchecker.watch;

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
        return new ListenerGroup(getDocument, maxRefreshingDelay);
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
        iniListeners();
        Function<Worker, Integer> delaySupplier = w -> w.autoRefreshing.stream() // supply the minimum of all listener's delays
                .mapToInt(lt -> lt.listener.config().autoChecking()).min().getAsInt();
        worker = new Worker(delaySupplier);
        worker.start();
    }

    private void iniListeners() {
        listeners.stream().forEach(this::configure);
    }

    public synchronized void addListener(Listener l) {
        configure(l);
        listeners.add(l);
        if (l.config().autoCheckingOn()) {
            worker.addToAutoRefreshing(l);
        }
        synchronized (worker) {
            worker.notify();
        }
    }

    private Listener configure(Listener l) {
        if (l.getLastChangeDocument() == null) {
            if (worker.getLastDoc() == null) refresh();
            l.setLastChangeDocument(worker.getLastDoc());
        }
        l.config().setOnAutoCheckingChange((oldValue, newValue) -> {
            if (newValue > 0) {
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
        listeners.stream().filter(update).forEach(this::applyListening);
    }

    public synchronized void refresh() {
        worker.refreshDocs();
    }

    private void applyListening(Listener l) {
        System.out.println("applyListening(" + l + ")");
        Element newElementToListen = l.supplyElement().apply(worker.getLastDoc());
        Element oldElementToListen = l.supplyElement().apply(l.getLastChangeDocument());

        if (l.changed().test(oldElementToListen, newElementToListen)) {
            l.action().accept(oldElementToListen, newElementToListen);
            l.setLastChangeDocument(worker.getLastDoc());
        }
    }

    private class Worker extends Thread {

        private final Function<Worker, Integer> delay;
        private final LinkedList<ListenerTime> autoRefreshing;
        private Document lastDoc;

        public Worker(Function<Worker, Integer> delay) {
            this.delay = delay;
            autoRefreshing = new LinkedList<>();
            refreshDocs();
//            setDaemon(true); // TODO: uncomment
        }

        private void addToAutoRefreshing(Listener l) {
            autoRefreshing.add(configure(new ListenerTime(l)));
        }

        private ListenerTime configure(ListenerTime lt) {
            lt.listener.config().setOnAutoCheckingChange((oldValue, newValue) -> {
                if (newValue == 0) {
                    autoRefreshing.remove(lt);
                }
            });
            return lt;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                System.out.println("worker's loop");
                synchronized (this) {
                    if (!autoRefreshing.isEmpty()) {
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
            autoRefreshing.stream().filter(ListenerTime::needRefresh).forEach(lt -> {
                applyListening(lt.listener);
                lt.refreshDone();
            });
            refreshDocs();
        }

        private long lastDocsRefresh;

        {
            lastDocsRefresh = 0;
        }

        public long getLastDocsRefresh() {
            return lastDocsRefresh;
        }

        private synchronized void refreshDocs() {
            lastDoc = toWork.get();
            lastDocsRefresh = System.currentTimeMillis();
        }

        public synchronized Document getLastDoc() {
            return lastDoc;
        }

        private void waitUntilListenersEmpty() {
            while (autoRefreshing.isEmpty()) {
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
