package org.jaxxy.guava;

import javax.ws.rs.core.MediaType;

import com.google.common.util.concurrent.ListenableFuture;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.fixture.JaxrsServiceFixtureFactory;
import org.jaxxy.test.hello.HelloResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.util.concurrent.Futures.getUnchecked;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListenableFutureInvokerProviderTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private HelloResource resource;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected JaxrsServiceFixtureFactory createJaxrsFixtureFactory() {
        return super.createJaxrsFixtureFactory()
                .withClientProvider(new ListenableFutureInvokerProvider());
    }

    @Override
    protected HelloResource createServiceObject() {
        return resource;
    }

    @Test
    public void get() {
        when(resource.sayHello("RX")).thenReturn("Hello, RX!");
        final ListenableFuture<String> response = webTarget()
                .path("hello").path("RX")
                .request(MediaType.TEXT_PLAIN_TYPE)
                .rx(ListenableFutureInvoker.class)
                .get(String.class);
        assertThat(getUnchecked(response)).isEqualTo("Hello, RX!");
    }
}