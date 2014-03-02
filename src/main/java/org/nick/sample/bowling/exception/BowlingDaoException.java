package org.nick.sample.bowling.exception;

/**
 * Created by ndhupia on 2/22/14.
 */
public class BowlingDaoException extends Exception {
    public BowlingDaoException() {
        super();
    }

    public BowlingDaoException(String message) {
        super(message);
    }

    public BowlingDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public BowlingDaoException(Throwable cause) {
        super(cause);
    }

    protected BowlingDaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
