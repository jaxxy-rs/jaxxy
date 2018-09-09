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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.ext.Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;

import static java.util.Optional.ofNullable;

@Provider
@Builder
@AllArgsConstructor
public class MonoInvokerProvider implements RxInvokerProvider<MonoInvoker> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Builder.Default
    private final ExecutorService defaultExecutorService = ForkJoinPool.commonPool();

//----------------------------------------------------------------------------------------------------------------------
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public MonoInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new MonoInvoker(syncInvoker, ofNullable(executorService).orElse(defaultExecutorService));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return MonoInvoker.class.equals(clazz);
    }
}
