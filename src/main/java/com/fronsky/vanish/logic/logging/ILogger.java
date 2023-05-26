package com.fronsky.vanish.logic.logging;

public interface ILogger {
    /**
     * Displays an informational message.
     *
     * @param message the message to be displayed.
     */
    void info(String message);

    /**
     * Displays a warning message.
     *
     * @param message the warning message to be displayed.
     */
    void warning(String message);

    /**
     * Displays a severe message.
     *
     * @param message the severe message to be displayed.
     */
    void severe(String message);

    /**
     * Displays a debug message.
     *
     * @param message the debug message to be displayed.
     */
    void debug(String message);
}
