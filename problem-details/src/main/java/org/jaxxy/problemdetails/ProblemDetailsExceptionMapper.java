package org.jaxxy.problemdetails;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static org.jaxxy.problemdetails.ProblemDetails.PROBLEM_JSON_TYPE;

@Provider
@Produces(PROBLEM_JSON_TYPE)
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class ProblemDetailsExceptionMapper<E extends Exception> implements ExceptionMapper<E> {

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ExceptionMapper ---------------------

    @Override
    public final Response toResponse(E exception) {
        final ProblemDetails details = createProblemDetails(exception);
        final Response.Status status = Optional.ofNullable(details)
                .map(ProblemDetails::getStatus)
                .orElse(Response.Status.INTERNAL_SERVER_ERROR);

        return Response.status(status)
                .type(PROBLEM_JSON_TYPE)
                .entity(details)
                .build();
    }

// -------------------------- OTHER METHODS --------------------------

    protected abstract ProblemDetails createProblemDetails(E exception);

}
