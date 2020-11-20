/*
 * Copyright (c) 2020 The Jaxxy Authors.
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

package org.jaxxy.guava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.ext.Provider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.RequiredArgsConstructor;
import org.jaxxy.rx.AbstractRxInvoker;

import static java.util.Optional.ofNullable;

@Provider
public class ListenableFutureInvokerProvider implements RxInvokerProvider<ListenableFutureInvoker> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ListeningExecutorService defaultExecutorService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new {@link ListenableFutureInvokerProvider} which calls {@link Executors#newCachedThreadPool()}
     * to create its default {@link ExecutorService}.
     */
    public ListenableFutureInvokerProvider() {
        this(MoreExecutors.listeningDecorator(Executors.newCachedThreadPool()));
    }

    /**
     * Creates a new {@link ListenableFutureInvokerProvider} which uses the provided default {@link ExecutorService}.
     */
    public ListenableFutureInvokerProvider(ListeningExecutorService defaultExecutorService) {
        this.defaultExecutorService = defaultExecutorService;
    }

//----------------------------------------------------------------------------------------------------------------------
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ListenableFutureInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new Invoker(syncInvoker, ofNullable(executorService).map(MoreExecutors::listeningDecorator).orElse(defaultExecutorService));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return ListenableFutureInvoker.class.equals(clazz);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @RequiredArgsConstructor
    @SuppressWarnings("unchecked")
    private static class Invoker extends AbstractRxInvoker<ListenableFuture> implements ListenableFutureInvoker {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final SyncInvoker syncInvoker;
        private final ListeningExecutorService executorService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        @Override
        protected <R> ListenableFuture async(Function<SyncInvoker, R> fn) {
            return executorService.submit(() -> fn.apply(syncInvoker));
        }
    }
}
