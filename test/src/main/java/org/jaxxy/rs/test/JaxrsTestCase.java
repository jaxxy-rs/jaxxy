package org.jaxxy.rs.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.http.asyncclient.AsyncHTTPConduit;
import org.jaxxy.rs.util.reflect.Types;
import org.junit.After;
import org.junit.Before;

@Slf4j
public abstract class JaxrsTestCase<I> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final int DEFAULT_PORT = 9999;
    private Server server;
    private String address;

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract I createServiceObject();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public I clientProxy() {
        DefaultJaxrsClientConfig config = new DefaultJaxrsClientConfig();
        configureClient(config);

        final Class<I> serviceInterface = serviceInterface();

        JAXRSClientFactoryBean factory = new JAXRSClientFactoryBean();
        factory.setProviders(config.getProviders());
        factory.setResourceClass(serviceInterface);
        factory.setAddress(address);
        return factory.create(serviceInterface);
    }

    protected void configureClient(JaxrsClientConfig config) {

    }

    protected void configureServer(JaxrsServerConfig config) {

    }

    protected int createPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            log.error("Unable to allocate port, defaulting to {}.", DEFAULT_PORT, e);
            return DEFAULT_PORT;
        }
    }

    protected Class<I> serviceInterface() {
        return Types.typeParamFromClass(getClass(), JaxrsTestCase.class, 0);
    }

    @Before
    public void startServer() {
        this.address = String.format("http://localhost:%d", createPort());
        DefaultJaxrsServerConfig config = new DefaultJaxrsServerConfig();
        configureServer(config);

        final Class<I> serviceInterface = serviceInterface();

        final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(BusFactory.getDefaultBus(true));
        factory.setResourceClasses(serviceInterface);
        factory.setResourceProvider(serviceInterface, new SingletonResourceProvider(createServiceObject(), true));
        factory.setServiceBean(createServiceObject());
        factory.setAddress(address);
        factory.setProviders(config.getProviders());
        factory.setFeatures(Collections.singletonList(new LoggingFeature()));
        this.server = factory.create();
    }

    @After
    public void stopServer() {
        server.stop();
        server.destroy();
    }

    public WebTarget webTarget() {
        return ClientBuilder.newClient()
                .target(address)
                .property(AsyncHTTPConduit.USE_ASYNC, Boolean.TRUE);
    }
}
