package org.webchecker.watcher;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
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
 * @author Matěj Kripner
 * @version 1.0
 * @see #toWork
 * @see #listeners
 * @see #maxRefreshingDelay
 * @see #DEFAULT_MAX_REFRESHING_DELAY
 * @see Worker
 */
public class ListenerGroup {

    private static final long DEFAULT_MAX_REFRESHING_DELAY = 1000 * 60 * 5;

    /**
     * Returns a new group with given document supplier and max refreshing delay. Both arguments has to be valid. This
     * means, that {@code getDocument != null && maxRefreshingDelay >= ListenerConfig.MIN_AUTO_CHECKING} has to return
     * true.
     *
     * @param getDocument        The document supplier for the new group
     * @param maxRefreshingDelay The max age of document, that can be int the new group regarded as a fresh
     * @return The new listener group with given parameters
     * @see #ListenerGroup(java.util.function.Supplier, long)
     * @see #newGroup(java.util.function.Supplier)
     * @see ListenerGroup
     */
    public static ListenerGroup newGroup(Supplier<Document> getDocument, long maxRefreshingDelay) {
        if (getDocument == null) throw new IllegalArgumentException("Document supplier can not be null");
        if (maxRefreshingDelay < ListenerConfig.MIN_AUTO_CHECKING)
            throw new IllegalArgumentException("Max refresh delay can not be less than " + ListenerConfig.MIN_AUTO_CHECKING + ", but was " + maxRefreshingDelay);
        return new ListenerGroup(getDocument, maxRefreshingDelay);
    }

    /**
     * This factory method do the same as {@link #newGroup(java.util.function.Supplier, long)}, but fill the
     * {@link #maxRefreshingDelay} with default value
     *
     * @param getDocument The document supplier for the new group
     * @return The new listener group with given parameters
     * @see #ListenerGroup(java.util.function.Supplier, long)
     * @see ListenerGroup
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
     * The constructor, that in fact do the same as {@link #ListenerGroup(java.util.function.Supplier, java.util.LinkedList, long)},
     * but do not create any listeners
     *
     * @param toWork             The document supplier for the new group
     * @param maxRefreshingDelay The max age of document, that can be int the new group regarded as a fresh
     */
    private ListenerGroup(Supplier<Document> toWork, long maxRefreshingDelay) {
        this(toWork, new LinkedList<>(), maxRefreshingDelay);
    }

    /**
     * The main constructor, that initialize class fields with given values, initialize given listeners and start the
     * {@link org.webchecker.watcher.ListenerGroup.Worker} object
     *
     * @param toWork The document supplier for the new group
     * @param iniListeners A list of listeners that will be used as a list for listeners saving
     * @param maxRefreshingDelay The max age of document, that can be int the new group regarded as a fresh
     */
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

    /**
     * Initialize all listeners in {@link #listeners} list. In fact do the same as the code:
     * {@code
     * for(Listener listener : listeners) {
     *     configure(listener);
     * }
     * }
     * As you can see, the initializing of each listener is done by {@link #configure(Listener)} method
     */
    private void iniListeners() {
        listeners.stream().forEach(this::configure);
    }

    /**
     * Configure the given listener and add it to the {@link #listeners} list. If autoChecking of the listener
     * is on, add it to {@link #worker}'s list to apply auto listening
     *
     * @param l A listener to be added. If null, {@link java.lang.NullPointerException} will be thrown
     * @see #listeners
     * @see #worker
     */
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

    /**
     * Configure and return given listener. A listener is ready for future using after
     * configuring by this method
     * @param l The listener to configure
     * @return Given listener after configuring
     */
    private Listener configure(Listener l) {
        if (l.getLastChangeDocument() == null) { // listener was not yet use and there is no last version of a document
            if (worker.getLastDoc() == null) refresh(); // worker was not yet use and (his refresh method was not yet called)
            l.setLastChangeDocument(worker.getLastDoc());
        }
        l.config().setOnAutoCheckingChange((oldValue, newValue) -> {
            if (newValue > 0) {
                worker.addToAutoRefreshing(l);
            }
        });
        return l;
    }

    /**
     * Do the same as {@link #check(java.util.function.Predicate)} method, but apply listening to all listeners
     */
    public void check() {
        check(l -> true);
    }

    /**
     * Apply listening to listeners, that return true for given predicate. If the last version of loaded document
     * is not fresh enough, refresh it by {@link #refresh()} method. A document is fresh enough when delay between
     * his loading time and now is less than {@link #maxRefreshingDelay} value.
     * @param update This function has to determine, if we should apply listening to a listener
     */
    public synchronized void check(Predicate<Listener> update) {
        if (System.currentTimeMillis() - worker.getLastDocRefresh() > maxRefreshingDelay) {
            refresh();
        }
        listeners.stream().filter(update).forEach(this::applyListening);
    }

    /**
     * Refresh a document the group is working on
     */
    public synchronized void refresh() {
        worker.refreshDoc();
    }

    /**
     * Apply listening to given listener.
     * @param l Listener to apply listening. If null, {@link java.lang.NullPointerException} is thrown
     */
    private void applyListening(Listener l) {
        Element newElementToListen = l.supplyElement().apply(worker.getLastDoc()); // new element to compare with his old version
        Element oldElementToListen = l.supplyElement().apply(l.getLastChangeDocument()); // old element to compare

        if (l.changed().test(oldElementToListen, newElementToListen)) { // tests, if change occurs
            l.action().accept(oldElementToListen, newElementToListen); // change occurs! Call the action function of a listener
            l.setLastChangeDocument(worker.getLastDoc()); // set the listener's document to newest version
        }
    }

    /**
     * Class primary used for applying auto listening. It also manage instance of {@link org.jsoup.nodes.Document} object
     * all application works with.
     * All is automatic, you only need to initialize the worker start it by {@link #start()} method
     *
     * @see #lastDoc
     * @see #work()
     */
    private class Worker extends Thread {
        /**
         * This function should return the minimum delay of all listener's delays.
         * The function will be called with instance of this listener (delay.apply(this))
         */
        private final Function<Worker, Integer> delay;
        /**
         * The list of listeners, which has auto listening on. The worker will care about listeners, which turn auto
         * listening off and it will remove it from the list
         */
        private final LinkedList<ListenerTime> autoRefreshing;
        /**
         * Last version of document we are working on
         */
        private Document lastDoc;

        /**
         * This constructor manage given delay function and do the initialize {@link #lastDoc} refresh by {@link #refreshDoc()}
         * function.
         * @param delay Given value for {@link #delay} attribute
         */
        public Worker(Function<Worker, Integer> delay) {
            this.delay = delay;
            autoRefreshing = new LinkedList<>();
            refreshDoc();
            setDaemon(true);
        }

        /**
         * If has auto listening on, configure the given listener and add it to {@link #autoRefreshing} list. Auto listening
         * will be applied with further {@link #work()} method call
         * @param l Listener to be added
         */
        private void addToAutoRefreshing(Listener l) {
            if(l.config().autoCheckingOn())
                autoRefreshing.add(configure(new ListenerTime(l)));
        }

        /**
         * Configure and return the given listenerTime. In fact ensure, that if the listeners will turn his auto
         * listening off, remove it from the list
         * @param lt ListenerTime to be configured
         * @return Configured listenerTime
         *
         * @see org.webchecker.watcher.ListenerGroup.Worker.ListenerTime
         */
        private ListenerTime configure(ListenerTime lt) {
            lt.listener.config().setOnAutoCheckingChange((oldValue, newValue) -> {
                if (newValue == 0) {
                    autoRefreshing.remove(lt);
                }
            });
            return lt;
        }

        /**
         * Do the worker's work (applying auto listening) until the worker is not interrupted. Wait, while the
         * {@link #autoRefreshing} list is empty
         */
        @Override
        public void run() {
            while (!interrupted()) {
                synchronized (this) {
                    if (!autoRefreshing.isEmpty()) {
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

        /**
         * Apply listening to all listeners in {@link #autoRefreshing} list, which needs it. Listener need listening,
         * when the time between last listening and now is greater than listener's autoChecking parameter
         */
        private void work() {
            autoRefreshing.stream().filter(ListenerTime::needRefresh).forEach(lt -> {
                applyListening(lt.listener);
                lt.refreshDone();
            });
            refreshDoc();
        }

        /**
         * Time, when the document we are working on was last refreshed
         */
        private long lastDocRefresh;

        {
            lastDocRefresh = 0; // initialize to zero
        }

        /**
         * Simple getter, that return the value of {@link #lastDocRefresh} attribute
         * @return The value of {@link #lastDocRefresh} attribute
         */
        public long getLastDocRefresh() {
            return lastDocRefresh;
        }

        private synchronized void refreshDoc() {
            lastDoc = toWork.get();
            lastDocRefresh = System.currentTimeMillis();
        }

        /**
         * Simple getter, that return the value of {@link #lastDoc} attribute
         * @return The value of {@link #lastDoc} attribute
         */
        public synchronized Document getLastDoc() {
            return lastDoc;
        }

        /**
         * Causes waiting by wait method while the {@link #autoRefreshing} list is empty
         */
        private void waitUntilListenersEmpty() {
            while (autoRefreshing.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }

        /**
         * A container for a listener and his last auto listening time. Provide function {@link #needRefresh()}, that
         * determine, if the listener needs to apply listening
         */
        private class ListenerTime {
            /**
             * The preserved listener
             */
            private final Listener listener;
            /**
             * Time of last auto listening of the {@link #listener}
             */
            private long lastRefresh;

            /**
             * Simple listener to that initialize the listenerTime
             * @param listener Listener to be preserved
             */
            public ListenerTime(Listener listener) {
                this.listener = listener;
                lastRefresh = 0;
            }

            /**
             * Determine, if the listener needs to apply listening
             * @return The listener needs to apply listening
             */
            public boolean needRefresh() {
                return (System.currentTimeMillis() - lastRefresh) > listener.config().autoChecking();
            }

            /**
             * This function should be called after each auto listening of preserved listener
             */
            public void refreshDone() {
                lastRefresh = System.currentTimeMillis();
            }
        }
    }
}
