package org.musictheorist.mother3hqaudiopatcher.ui.popups.error;

final public class LocalizedException extends Exception {
    public LocalizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}