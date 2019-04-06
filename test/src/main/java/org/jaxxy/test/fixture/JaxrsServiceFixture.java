package org.jaxxy.test.fixture;

import javax.ws.rs.client.WebTarget;

/**
 * A <code>JaxrsServiceFixture</code> allows for testing JAX-RS services and clients.
 *
 * @param <T> the service interface type
 */
public interface JaxrsServiceFixture<T> extends AutoCloseable {

    /**
     * Creates a type-safe client proxy for the service.
     *
     * @return the proxy
     */
    T createClientProxy();

    /**
     * Creates a {@link WebTarget} which points to the service&apos;s <code>baseUrl</code>.
     *
     * @return the web target
     */
    WebTarget createWebTarget();

    /**
     * Returns the base URL of the service.
     *
     * @return the base URL of the service
     */
    String baseUrl();
}
