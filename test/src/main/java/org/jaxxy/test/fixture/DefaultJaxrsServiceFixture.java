package org.jaxxy.test.fixture;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configurable;

import lombok.RequiredArgsConstructor;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import static org.jaxxy.test.fixture.DefaultJaxrsServiceFixtureFactory.SPLIT_HEADERS_PROP;

@RequiredArgsConstructor
class DefaultJaxrsServiceFixture<T> implements JaxrsServiceFixture<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<T> serviceInterface;
    private final Server server;
    private final List<Object> clientProviders;
    private final List<Feature> clientFeatures;

//----------------------------------------------------------------------------------------------------------------------
// AutoCloseable Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void close() {
        server.destroy();
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceFixture Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String baseUrl() {
        return server.getEndpoint().getEndpointInfo().getAddress();
    }

    @Override
    public T createClientProxy() {
        try {
            return configure(RestClientBuilder.newBuilder())
                    .baseUrl(new URL(baseUrl()))
                    .build(serviceInterface);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad base URL.", e);
        }
    }

    @Override
    public WebTarget createWebTarget() {
        return configure(ClientBuilder.newBuilder())
                .build()
                .target(baseUrl());
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <C extends Configurable<C>> C configure(C configurable) {
        clientFeatures.forEach(configurable::register);
        clientProviders.forEach(configurable::register);
        return configurable
                .property(AsyncHTTPConduit.USE_ASYNC, Boolean.TRUE)
                .property(SPLIT_HEADERS_PROP, true);
    }
}
