/*
 * Copyright (c) 2018 The Jaxxy Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jaxxy.test;

import java.util.Collections;

import javax.ws.rs.client.Client;
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
import org.jaxxy.util.reflect.Types;
import org.junit.After;
import org.junit.Before;

@Slf4j
public abstract class JaxrsTestCase<I> {
    //----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final int DEFAULT_PORT = 9999;
    public static final String SPLIT_HEADERS_PROP = "org.apache.cxf.http.header.split";
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
        DefaultJaxrsClientConfig config = createClientConfig();

        final Class<I> serviceInterface = serviceInterface();

        JAXRSClientFactoryBean factory = new JAXRSClientFactoryBean();
        factory.setProviders(config.getProviders());
        factory.setResourceClass(serviceInterface);
        factory.setAddress(address);
        return factory.create(serviceInterface);
    }

    private DefaultJaxrsClientConfig createClientConfig() {
        DefaultJaxrsClientConfig config = new DefaultJaxrsClientConfig();
        configureClient(config);
        return config;
    }

    protected void configureClient(JaxrsClientConfig config) {

    }

    protected void configureServer(JaxrsServerConfig config) {

    }

    protected int createPort() {
        return DEFAULT_PORT;
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
        factory.getProperties(true).put(SPLIT_HEADERS_PROP, true);
        this.server = factory.create();
    }

    @After
    public void stopServer() {
        server.stop();
        server.destroy();
    }

    public WebTarget webTarget() {
        final Client client = ClientBuilder.newClient();
        client.property(SPLIT_HEADERS_PROP, true);
        createClientConfig().getProviders().forEach(client::register);
        return client
                .target(address)
                .property(AsyncHTTPConduit.USE_ASYNC, Boolean.TRUE);
    }
}
