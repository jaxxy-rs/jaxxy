/*
 * Copyright (c) 2020 The Jaxxy Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jaxxy.cache;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/")
@Produces(TEXT_PLAIN)
public interface CacheableResource {

    @GET
    @Path("/noPreconditions")
    String noPreconditions();

    @GET
    @Path("/eTag")
    String eTag();

    @GET
    @Path("/lastModified")
    String lastModified();

    @GET
    @Path("/eTagAndLastModified")
    String eTagAndLastModified();

    @PUT
    @Path("/messages")
    @Consumes(TEXT_PLAIN)
    void putNewMessage(String value);

}
