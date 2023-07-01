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

class IllegalArgumentExceptionMapperTest extends ProblemDetailsExceptionMapperTestCase<IllegalArgumentException> {
    @Override
    protected HelloResource createServiceObject() {
        return new IllegalArgumentExceptionService();
    }

    @Override
    protected IllegalArgumentExceptionMapper createExceptionMapper() {
        return new IllegalArgumentExceptionMapper();
    }

    @Test
    void shouldReturn400() {
        Response response = webTarget().path("hello").path("Jaxxy").request().get();
        assertThat(response.getStatus()).isEqualTo(400);
        JsonObject json = response.readEntity(JsonObject.class);
        assertThat(json.getAsJsonPrimitive("title").getAsString()).isEqualTo("Bad Request");
        assertThat(json.getAsJsonPrimitive("detail").getAsString()).isEqualTo("The name \"Jaxxy\" is not allowed.");
        assertThat(json.getAsJsonPrimitive("status").getAsInt()).isEqualTo(400);
    }

    public static class IllegalArgumentExceptionService implements HelloResource {
        @Override
        public String sayHello(String name) {
            throw new IllegalArgumentException(String.format("The name \"%s\" is not allowed.", name));
        }
    }

}