package org.nick.sample.bowling.exception;

public class BowlingInvalidDataException extends Exception {
    public BowlingInvalidDataException() {
        super();
    }

    public BowlingInvalidDataException(String message) {
        super(message);
    }

    public BowlingInvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public BowlingInvalidDataException(Throwable cause) {
        super(cause);
    }

    protected BowlingInvalidDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
