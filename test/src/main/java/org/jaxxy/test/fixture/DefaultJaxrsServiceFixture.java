package org.jaxxy.test.fixture;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import lombok.RequiredArgsConstructor;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;

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
    public void close() throws Exception {
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
        final JAXRSClientFactoryBean factory = new JAXRSClientFactoryBean();
        factory.setProviders(clientProviders);
        factory.setResourceClass(serviceInterface);
        factory.setAddress(baseUrl());
        return factory.create(serviceInterface);
    }

    @Override
    public WebTarget createWebTarget() {
        final ClientBuilder builder = ClientBuilder.newBuilder();
        builder.property(SPLIT_HEADERS_PROP, true);
        clientProviders.forEach(builder::register);
        clientFeatures.forEach(builder::register);
        return builder.build()
                .target(baseUrl())
                .property(AsyncHTTPConduit.USE_ASYNC, Boolean.TRUE);
    }
}
