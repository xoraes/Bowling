package org.nick.sample.bowling.exception;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the error object sent to client
 */
@XmlRootElement
public class ErrorResponse {

    private String errorDescription;
    private int errorId;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

}