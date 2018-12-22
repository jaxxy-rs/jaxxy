package org.jaxxy.guava;

import java.util.concurrent.Executors;

import javax.ws.rs.core.MediaType;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.jaxxy.test.JaxrsClientConfig;
import org.jaxxy.test.JaxrsTestCase;
import org.jaxxy.test.hello.HelloResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.util.concurrent.Futures.getUnchecked;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    protected void configureClient(JaxrsClientConfig config) {
        super.configureClient(config);
        config.withProvider(new ListenableFutureInvokerProvider(MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1))));
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