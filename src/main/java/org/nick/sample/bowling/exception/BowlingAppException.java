package org.nick.sample.bowling.exception;

/**
 * Created by ndhupia on 2/22/14.
 */
public class BowlingAppException extends Exception {
    private static final long serialVersionUID = 4351720088030656859L;
    private int errorId;

    public BowlingAppException() {
        super();
    }

    public BowlingAppException(String message) {
        super(message);
    }
    public BowlingAppException(int errorId, String message) {
        super(message);
        setErrorId(errorId);
    }

    public BowlingAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public BowlingAppException(Throwable cause) {
        super(cause);
    }

    protected BowlingAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }
}
