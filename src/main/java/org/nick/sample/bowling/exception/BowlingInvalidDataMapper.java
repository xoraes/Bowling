package org.nick.sample.bowling.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BowlingInvalidDataMapper implements ExceptionMapper<BowlingInvalidDataException> {

    public BowlingInvalidDataMapper() {
    }

    @Override
    public Response toResponse(BowlingInvalidDataException invalidDataException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorId(400);
        errorResponse.setErrorDescription(invalidDataException.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).
                entity(errorResponse).type(MediaType.APPLICATION_JSON).build();

    }

}