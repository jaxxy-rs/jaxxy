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
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public FutureInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new Invoker(syncInvoker, ofNullable(executorService).orElse(Executors.newFixedThreadPool(1)));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return FutureInvoker.class.equals(clazz);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @RequiredArgsConstructor
    private static class Invoker extends AbstractRxInvoker<Future> implements FutureInvoker {

        private final SyncInvoker syncInvoker;
        private final ExecutorService executorService;

        @Override
        protected <R> Future async(Function<SyncInvoker, R> fn) {
            return executorService.submit(() -> fn.apply(syncInvoker));
        }
    }
}
