package org.jaxxy.rx;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractRxInvokerTest {

    @Mock
    private SyncInvoker syncInvoker;

    private FutureInvoker invoker;

    @BeforeEach
    void setUp() {
        this.invoker = new FutureInvokerProvider().getRxInvoker(syncInvoker, Executors.newFixedThreadPool(1));
    }

    @Test
    void trace() throws Exception {
        final Response expected = Response.ok("Hello, RX!").build();
        when(syncInvoker.trace()).thenReturn(expected);
        final Future<Response> future = invoker.trace();
        assertThat(future.get()).isSameAs(expected);
        verify(syncInvoker).trace();
    }

    @Test
    void traceWithGenericResponseType() throws Exception {
        when(syncInvoker.trace(new GenericType<>(String.class))).thenReturn("Hello, RX!");
        final Future<String> future = invoker.trace(new GenericType<>(String.class));
        assertThat(future.get()).isEqualTo("Hello, RX!");
        verify(syncInvoker).trace(new GenericType<>(String.class));
    }

    @Test
    void traceWithResponseType() throws Exception {
        when(syncInvoker.trace(String.class)).thenReturn("Hello, RX!");
        final Future<String> future = invoker.trace(String.class);
        assertThat(future.get()).isEqualTo("Hello, RX!");
        verify(syncInvoker).trace(String.class);
    }

    @AfterEach
    void verifyMock() {
        verifyNoMoreInteractions(syncInvoker);
    }

}