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

package org.jaxxy.problemdetails;

import com.google.gson.JsonObject;
import jakarta.ws.rs.core.Response;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RootExceptionMapperTest extends ProblemDetailsExceptionMapperTestCase<Exception> {

    @Override
    protected HelloResource createServiceObject() {
        return new ExceptionHelloService();
    }

    @Override
    protected RootExceptionMapper createExceptionMapper() {
        return new RootExceptionMapper();
    }

    @Test
    void shouldReturn500() {
        Response response = webTarget().path("hello").path("Jaxxy").request().get();
        assertThat(response.getStatus()).isEqualTo(500);
        JsonObject json = response.readEntity(JsonObject.class);
        assertThat(json.getAsJsonPrimitive("title").getAsString()).isEqualTo("Internal Server Error");
        assertThat(json.getAsJsonPrimitive("detail").getAsString()).isEqualTo("An internal server error has occurred.");
        assertThat(json.getAsJsonPrimitive("status").getAsInt()).isEqualTo(500);
    }
    public static class ExceptionHelloService implements HelloResource {
        @Override
        public String sayHello(String name) {
            throw new RuntimeException("Oops!");
        }
    }
}