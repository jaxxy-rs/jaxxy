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

import jakarta.ws.rs.client.SyncInvoker;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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