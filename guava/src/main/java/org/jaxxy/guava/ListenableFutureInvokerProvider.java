package org.jaxxy.guava;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.ext.Provider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.RequiredArgsConstructor;
import org.jaxxy.rx.AbstractRxInvoker;

import static java.util.Optional.ofNullable;

@Provider
@RequiredArgsConstructor
public class ListenableFutureInvokerProvider implements RxInvokerProvider<ListenableFutureInvoker> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ListeningExecutorService defaultExecutorService;

//----------------------------------------------------------------------------------------------------------------------
// RxInvokerProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ListenableFutureInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
        return new Invoker(syncInvoker, ofNullable(executorService).map(MoreExecutors::listeningDecorator).orElse(defaultExecutorService));
    }

    @Override
    public boolean isProviderFor(Class<?> clazz) {
        return ListenableFutureInvoker.class.equals(clazz);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    @RequiredArgsConstructor
    private static class Invoker extends AbstractRxInvoker<ListenableFuture> implements ListenableFutureInvoker {

        private final SyncInvoker syncInvoker;
        private final ListeningExecutorService executorService;
        
        @Override
        protected <R> ListenableFuture async(Function<SyncInvoker, R> fn) {
            return executorService.submit(() -> fn.apply(syncInvoker));
        }
    }
}
