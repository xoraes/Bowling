package org.nick.sample.bowling.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BowlingAppExceptionMapper implements ExceptionMapper<BowlingAppException> {

    @Override
    public Response toResponse(BowlingAppException serverException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorId(400);
        errorResponse.setErrorDescription(serverException.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

    }

}