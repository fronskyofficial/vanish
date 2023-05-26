package com.fronsky.vanish.logic.task;

public interface ITask {
    /**
     * Runs the task.
     */
    void run();

    /**
     * Retrieves the delay of the task.
     *
     * @return the delay of the task in milliseconds.
     */
    long getDelay();

    /**
     * Retrieves the period of the task.
     *
     * @return the period of the task in milliseconds.
     */
    long getPeriod();

    /**
     * Checks if the task is asynchronous.
     *
     * @return true if the task is asynchronous, false otherwise.
     */
    boolean isAsync();

}
