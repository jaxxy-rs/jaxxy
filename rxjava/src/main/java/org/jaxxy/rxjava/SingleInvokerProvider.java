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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import static java.util.Optional.ofNullable;

@Builder
@RequiredArgsConstructor
public class SingleInvokerProvider implements RxInvokerProvider<SingleInvoker> {
//----------------------------------------------------------------------------------------------------------------------
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Builder.Default
    private final ExecutorService defaultExecutorService = ForkJoinPool.commonPool();

    @Override
    public SingleInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new SingleInvoker(syncInvoker, ofNullable(executorService).orElse(defaultExecutorService));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return SingleInvoker.class.equals(clazz);
    }
}
