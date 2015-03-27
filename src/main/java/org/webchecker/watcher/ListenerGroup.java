package org.webchecker.watcher;

import com.sun.istack.internal.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Class represents a group of listeners. The rule of listeners grouping is that one group has only one source
 * document to work on. This is the only thing connecting listeners in one group and each listener can (has to)
 * have his own configuration.
 * <p>
 * If listener wants to listen, he has to be in one or more groups.
 * This group contains mechanism for auto listening and refreshing the source document and for explicit listening called
 * by user.
 * <p>
 * If you want to create a group, you have to specify a supplier able to supply fresh version of document. In this case,
 * the supplier is represent by {@link java.util.function.Supplier} functional interface.
 * Each listener contains the max age of document, that can be regarded as a fresh. If this parameter is not specify
 * in constructor, the default value will be used.
 *
 * @author Matěj Kripner <kripnermatej@gmail.com>
 * @version 1.0
 */
public class ListenerGroup {

    private static final long DEFAULT_MAX_REFRESHING_DELAY = 1000 * 60 * 5;

    /**
     * Returns a new group with given document supplier and max refreshing delay. Both arguments has to be valid. This
     * means, that {@code getDocument != null && maxRefreshingDelay >= ListenerConfig.MIN_AUTO_CHECKING} has to return
     * {@code true}.
     *
     * @param document           the document supplier for the new group
     * @param maxRefreshingDelay the max age of document, that can be int the new group regarded as a fresh
     * @return new listener group with given parameters
     * @throws IllegalArgumentException if given document is {@code null} or maximal refreshing delay is not valid
     * @see #ListenerGroup(java.util.function.Supplier, long)
     * @see #newGroup(java.util.function.Supplier)
     * @see ListenerGroup
     */
    public static ListenerGroup newGroup(@NotNull Supplier<Document> document, long maxRefreshingDelay) throws IllegalArgumentException {
        if (document == null) throw new IllegalArgumentException("Document supplier can not be null");
        if (maxRefreshingDelay < ListenerConfig.MIN_AUTO_CHECKING && maxRefreshingDelay >= 0)
            throw new IllegalArgumentException("Max refresh delay can not be less than " + ListenerConfig.MIN_AUTO_CHECKING + ", but was " + maxRefreshingDelay);
        return new ListenerGroup(document, maxRefreshingDelay);
    }

    /**
     * This factory method do the same as {@link #newGroup(java.util.function.Supplier, long)}, but fill the
     * maximal refreshing delay with default value.
     *
     * @param document the document supplier for the new group
     * @return the new listener group with given parameters
     * @throws IllegalArgumentException if given document is {@code null}
     * @see #ListenerGroup(java.util.function.Supplier, long)
     * @see ListenerGroup
     */
    public static ListenerGroup newGroup(Supplier<Document> document) throws IllegalArgumentException {
        return newGroup(document, DEFAULT_MAX_REFRESHING_DELAY);
    }

    // The list of listeners of this group. Can not be null.
    private LinkedList<Listener> listeners;

    // The supplier of document, which the listeners will work on.
    private Supplier<Document> toWork;

    private final Worker worker;

    /* The max age of document, that can be in this group regarded as a fresh. If the value is negative, then
       the document will be never refreshed by calling #check() or #check(java.util.function.Predicate)
       method. */
    private long maxRefreshingDelay;

    // Mark if this group was destroyed. If true, you cannot call any method of this group.
    private boolean destroyed;

    /*
     * The constructor, that in fact do the same as #ListenerGroup(java.util.function.Supplier, java.util.LinkedList, long),
     * but do not create any listeners.
     *
     * @param toWork             the document supplier for the new group
     * @param maxRefreshingDelay the max age of document, that can be int the new group regarded as a fresh
     */
    private ListenerGroup(Supplier<Document> toWork, long maxRefreshingDelay) {
        this(toWork, new LinkedList<>(), maxRefreshingDelay);
    }

    /*
     * The main constructor, that initialize class fields with given values, initialize given listeners and start the
     * org.webchecker.watcher.ListenerGroup.Worker object.
     *
     * @param toWork             the document supplier for the new group
     * @param iniListeners       a list of listeners that will be used as a list for listeners saving
     * @param maxRefreshingDelay the max age of document, that can be int the new group regarded as a fresh
     */
    private ListenerGroup(Supplier<Document> toWork, LinkedList<Listener> iniListeners, long maxRefreshingDelay) {
        listeners = iniListeners;
        this.toWork = toWork;
        this.maxRefreshingDelay = maxRefreshingDelay;
        destroyed = false;
        iniListeners();
        Function<Worker, Integer> delaySupplier = w -> w.autoRefreshing.stream() // supply the minimum of all listener's delays
                .mapToInt(lt -> lt.listener.getConfig().getAutoChecking()).min().getAsInt();
        worker = new Worker(delaySupplier);
        worker.start();
    }

    /*
     * Initialize all listeners in listeners list.
     * <p>
     * In fact do the same as the code:
     * {@code
     * for(Listener listener : listeners) {
     *  configure(listener);
     * }
     * }
     * As you can see, the initializing of each listener is done by #configure(Listener) method.
     */
    private void iniListeners() {
        listeners.stream().forEach(this::configure);
    }

    /**
     * Configure the given listener and add it to the listeners list. If auto checking of the {@link Listener}
     * is on, add it to worker's list to apply auto listening.
     *
     * @param listener a listener to be added; if it's {@code null} it won't be added to listeners list
     * @throws AlreadyDestroyedException if {@link Listener} was already destroyed
     */
    public synchronized void addListener(Listener listener) throws AlreadyDestroyedException {
        if (listener != null) {
            checkDestroyed();
            configure(listener);
            listeners.add(listener);
            if (listener.getConfig().isAutoCheckingOn()) {
                worker.addToAutoRefreshing(listener);
            }
            synchronized (worker) {
                worker.notify();
            }
        }
    }

    /**
     * Call the {@link #addListener(Listener)} method for each listeners in given array.
     * <p>
     * In fact, this do the same as:
     * {@code
     * for(Listener l : listeners) {
     * addListener(l);
     * }
     * }
     *
     * @param listeners array of listeners to add by {@link #addListener(Listener)} method
     * @throws AlreadyDestroyedException if {@link Listener} was already destroyed
     */
    public synchronized void addAllListeners(Listener... listeners) throws AlreadyDestroyedException {
        Arrays.stream(listeners).forEach(this::addListener);
    }

    /*
     * Configure and return given listener. A listener is ready for future using after
     * configuring by this method.
     *
     * @param listener the listener to configure
     * @return given listener after configuring
     */
    private Listener configure(Listener listener) {
        if (listener.getLastChangeDocument() == null) { // listener was not yet use and there is no last version of a document
            if (worker.getLastDoc() == null)
                refresh(); // worker was not yet use and (his refresh method was not yet called)
            listener.setLastChangeDocument(worker.getLastDoc());
        }
        listener.getConfig().setOnAutoCheckingChange((oldValue, newValue) -> {
            if (newValue > 0) {
                worker.addToAutoRefreshing(listener);
            }
        });
        return listener;
    }

    /**
     * Do the same as {@link #check(java.util.function.Predicate)} method, but apply listening to all listeners.
     *
     * @throws AlreadyDestroyedException if {@link Listener} was already destroyed
     */
    public void check() throws AlreadyDestroyedException {
        check(l -> true);
    }

    /**
     * Apply listening to listeners, that return true for given predicate. If the last version of loaded document
     * is not fresh enough, refresh it by {@link #refresh()} method. A document is fresh enough when delay between
     * his loading time and now is less than max refreshing delay.
     *
     * @param update this function has to determine, if we should apply listening to a listener
     * @throws AlreadyDestroyedException if {@link Listener} was already destroyed
     */
    public synchronized void check(Predicate<Listener> update) throws AlreadyDestroyedException {
        checkDestroyed();
        if (maxRefreshingDelay > 0 && System.currentTimeMillis() - worker.getLastDocRefresh() > maxRefreshingDelay) {
            refresh();
        }
        listeners.stream().filter(update).forEach(this::applyListening);
    }

    /**
     * Refresh a document the group is working on.
     */
    public synchronized void refresh() {
        try {
            checkDestroyed();
            worker.refreshDoc();
        } catch (AlreadyDestroyedException e) {
            // Do nothing on purpose.
        }
    }

    /*
     * Apply listening to given listener.
     *
     * @param listener the listener to apply listening; if it's null, nothing will happen to it
     */
    private void applyListening(Listener listener) {
        if (listener != null) {
            Element newElementToListen = listener.getSupplyElement().apply(worker.getLastDoc()); // new element to compare with his old version
            Element oldElementToListen = listener.getSupplyElement().apply(listener.getLastChangeDocument()); // old element to compare

            if (listener.getChanged().test(oldElementToListen, newElementToListen)) { // tests, if change occurs
                listener.getAction().accept(oldElementToListen, newElementToListen); // change occurs! Call the action function of a listener
                listener.setLastChangeDocument(worker.getLastDoc()); // set the listener's document to newest version
            }
        }
    }

    /**
     * Destroy this listener group. This mean that you will be not able to use this object. And if you try to,
     * it will throw an {@link AlreadyDestroyedException}!
     * <p>
     * This method should be called, when you know, that you will not use this group in the future.
     */
    public synchronized void destroy() {
        worker.interrupt();
        listeners = null;
        toWork = null;
        destroyed = true;
    }

    /**
     * Return {@code true}, if this group was destroyed.
     *
     * @return {@code true} if this group was destroyed; otherwise {@code false}
     * @see #destroy()
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    // Check, if this group was destroyed. If yes, throw an AlreadyDestroyedException
    private void checkDestroyed() throws AlreadyDestroyedException {
        if (destroyed) throw new AlreadyDestroyedException();
    }

    /**
     * This exception should be thrown, when somebody call a method of destroyed group.
     */
    class AlreadyDestroyedException extends RuntimeException {
        public AlreadyDestroyedException() {
            super("This listener group was already destroyed");
        }
    }

    /**
     * Simple getter, that return unmodifiable version of the listeners list.
     *
     * @return unmodifiable version of the listeners list
     */
    public List<Listener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Simple getter, that return value of the max refreshing delay attribute.
     *
     * @return value of the max refreshing delay attribute
     */
    public long getMaxRefreshingDelay() {
        return maxRefreshingDelay;
    }

    /**
     * Simple setter, that set the max refreshing delay attribute to the given value.
     *
     * @param newValue new value of the max refreshing delay attribute
     */
    public synchronized void setMaxRefreshingDelay(long newValue) {
        maxRefreshingDelay = newValue;
    }

    /* Class primary used for applying auto listening. It also manage instance of Jsoup Document object
       all application works with.
       <p>
       All is automatic, you only need to initialize the worker start it by #start() method */
    private class Worker extends Thread {

        /* This function should return the minimum delay of all listener's delays.
           The function will be called with instance of this listener (delay.apply(this)). */
        private final Function<Worker, Integer> delay;

        /* The list of listeners, which has auto listening on. The worker will care about listeners, which turn auto
           listening off and it will remove it from the list. */
        private final LinkedList<ListenerTime> autoRefreshing;

        // Last version of document we are working on
        private Document lastDoc;

        /* This constructor manage given delay function and do the initialize #lastDoc refresh by #refreshDoc() function.

           @param delay Given value for {@link #delay} attribute */
        public Worker(Function<Worker, Integer> delay) {
            this.delay = delay;
            autoRefreshing = new LinkedList<>();
            refreshDoc();
            setDaemon(true);
        }

        /* If has auto listening on, configure the given listener and add it to #autoRefreshing list. Auto listening
           will be applied with further #work() method call.

           @param listener Listener to be added */
        private void addToAutoRefreshing(Listener listener) {
            if (listener.getConfig().isAutoCheckingOn()) autoRefreshing.add(configure(new ListenerTime(listener)));
        }

        /* Configure and return the given listenerTime. In fact ensure, that if the listeners will turn his auto
           listening off, remove it from the list.

           @param lt ListenerTime to be configured
           @return Configured listenerTime */
        private ListenerTime configure(ListenerTime lt) {
            lt.listener.getConfig().setOnAutoCheckingChange((oldValue, newValue) -> {
                if (newValue == 0) {
                    autoRefreshing.remove(lt);
                }
            });
            return lt;
        }

        /* Do the worker's work (applying auto listening) until the worker is not interrupted. Wait, while the
           #autoRefreshing list is empty. */
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

        /* Apply listening to all listeners in #autoRefreshing list, which needs it. Listener need listening,
           when the time between last listening and now is greater than listener's autoChecking parameter. */
        private void work() {
            autoRefreshing.stream().filter(ListenerTime::needRefresh).forEach(lt -> {
                applyListening(lt.listener);
                lt.refreshDone();
            });
            refreshDoc();
        }

        // Time, when the document we are working on was last refreshed.
        private long lastDocRefresh = 0;

        /* Simple getter, that return the value of #lastDocRefresh attribute.

           @return The value of #lastDocRefresh attribute */
        public long getLastDocRefresh() {
            return lastDocRefresh;
        }

        private synchronized void refreshDoc() {
            lastDoc = toWork.get();
            lastDocRefresh = System.currentTimeMillis();
        }

        /* Simple getter, that return the value of #lastDoc attribute.

           @return The value of #lastDoc attribute */
        public synchronized Document getLastDoc() {
            return lastDoc;
        }

        // Causes waiting by wait method while the #autoRefreshing list is empty.
        private void waitUntilListenersEmpty() {
            while (autoRefreshing.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }

        /* A container for a listener and his last auto listening time. Provide function #needRefresh(), that
           determine, if the listener needs to apply listening. */
        private class ListenerTime {

            // The preserved listener.
            private final Listener listener;

            // Time of last auto listening of the #listener.
            private long lastRefresh;

            /* Simple listener to that initialize the listenerTime.

               @param listener Listener to be preserved */
            public ListenerTime(Listener listener) {
                this.listener = listener;
                lastRefresh = 0;
            }

            /* Determine, if the listener needs to apply listening.

               @return The listener needs to apply listening */
            public boolean needRefresh() {
                return (System.currentTimeMillis() - lastRefresh) > listener.getConfig().getAutoChecking();
            }

            // This function should be called after each auto listening of preserved listener.
            public void refreshDone() {
                lastRefresh = System.currentTimeMillis();
            }
        }
    }
}
