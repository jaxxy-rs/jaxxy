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

package org.jaxxy.rxjava;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.reactivex.Single;
import org.jaxxy.test.JaxrsClientConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleInvokerProviderTest extends JaxrsTestCase<StringResource> {
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
        config.withProvider(SingleInvokerProvider.builder().build());
    }

    @Override
    protected StringResource createServiceObject() {
        return resource;
    }

    @Test
    public void get() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().get();
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void getWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().get(new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void getWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().get(String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void options() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().options();
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void optionsWithGenericResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().options(new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void optionsWithResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().options(String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void delete() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().delete();
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void deleteWithResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().delete(String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void deleteWithGenericResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().delete(new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void head() {
        final Single<Response> single = invoker().head();
        assertThat(single.blockingGet().getStatus()).isEqualTo(204);
    }

    @Test
    public void method() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().method(HttpMethod.GET);
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().method(HttpMethod.GET, String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Single<String> single = invoker().method(HttpMethod.GET, new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntity() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().method(HttpMethod.PUT, Entity.text("RX"));
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntityAndResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Single<String> single = invoker().method(HttpMethod.PUT, Entity.text("RX"), String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntityAndGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Single<String> single = invoker().method(HttpMethod.PUT, Entity.text("RX"), new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }
    
    @Test
    public void put() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().put(Entity.text("RX"));
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void putWithResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Single<String> single = invoker().put(Entity.text("RX"), String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void putWithGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Single<String> single = invoker().put(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void post() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Single<Response> single = invoker().post(Entity.text("RX"));
        assertThat(single.blockingGet().readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void postWithResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Single<String> single = invoker().post(Entity.text("RX"), String.class);
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    @Test
    public void postWithGenericResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Single<String> single = invoker().post(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(single.blockingGet()).isEqualTo("Hello, RX!");
    }

    private SingleInvoker invoker() {
        return webTarget()
                .request(MediaType.TEXT_PLAIN_TYPE)
                .rx(SingleInvoker.class);
    }
}