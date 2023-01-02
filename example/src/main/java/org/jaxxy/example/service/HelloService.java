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

package org.jaxxy.example.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import org.jaxxy.example.HelloProtos;

import static com.fasterxml.jackson.jakarta.rs.smile.SmileMediaTypes.APPLICATION_JACKSON_SMILE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@OpenAPIDefinition(
        info = @Info(
                title = "A friendly hello service",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
@Path("/hello")
public interface HelloService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @GET
    @Path("/{name}/{n}")
    @Produces(APPLICATION_JSON)
    HelloResponse hello(@PathParam("name") String name, @PathParam("n") @DefaultValue("1") int n);

    @GET
    @Path("/smile/{name}/{n}")
    @Produces(APPLICATION_JACKSON_SMILE)
    HelloResponse helloSmile(@PathParam("name") String name, @PathParam("n") @DefaultValue("1") int n);

    @GET
    @Path("/yaml/{name}/{n}")
    @Produces("application/yaml")
    HelloResponse helloYaml(@PathParam("name") String name, @PathParam("n") @DefaultValue("1") int n);

    @GET
    @Path("/proto/{name}/{n}")
    @Produces("application/protobuf")
    HelloProtos.HelloResponse helloProto(@PathParam("name") String name, @PathParam("n") @DefaultValue("1") int n);
}
