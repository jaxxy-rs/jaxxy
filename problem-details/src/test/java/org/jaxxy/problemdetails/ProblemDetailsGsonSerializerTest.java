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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemDetailsGsonSerializerTest {

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ProblemDetails.class, new ProblemDetailsGsonSerializer())
                .create();
    }

    @Test
    void shouldSerializeAsJsonNullWhenNull() {
        Gson gson = createGson();

        JsonElement json = gson.toJsonTree(null, ProblemDetails.class);
        assertThat(json).isEqualTo(JsonNull.INSTANCE);
    }


    @Test
    void shouldSerializeTypeAsString() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .type(URI.create("https://foo/bar/baz"))
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonPrimitive("type").getAsString()).isEqualTo("https://foo/bar/baz");
    }

    @Test
    void shouldSerializeTitleAsString() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .title("User already exists.")
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonPrimitive("title").getAsString()).isEqualTo("User already exists.");
    }

    @Test
    void shouldSerializeTitleAsNumber() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .status(Response.Status.BAD_REQUEST)
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonPrimitive("status").getAsInt()).isEqualTo(400);
    }

    @Test
    void shouldSerializeDetailAsString() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .detail("User named 'Joe' already exists.")
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonPrimitive("detail").getAsString()).isEqualTo("User named 'Joe' already exists.");
    }

    @Test
    void shouldSerializeInstanceAsString() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .instance(URI.create("https://foo/bar/baz/123"))
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonPrimitive("instance").getAsString()).isEqualTo("https://foo/bar/baz/123");
    }

    @Test
    void shouldSerializePrimitiveExtensions() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .extension("foo", "ABC123")
                .extension("bar", false)
                .extension("baz", 123)
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonPrimitive("foo").getAsString()).isEqualTo("ABC123");
        assertThat(json.getAsJsonPrimitive("bar").getAsBoolean()).isFalse();
        assertThat(json.getAsJsonPrimitive("baz").getAsInt()).isEqualTo(123);
    }

    @Test
    void shouldSerializeComplexExtensions() {
        Gson gson = createGson();
        ProblemDetails details = ProblemDetails.builder()
                .extension("custom", CustomExtensionObject.builder()
                        .foo("ABC123")
                        .bar(true)
                        .baz(123)
                        .build())
                .build();
        JsonObject json = gson.toJsonTree(details).getAsJsonObject();
        assertThat(json.getAsJsonObject("custom")).isEqualTo(gson.toJsonTree(details.getExtensions().get("custom")));
    }

    @Value
    @Builder
    private static class CustomExtensionObject {
        String foo;
        boolean bar;
        int baz;
    }
}