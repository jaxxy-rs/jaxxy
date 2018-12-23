package org.jaxxy.rx;

import java.util.function.Function;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.core.GenericType;

public abstract class AbstractRxInvoker<T> implements RxInvoker<T> {
//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract <R> T async(Function<SyncInvoker, R> fn);

//----------------------------------------------------------------------------------------------------------------------
// RxInvoker Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public T delete() {
        return async(SyncInvoker::delete);
    }

    @Override
    public <R> T delete(Class<R> responseType) {
        return async(s -> s.delete(responseType));
    }

    @Override
    public <R> T delete(GenericType<R> responseType) {
        return async(s -> s.delete(responseType));
    }

    @Override
    public T get() {
        return async(SyncInvoker::get);
    }

    @Override
    public <R> T get(Class<R> responseType) {
        return async(s -> s.get(responseType));
    }

    @Override
    public <R> T get(GenericType<R> responseType) {
        return async(s -> s.get(responseType));
    }

    @Override
    public T head() {
        return async(SyncInvoker::head);
    }

    @Override
    public T method(String name) {
        return async(s -> s.method(name));
    }

    @Override
    public <R> T method(String name, Class<R> responseType) {
        return async(s -> s.method(name, responseType));
    }

    @Override
    public <R> T method(String name, GenericType<R> responseType) {
        return async(s -> s.method(name, responseType));
    }

    @Override
    public T method(String name, Entity<?> entity) {
        return async(s -> s.method(name, entity));
    }

    @Override
    public <R> T method(String name, Entity<?> entity, Class<R> responseType) {
        return async(s -> s.method(name, entity, responseType));
    }

    @Override
    public <R> T method(String name, Entity<?> entity, GenericType<R> responseType) {
        return async(s -> s.method(name, entity, responseType));
    }

    @Override
    public T options() {
        return async(SyncInvoker::options);
    }

    @Override
    public <R> T options(Class<R> responseType) {
        return async(s -> s.options(responseType));
    }

    @Override
    public <R> T options(GenericType<R> responseType) {
        return async(s -> s.options(responseType));
    }

    @Override
    public T post(Entity<?> entity) {
        return async(s -> s.post(entity));
    }

    @Override
    public <R> T post(Entity<?> entity, Class<R> responseType) {
        return async(s -> s.post(entity, responseType));
    }

    @Override
    public <R> T post(Entity<?> entity, GenericType<R> responseType) {
        return async(s -> s.post(entity, responseType));
    }

    @Override
    public T put(Entity<?> entity) {
        return async(s -> s.put(entity));
    }

    @Override
    public <R> T put(Entity<?> entity, Class<R> responseType) {
        return async(s -> s.put(entity, responseType));
    }

    @Override
    public <R> T put(Entity<?> entity, GenericType<R> responseType) {
        return async(s -> s.put(entity, responseType));
    }

    @Override
    public T trace() {
        return async(SyncInvoker::trace);
    }

    @Override
    public <R> T trace(Class<R> responseType) {
        return async(s -> s.trace(responseType));
    }

    @Override
    public <R> T trace(GenericType<R> responseType) {
        return async(s -> s.trace(responseType));
    }
}
