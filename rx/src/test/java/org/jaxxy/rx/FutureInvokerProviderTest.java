package org.jaxxy.rx;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.str.StringResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FutureInvokerProviderTest extends JaxrsTestCase<StringResource> {
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
    public void delete() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().delete();
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void deleteWithGenericResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().delete(new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void deleteWithResponseType() {
        when(resource.delete()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().delete(String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void get() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().get();
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void getWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().get(new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void getWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().get(String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void head() {
        final Future<Response> future = invoker().head();
        assertThat(get(future).getStatus()).isEqualTo(204);
    }

    private FutureInvoker invoker() {
        return webTarget()
                .request(MediaType.TEXT_PLAIN_TYPE)
                .rx(FutureInvoker.class);
    }

    @Test
    public void method() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().method(HttpMethod.GET);
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntity() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().method(HttpMethod.PUT, Entity.text("RX"));
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntityAndGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.PUT, Entity.text("RX"), new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithEntityAndResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.PUT, Entity.text("RX"), String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithGenericResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.GET, new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void methodWithResponseType() {
        when(resource.get()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().method(HttpMethod.GET, String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void options() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().options();
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void optionsWithGenericResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().options(new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void optionsWithResponseType() {
        when(resource.options()).thenReturn("Hello, RX!");
        final Future<String> future = invoker().options(String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void post() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().post(Entity.text("RX"));
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void postWithGenericResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().post(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void postWithResponseType() {
        when(resource.post("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().post(Entity.text("RX"), String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void put() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<Response> future = invoker().put(Entity.text("RX"));
        assertThat(get(future).readEntity(String.class)).isEqualTo("Hello, RX!");
    }

    @Test
    public void putWithGenericResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().put(Entity.text("RX"), new GenericType<>(String.class));
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }

    @Test
    public void putWithResponseType() {
        when(resource.put("RX")).thenReturn("Hello, RX!");
        final Future<String> future = invoker().put(Entity.text("RX"), String.class);
        assertThat(get(future)).isEqualTo("Hello, RX!");
    }
}