package org.jaxxy.guava;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * An {@link RxInvoker} implementation which supports Guava&apos;s {@link ListenableFuture}.
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
