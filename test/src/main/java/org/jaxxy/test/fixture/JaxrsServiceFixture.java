package org.jaxxy.test.fixture;

import javax.ws.rs.client.WebTarget;

public interface JaxrsServiceFixture<T> extends AutoCloseable {

    T createClientProxy();

    WebTarget createWebTarget();

    String baseUrl();
}
