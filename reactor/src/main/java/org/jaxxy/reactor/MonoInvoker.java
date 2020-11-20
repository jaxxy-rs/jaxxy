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

package org.jaxxy.reactor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import reactor.core.publisher.Mono;

/**
 * An {@link RxInvoker} which supports Project Reactor {@link Mono}.
 */
public interface MonoInvoker extends RxInvoker<Mono> {
    @Override
    Mono<Response> delete();

    @Override
    <R> Mono<R> delete(Class<R> responseType);

    @Override
    <R> Mono<R> delete(GenericType<R> responseType);

    @Override
    Mono<Response> get();

    @Override
    <R> Mono<R> get(Class<R> responseType);

    @Override
    <R> Mono<R> get(GenericType<R> responseType);

    @Override
    Mono<Response> head();

    @Override
    Mono<Response> method(String name);

    @Override
    <R> Mono<R> method(String name, Class<R> responseType);

    @Override
    <R> Mono<R> method(String name, GenericType<R> responseType);

    @Override
    Mono<Response> method(String name, Entity<?> entity);

    @Override
    <R> Mono<R> method(String name, Entity<?> entity, Class<R> responseType);

    @Override
    <R> Mono<R> method(String name, Entity<?> entity, GenericType<R> responseType);

    @Override
    Mono<Response> options();

    @Override
    <R> Mono<R> options(Class<R> responseType);

    @Override
    <R> Mono<R> options(GenericType<R> responseType);

    @Override
    Mono<Response> post(Entity<?> entity);

    @Override
    <R> Mono<R> post(Entity<?> entity, Class<R> responseType);

    @Override
    <R> Mono<R> post(Entity<?> entity, GenericType<R> responseType);

    @Override
    Mono<Response> put(Entity<?> entity);

    @Override
    <R> Mono<R> put(Entity<?> entity, Class<R> responseType);

    @Override
    <R> Mono<R> put(Entity<?> entity, GenericType<R> responseType);

    @Override
    Mono<Response> trace();

    @Override
    <R> Mono<R> trace(Class<R> responseType);

    @Override
    <R> Mono<R> trace(GenericType<R> responseType);
}
