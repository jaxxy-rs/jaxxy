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
import java.util.function.Function;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

/**
 * Provides support for RxJava {@link Single} return types from {@link javax.ws.rs.client.WebTarget}-based
 * invocations.
 */
@RequiredArgsConstructor
public class SingleInvoker implements RxInvoker<Single<?>> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final SyncInvoker syncInvoker;
    private final ExecutorService executor;

//----------------------------------------------------------------------------------------------------------------------
// RxInvoker Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Single<Response> delete() {
        return single(SyncInvoker::delete);
    }

    @Override
    public <R> Single<R> delete(Class<R> responseType) {
        return single(s -> s.delete(responseType));
    }

    @Override
    public <R> Single<R> delete(GenericType<R> responseType) {
        return single(s -> s.delete(responseType));
    }

    @Override
    public Single<Response> get() {
        return single(SyncInvoker::get);
    }

    @Override
    public <R> Single<R> get(Class<R> responseType) {
        return single(s -> s.get(responseType));
    }

    @Override
    public <R> Single<R> get(GenericType<R> responseType) {
        return single(s -> s.get(responseType));
    }

    @Override
    public Single<Response> head() {
        return single(SyncInvoker::head);
    }

    @Override
    public Single<Response> method(String name) {
        return single(s -> s.method(name));
    }

    @Override
    public <R> Single<R> method(String name, Class<R> responseType) {
        return single(s -> s.method(name, responseType));
    }

    @Override
    public <R> Single<R> method(String name, GenericType<R> responseType) {
        return single(s -> s.method(name, responseType));
    }

    @Override
    public Single<Response> method(String name, Entity<?> entity) {
        return single(s -> s.method(name, entity));
    }

    @Override
    public <R> Single<R> method(String name, Entity<?> entity, Class<R> responseType) {
        return single(s -> s.method(name, entity, responseType));
    }

    @Override
    public <R> Single<R> method(String name, Entity<?> entity, GenericType<R> responseType) {
        return single(s -> s.method(name, entity, responseType));
    }

    @Override
    public Single<Response> options() {
        return single(SyncInvoker::options);
    }

    @Override
    public <R> Single<R> options(Class<R> responseType) {
        return single(s -> s.options(responseType));
    }

    @Override
    public <R> Single<R> options(GenericType<R> responseType) {
        return single(s -> s.options(responseType));
    }

    @Override
    public Single<Response> post(Entity<?> entity) {
        return single(s -> s.post(entity));
    }

    @Override
    public <R> Single<R> post(Entity<?> entity, Class<R> responseType) {
        return single(s -> s.post(entity, responseType));
    }

    @Override
    public <R> Single<R> post(Entity<?> entity, GenericType<R> responseType) {
        return single(s -> s.post(entity, responseType));
    }

    @Override
    public Single<Response> put(Entity<?> entity) {
        return single(s -> s.put(entity));
    }

    @Override
    public <R> Single<R> put(Entity<?> entity, Class<R> responseType) {
        return single(s -> s.put(entity, responseType));
    }

    @Override
    public <R> Single<R> put(Entity<?> entity, GenericType<R> responseType) {
        return single(s -> s.put(entity, responseType));
    }

    @Override
    public Single<Response> trace() {
        return single(SyncInvoker::trace);
    }

    @Override
    public <R> Single<R> trace(Class<R> responseType) {
        return single(s -> s.trace(responseType));
    }

    @Override
    public <R> Single<R> trace(GenericType<R> responseType) {
        return single(s -> s.trace(responseType));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> Single<T> single(Function<SyncInvoker, T> fn) {
        return Single.fromFuture(executor.submit(() -> fn.apply(syncInvoker)));
    }
}
