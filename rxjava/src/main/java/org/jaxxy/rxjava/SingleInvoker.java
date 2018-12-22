package org.jaxxy.rxjava;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import io.reactivex.Single;

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
