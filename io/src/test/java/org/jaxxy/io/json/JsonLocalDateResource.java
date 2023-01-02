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

package org.jaxxy.io.json;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.Date;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
public interface JsonLocalDateResource {
    @Path("/nowJson")
    @GET
    @Produces(APPLICATION_JSON)
    LocalDate nowJson();

    @Path("/nowVendorJson")
    @GET
    @Produces("application/vnd.jaxxy.local-date+json")
    LocalDate nowVendorJson();

    @Path("/blacklisted")
    @GET
    @Produces(APPLICATION_JSON)
    Response blacklisted();

    @Path("/unsupported")
    @GET
    @Produces(APPLICATION_JSON)
    Date unsupported();
}
