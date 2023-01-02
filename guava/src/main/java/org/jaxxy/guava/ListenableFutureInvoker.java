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

package org.jaxxy.guava;

import com.google.common.util.concurrent.ListenableFuture;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.RxInvoker;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

/**
 * An {@link RxInvoker} implementation which supports Guava {@link ListenableFuture}.
 */
public interface ListenableFutureInvoker extends RxInvoker<ListenableFuture> {
//----------------------------------------------------------------------------------------------------------------------
// RxInvoker Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    ListenableFuture<Response> delete();

    @Override
    <R> ListenableFuture<R> delete(Class<R> responseType);

    @Override
    <R> ListenableFuture<R> delete(GenericType<R> responseType);

    @Override
    ListenableFuture<Response> get();

    @Override
    <R> ListenableFuture<R> get(Class<R> responseType);

    @Override
    <R> ListenableFuture<R> get(GenericType<R> responseType);

    @Override
    ListenableFuture<Response> head();

    @Override
    ListenableFuture<Response> method(String name);

    @Override
    <R> ListenableFuture<R> method(String name, Class<R> responseType);

    @Override
    <R> ListenableFuture<R> method(String name, GenericType<R> responseType);

    @Override
    ListenableFuture<Response> method(String name, Entity<?> entity);

    @Override
    <R> ListenableFuture<R> method(String name, Entity<?> entity, Class<R> responseType);

    @Override
    <R> ListenableFuture<R> method(String name, Entity<?> entity, GenericType<R> responseType);

    @Override
    ListenableFuture<Response> options();

    @Override
    <R> ListenableFuture<R> options(Class<R> responseType);

    @Override
    <R> ListenableFuture<R> options(GenericType<R> responseType);

    @Override
    ListenableFuture<Response> post(Entity<?> entity);

    @Override
    <R> ListenableFuture<R> post(Entity<?> entity, Class<R> responseType);

    @Override
    <R> ListenableFuture<R> post(Entity<?> entity, GenericType<R> responseType);

    @Override
    ListenableFuture<Response> put(Entity<?> entity);

    @Override
    <R> ListenableFuture<R> put(Entity<?> entity, Class<R> responseType);

    @Override
    <R> ListenableFuture<R> put(Entity<?> entity, GenericType<R> responseType);

    @Override
    ListenableFuture<Response> trace();

    @Override
    <R> ListenableFuture<R> trace(Class<R> responseType);

    @Override
    <R> ListenableFuture<R> trace(GenericType<R> responseType);
}
