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
 *
 * Class represents a group of listeners. The rule of listeners grouping is that one group has only one source
 * document to work on. This is the only thing connecting listeners in one group and each listener can (has to)
 * have his own configuration.
 * If listener wants to listen, he has to be in one or more groups.
 * This group contains mechanism for auto listening and refreshing the source document and for explicit listening called
 * by user.
 * If you want to create a group, you have to specify a supplier able to supply fresh version of document. In this case,
 * the supplier is represent by {@link java.util.function.Supplier} functional interface.
 * Each listener contains the max age of document, that can be regarded as a fresh. If this parameter is not specify
 * in constructor, the default value will be used.
 *
 * @see #toWork
 * @see #listeners
 * @see #maxRefreshingDelay
 * @see #DEFAULT_MAX_REFRESHING_DELAY
 * @see cz.matejkripner.watch.ListenerGroup.Worker
 */
public class ListenerGroup {
    private static final long DEFAULT_MAX_REFRESHING_DELAY = 1000 * 60 * 5;

    /**
     * Returns a new group with given document supplier and max refreshing delay. Both arguments has to be valid. This
     * means, that {@code getDocument != null && maxRefreshingDelay >= ListenerConfig.MIN_AUTO_CHECKING} has to return
     * true.
     * @param getDocument The document supplier for the new group
     * @param maxRefreshingDelay The max age of document, that can be int the new group regarded as a fresh
     * @return The new listener group with given parameters
     *
     * @see #ListenerGroup(java.util.function.Supplier, long)
     * @see #newGroup(java.util.function.Supplier)
     * @see cz.matejkripner.watch.ListenerGroup
     */
    public static ListenerGroup newGroup(Supplier<Document> getDocument, long maxRefreshingDelay) {
        if(getDocument == null)
            throw new IllegalArgumentException("Document supplier can not be null");
        if(maxRefreshingDelay < ListenerConfig.MIN_AUTO_CHECKING)
            throw new IllegalArgumentException("Max refresh delay can not be less than " + ListenerConfig.MIN_AUTO_CHECKING
                    + ", but was " + maxRefreshingDelay);
        ListenerGroup group = new ListenerGroup(getDocument, maxRefreshingDelay);
        return group;
    }

    /**
     * This factory method do the same as {@link #newGroup(java.util.function.Supplier, long)}, but fill the
     * {@link #maxRefreshingDelay} with default value
     *
     * @param getDocument The document supplier for the new group
     * @return The new listener group with given parameters
     *
     * @see #ListenerGroup(java.util.function.Supplier, long)
     * @see cz.matejkripner.watch.ListenerGroup
     * @see #DEFAULT_MAX_REFRESHING_DELAY
     */
    public static ListenerGroup newGroup(Supplier<Document> getDocument) {
        return newGroup(getDocument, DEFAULT_MAX_REFRESHING_DELAY);
    }

    /**
     * The list of listeners of this group. Can not be null
     */
    private final LinkedList<Listener> listeners;
    /**
     * The supplier of document, which the listeners will work on
     */
    private final Supplier<Document> toWork;

    private final Worker worker;

    /**
     * The max age of document, that can be in this group regarded as a fresh
     */
    private final long maxRefreshingDelay;

    /**
     * The constructor, that in fact do the same as {@link #ListenerGroup(java.util.function.Supplier, java.util.LinkedList, long)}
     * @param toWork The document supplier for the new group
     * @param maxRefreshingDelay The max age of document, that can be int the new group regarded as a fresh
     */
    private ListenerGroup(Supplier<Document> toWork, long maxRefreshingDelay) {
        this(toWork, new LinkedList<>(), maxRefreshingDelay);
    }

    /**
     *
     * @param toWork
     * @param iniListeners
     * @param maxRefreshingDelay
     */
    private ListenerGroup(Supplier<Document> toWork, LinkedList<Listener> iniListeners, long maxRefreshingDelay) {
        listeners = iniListeners;
        this.toWork = toWork;
        this.maxRefreshingDelay = maxRefreshingDelay;
        iniListeners();
        Function<Worker, Integer> delaySupplier = w -> w.autoRefreshings.stream() // supply the minimum of all listener's delays
                .mapToInt(lt -> lt.listener.config().autoChecking())
                .min()
                .getAsInt();
        worker = new Worker(delaySupplier);
        worker.start();
    }

    /**
     *
     */
    private void iniListeners() {
        listeners.stream().forEach(this::configure);
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
        if(l.getLastChangeDocument() == null) {
            if(worker.getLastDoc() == null)
                refresh();
            l.setLastChangeDocument(worker.getLastDoc());
        }
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
        Element newElementToListen = l.supplyElement().apply(worker.getLastDoc());
        Element oldElementToListen = l.supplyElement().apply(l.getLastChangeDocument());

        if(l.changed().test(oldElementToListen, newElementToListen)) {
            l.action().accept(oldElementToListen, newElementToListen);
            l.setLastChangeDocument(worker.getLastDoc());
        }
    }

    private class Worker extends Thread {
        private final Function<Worker, Integer> delay;
        private final LinkedList<ListenerTime> autoRefreshings;

        private Document lastDoc;

        public Worker(Function<Worker, Integer> delay) {
            this.delay = delay;
            autoRefreshings = new LinkedList<>();
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
                synchronized (this) {
                    if (!autoRefreshings.isEmpty()) {
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
