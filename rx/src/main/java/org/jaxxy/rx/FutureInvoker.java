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

package org.jaxxy.rx;

import java.util.concurrent.Future;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * An {@link RxInvoker} which supports Java {@link Future}.
 */
public interface FutureInvoker extends RxInvoker<Future> {
//----------------------------------------------------------------------------------------------------------------------
// RxInvoker Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    Future<Response> delete();

    @Override
    <R> Future<R> delete(Class<R> responseType);

    @Override
    <R> Future<R> delete(GenericType<R> responseType);

    @Override
    Future<Response> get();

    @Override
    <R> Future<R> get(Class<R> responseType);

    @Override
    <R> Future<R> get(GenericType<R> responseType);

    @Override
    Future<Response> head();

    @Override
    Future<Response> method(String name);

    @Override
    <R> Future<R> method(String name, Class<R> responseType);

    @Override
    <R> Future<R> method(String name, GenericType<R> responseType);

    @Override
    Future<Response> method(String name, Entity<?> entity);

    @Override
    <R> Future<R> method(String name, Entity<?> entity, Class<R> responseType);

    @Override
    <R> Future<R> method(String name, Entity<?> entity, GenericType<R> responseType);

    @Override
    Future<Response> options();

    @Override
    <R> Future<R> options(Class<R> responseType);

    @Override
    <R> Future<R> options(GenericType<R> responseType);

    @Override
    Future<Response> post(Entity<?> entity);

    @Override
    <R> Future<R> post(Entity<?> entity, Class<R> responseType);

    @Override
    <R> Future<R> post(Entity<?> entity, GenericType<R> responseType);

    @Override
    Future<Response> put(Entity<?> entity);

    @Override
    <R> Future<R> put(Entity<?> entity, Class<R> responseType);

    @Override
    <R> Future<R> put(Entity<?> entity, GenericType<R> responseType);

    @Override
    Future<Response> trace();

    @Override
    <R> Future<R> trace(Class<R> responseType);

    @Override
    <R> Future<R> trace(GenericType<R> responseType);
}
