package org.jaxxy.reactor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import reactor.core.publisher.Mono;

/**
 * An {@link RxInvoker} which supports Project Reactor&apos;s {@link Mono}.
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
