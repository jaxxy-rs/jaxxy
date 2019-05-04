package org.jaxxy.logging.mdc;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import lombok.RequiredArgsConstructor;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
@RequiredArgsConstructor
public class MdcHeadersFilter implements ContainerRequestFilter {

    private final RichMdc mdc;

    @Override
    public void filter(ContainerRequestContext request) {
        mdc.put("headers", request.getHeaders());
    }
}
