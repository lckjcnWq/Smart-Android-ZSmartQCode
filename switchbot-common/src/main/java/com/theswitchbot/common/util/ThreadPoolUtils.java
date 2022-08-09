package com.theswitchbot.common.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2016-08-15.
 */
public class ThreadPoolUtils {
    private static ThreadPoolUtils sharedInstance;
    private ThreadPoolExecutor service;

    private ThreadPoolUtils(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        service = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory());
        service.allowCoreThreadTimeOut(true);
    }

    /**
     * Returns a shared instance of the ThreadPoolUtils class.
     *
     * @return Returns a shared instance of the ThreadPoolUtils class.
     */
    public static ThreadPoolUtils getInstance() {

        synchronized (ThreadPoolUtils.class) {

            if (sharedInstance == null) {
                sharedInstance = new ThreadPoolUtils(5, 64, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(15, true));
            }
        }

        return sharedInstance;
    }

    /**
     * Clears the queue of the thread pool.
     */
    public void clearQueue() {
        service.getQueue().clear();
    }

    /**
     * Returns the max pool size of the ThreadPoolUtils.
     *
     * @return Returns the max pool size of the ThreadPoolUtils.
     */
    public int getMaxPoolSize() {
        return service.getMaximumPoolSize();
    }

    /**
     * Returns the active task count of the ThreadPoolUtils.
     *
     * @return Returns the active task count of the ThreadPoolUtils.
     */
    public int getActiveTaskCount() {
        return service.getActiveCount();
    }

    /**
     * Returns the queued task count of the thread pool.
     *
     * @return Returns the queued task count of the thread pool.
     */
    public int getQueuedTaskCount() {
        return service.getQueue().size();
    }

    /**
     * Returns the uncompleted task count of the ThreadPoolUtils.
     *
     * @return Returns the uncompleted task count of the ThreadPoolUtils.
     */
    public int getUncompletedTaskCount() {
        return service.getQueue().size() - service.getActiveCount();
    }

    /**
     * Removes this task from the executor's internal queue if it is present, thus causing it not to be run if it has not already started.
     *
     * @param runnable The task to remove.
     */
    public boolean remove(Runnable runnable) {
        return service.remove(runnable);
    }

    /**
     * Gets the rejected execution handler.
     *
     * @return The rejected execution handler.
     */
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return service.getRejectedExecutionHandler();
    }

    /**
     * Sets the rejected execution handler, to handle errors on when submitting and running tasks on the thread pool.
     *
     * @param rejectedExecutionHandler The rejected execution handler.
     */
    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        service.setRejectedExecutionHandler(rejectedExecutionHandler);
    }

    /**
     * Submits a task to be run on a background thread pool.
     *
     * @param runnable The runnable to be run on a background thread.
     */
    public void submit(Runnable runnable) {
        service.submit(runnable);
    }

    /**
     * Terminates the thread pool.
     *
     * @throws InterruptedException Interrupted Exception
     */
    public void terminateThreadPool() throws InterruptedException {
        terminateThreadPool(false);
    }

    /**
     * Terminates the thread pool.
     *
     * @param shouldFinishQueue Should the pool wait for tasks to finish before terminating.
     * @throws InterruptedException Interrupted Exception
     */
    public void terminateThreadPool(boolean shouldFinishQueue) throws InterruptedException {

        if (shouldFinishQueue) {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } else {
            service.shutdownNow();
        }
    }
}
