package org.jaxxy.rx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;

import static java.util.Optional.ofNullable;

@Provider
public class FutureInvokerProvider implements RxInvokerProvider<FutureInvoker> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ExecutorService defaultExecutorService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new {@link FutureInvokerProvider} which calls {@link Executors#newCachedThreadPool()}
     * to create its default {@link ExecutorService}.
     */
    public FutureInvokerProvider() {
        this(Executors.newCachedThreadPool());
    }

    /**
     * Creates a new {@link FutureInvokerProvider} which uses the provided default {@link ExecutorService}.
     * @param defaultExecutorService
     */
    public FutureInvokerProvider(ExecutorService defaultExecutorService) {
        this.defaultExecutorService = defaultExecutorService;
    }

//----------------------------------------------------------------------------------------------------------------------
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public FutureInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new Invoker(syncInvoker, ofNullable(executorService).orElse(defaultExecutorService));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return FutureInvoker.class.equals(clazz);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @RequiredArgsConstructor
    @SuppressWarnings("unchecked")
    private static class Invoker extends AbstractRxInvoker<Future> implements FutureInvoker {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final SyncInvoker syncInvoker;
        private final ExecutorService executorService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        @Override
        protected <R> Future async(Function<SyncInvoker, R> fn) {
            return executorService.submit(() -> fn.apply(syncInvoker));
        }
    }
}
