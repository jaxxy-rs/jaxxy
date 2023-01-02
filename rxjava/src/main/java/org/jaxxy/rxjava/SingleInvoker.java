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

package org.jaxxy.rxjava;

import io.reactivex.Single;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.RxInvoker;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

/**
 * An {@link RxInvoker} implementation which supports RxJava {@link Single}.
 */
public interface SingleInvoker extends RxInvoker<Single> {

    @Override
    Single<Response> delete();

    @Override
    <R> Single<R> delete(Class<R> responseType);

    @Override
    <R> Single<R> delete(GenericType<R> responseType);

    @Override
    Single<Response> get();

    @Override
    <R> Single<R> get(Class<R> responseType);

    @Override
    <R> Single<R> get(GenericType<R> responseType);

    @Override
    Single<Response> head();

    @Override
    Single<Response> method(String name);

    @Override
    <R> Single<R> method(String name, Class<R> responseType);

    @Override
    <R> Single<R> method(String name, GenericType<R> responseType);

    @Override
    Single<Response> method(String name, Entity<?> entity);

    @Override
    <R> Single<R> method(String name, Entity<?> entity, Class<R> responseType);

    @Override
    <R> Single<R> method(String name, Entity<?> entity, GenericType<R> responseType);

    @Override
    Single<Response> options();

    @Override
    <R> Single<R> options(Class<R> responseType);

    @Override
    <R> Single<R> options(GenericType<R> responseType);

    @Override
    Single<Response> post(Entity<?> entity);

    @Override
    <R> Single<R> post(Entity<?> entity, Class<R> responseType);

    @Override
    <R> Single<R> post(Entity<?> entity, GenericType<R> responseType);

    @Override
    Single<Response> put(Entity<?> entity);

    @Override
    <R> Single<R> put(Entity<?> entity, Class<R> responseType);

    @Override
    <R> Single<R> put(Entity<?> entity, GenericType<R> responseType);

    @Override
    Single<Response> trace();

    @Override
    <R> Single<R> trace(Class<R> responseType);

    @Override
    <R> Single<R> trace(GenericType<R> responseType);
}
