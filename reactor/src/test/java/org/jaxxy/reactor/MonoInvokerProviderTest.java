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

package org.jaxxy.reactor;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jaxxy.test.JaxrsClientConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonoInvokerProviderTest extends JaxrsTestCase<StringResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private StringResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configureClient(JaxrsClientConfig config) {
        super.configureClient(config);
        config.withProvider(MonoInvokerProvider.builder().build());
    }

    @Override
    protected StringResource createServiceObject() {
        return resource;
    }

    @Test
    public void get() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().get();
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void getWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().get(new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void getWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().get(String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void options() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().options();
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void optionsWithGenericResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().options(new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void optionsWithResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().options(String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void delete() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().delete();
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void deleteWithResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().delete(String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void deleteWithGenericResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().delete(new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void head() {
        final Mono<Response> mono = invoker().head();
        assertThat(mono.block().getStatus()).isEqualTo(204);
    }

    @Test
    public void method() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().method(HttpMethod.GET);
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().method(HttpMethod.GET, String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().method(HttpMethod.GET, new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntity() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().method(HttpMethod.PUT, Entity.text("RX"));
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntityAndResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().method(HttpMethod.PUT, Entity.text("RX"), String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntityAndGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().method(HttpMethod.PUT, Entity.text("RX"), new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void put() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().put(Entity.text("RX"));
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void putWithResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().put(Entity.text("RX"), String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void putWithGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().put(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void post() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Mono<Response> mono = invoker().post(Entity.text("RX"));
        assertThat(mono.block().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void postWithResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().post(Entity.text("RX"), String.class);
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    @Test
    public void postWithGenericResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Mono<String> mono = invoker().post(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(mono.block()).isEqualTo("Hello, RX!");
    }

    private MonoInvoker invoker() {
        return webTarget()
                .request(MediaType.TEXT_PLAIN_TYPE)
                .rx(MonoInvoker.class);
    }
}