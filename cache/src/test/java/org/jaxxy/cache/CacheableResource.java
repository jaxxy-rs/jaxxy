/*
 * Copyright (c) 2018-2023 The Jaxxy Authors.
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

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

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
