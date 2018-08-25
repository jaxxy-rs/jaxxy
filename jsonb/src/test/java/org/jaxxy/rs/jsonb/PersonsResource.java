package org.jaxxy.rs.jsonb;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/persons")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public interface PersonsResource {

    @GET
    @Path("/{id}")
    Person get(@PathParam("id") String id);
}
