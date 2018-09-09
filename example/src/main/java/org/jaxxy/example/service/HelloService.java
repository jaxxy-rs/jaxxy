/*
 * Copyright (c) 2018 The Jaxxy Authors.
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

package org.jaxxy.example.service;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jaxxy.example.HelloProtos;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/hello")
public interface HelloService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("/{name}/{n}")
    @Produces(APPLICATION_JSON)
    HelloResponse sayHello(@PathParam("name") String name, @PathParam("n") @DefaultValue("1") int n);

    @GET
    @Path("/proto/{name}/{n}")
    @Produces("application/protobuf")
    HelloProtos.HelloResponse helloProto(@PathParam("name") String name, @PathParam("n") @DefaultValue("1") int n);
}
