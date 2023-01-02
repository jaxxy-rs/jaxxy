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

package org.jaxxy.rx;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.str.StringResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FutureInvokerProviderTest extends JaxrsTestCase<StringResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private StringResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    private static <T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withClientProvider(new FutureInvokerProvider());
    }

    @Override
    protected StringResource createServiceObject() {
        return resource;
    }

    @Test
    void delete() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().delete();
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void deleteWithGenericResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().delete(new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void deleteWithResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().delete(String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void get() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().get();
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void getWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().get(new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void getWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().get(String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void head() {
        final Future<Response> future = invoker().head();
        assertThat(get(future).getStatus()).isEqualTo(204);
    }

    private FutureInvoker invoker() {
        return webTarget()
                .request(MediaType.TEXT_PLAIN_TYPE)
                .rx(FutureInvoker.class);
    }

    @Test
    void method() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().method(HttpMethod.GET);
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void methodWithEntity() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().method(HttpMethod.PUT, Entity.text("RX"));
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void methodWithEntityAndGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.PUT, Entity.text("RX"), new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void methodWithEntityAndResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.PUT, Entity.text("RX"), String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void methodWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.GET, new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void methodWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.GET, String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void options() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().options();
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void optionsWithGenericResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().options(new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void optionsWithResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().options(String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void post() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().post(Entity.text("RX"));
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void postWithGenericResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().post(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void postWithResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().post(Entity.text("RX"), String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void put() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().put(Entity.text("RX"));
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    void putWithGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().put(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    void putWithResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().put(Entity.text("RX"), String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }
}