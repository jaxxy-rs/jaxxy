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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MonoInvoker implements RxInvoker<Mono<?>> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final SyncInvoker syncInvoker;
    private final ExecutorService executor;

//----------------------------------------------------------------------------------------------------------------------
// RxInvoker Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Mono<Response> delete() {
        return single(SyncInvoker::delete);
    }

    @Override
    public <R> Mono<R> delete(Class<R> responseType) {
        return single(s -> s.delete(responseType));
    }

    @Override
    public <R> Mono<R> delete(GenericType<R> responseType) {
        return single(s -> s.delete(responseType));
    }

    @Override
    public Mono<Response> get() {
        return single(SyncInvoker::get);
    }

    @Override
    public <R> Mono<R> get(Class<R> responseType) {
        return single(s -> s.get(responseType));
    }

    @Override
    public <R> Mono<R> get(GenericType<R> responseType) {
        return single(s -> s.get(responseType));
    }

    @Override
    public Mono<Response> head() {
        return single(SyncInvoker::head);
    }

    @Override
    public Mono<Response> method(String name) {
        return single(s -> s.method(name));
    }

    @Override
    public <R> Mono<R> method(String name, Class<R> responseType) {
        return single(s -> s.method(name, responseType));
    }

    @Override
    public <R> Mono<R> method(String name, GenericType<R> responseType) {
        return single(s -> s.method(name, responseType));
    }

    @Override
    public Mono<Response> method(String name, Entity<?> entity) {
        return single(s -> s.method(name, entity));
    }

    @Override
    public <R> Mono<R> method(String name, Entity<?> entity, Class<R> responseType) {
        return single(s -> s.method(name, entity, responseType));
    }

    @Override
    public <R> Mono<R> method(String name, Entity<?> entity, GenericType<R> responseType) {
        return single(s -> s.method(name, entity, responseType));
    }

    @Override
    public Mono<Response> options() {
        return single(SyncInvoker::options);
    }

    @Override
    public <R> Mono<R> options(Class<R> responseType) {
        return single(s -> s.options(responseType));
    }

    @Override
    public <R> Mono<R> options(GenericType<R> responseType) {
        return single(s -> s.options(responseType));
    }

    @Override
    public Mono<Response> post(Entity<?> entity) {
        return single(s -> s.post(entity));
    }

    @Override
    public <R> Mono<R> post(Entity<?> entity, Class<R> responseType) {
        return single(s -> s.post(entity, responseType));
    }

    @Override
    public <R> Mono<R> post(Entity<?> entity, GenericType<R> responseType) {
        return single(s -> s.post(entity, responseType));
    }

    @Override
    public Mono<Response> put(Entity<?> entity) {
        return single(s -> s.put(entity));
    }

    @Override
    public <R> Mono<R> put(Entity<?> entity, Class<R> responseType) {
        return single(s -> s.put(entity, responseType));
    }

    @Override
    public <R> Mono<R> put(Entity<?> entity, GenericType<R> responseType) {
        return single(s -> s.put(entity, responseType));
    }

    @Override
    public Mono<Response> trace() {
        return single(SyncInvoker::trace);
    }

    @Override
    public <R> Mono<R> trace(Class<R> responseType) {
        return single(s -> s.trace(responseType));
    }

    @Override
    public <R> Mono<R> trace(GenericType<R> responseType) {
        return single(s -> s.trace(responseType));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> Mono<T> single(Function<SyncInvoker, T> fn) {
        return Mono.fromFuture(CompletableFuture.supplyAsync(() -> fn.apply(syncInvoker), executor));
    }
}
