package org.jaxxy.test.str;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/")
@Produces(TEXT_PLAIN)
@Consumes(TEXT_PLAIN)
public interface StringResource {

    @GET
    String get();

    @DELETE
    String delete();

    @OPTIONS
    String options();

    @PUT
    String put(String entity);

    @POST
    String post(String entity);

    @TRACE
    String trace();
}