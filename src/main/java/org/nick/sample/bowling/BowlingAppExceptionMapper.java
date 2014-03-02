package org.nick.sample.bowling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BowlingAppExceptionMapper implements
        ExceptionMapper<BowlingAppException> {

    public BowlingAppExceptionMapper() {
    }

    @Override
    public Response toResponse(BowlingAppException bowingAppException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorId(bowingAppException.getErrorId());
        errorResponse.setErrorDescription(bowingAppException.getMessage());

        Response r;
        if (bowingAppException.getErrorId() == 404) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

    }

}