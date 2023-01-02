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

import jakarta.ws.rs.client.RxInvokerProvider;
import jakarta.ws.rs.client.SyncInvoker;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Provider
public class FutureInvokerProvider implements RxInvokerProvider<FutureInvoker> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ExecutorService defaultExecutorService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new {@link FutureInvokerProvider} which calls {@link Executors#newCachedThreadPool()}
     * to create its default {@link ExecutorService}.
     */
    public FutureInvokerProvider() {
        this(Executors.newCachedThreadPool());
    }

    /**
     * Creates a new {@link FutureInvokerProvider} which uses the provided default {@link ExecutorService}.
     * @param defaultExecutorService
     */
    public FutureInvokerProvider(ExecutorService defaultExecutorService) {
        this.defaultExecutorService = defaultExecutorService;
    }

//----------------------------------------------------------------------------------------------------------------------
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public FutureInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new Invoker(syncInvoker, ofNullable(executorService).orElse(defaultExecutorService));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return FutureInvoker.class.equals(clazz);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @RequiredArgsConstructor
    @SuppressWarnings("unchecked")
    private static class Invoker extends AbstractRxInvoker<Future> implements FutureInvoker {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final SyncInvoker syncInvoker;
        private final ExecutorService executorService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        @Override
        protected <R> Future async(Function<SyncInvoker, R> fn) {
            return executorService.submit(() -> fn.apply(syncInvoker));
        }
    }
}
