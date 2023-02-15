/*
 * Copyright (c) 2018-2023 The Jaxxy Authors.
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

package org.jaxxy.test.fixture;

import lombok.Builder;
import lombok.Value;
import org.apache.cxf.BusFactory;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.testutil.common.TestUtil;

import java.util.ArrayList;
import java.util.List;


@Builder(toBuilder = true)
@Value
class DefaultJaxrsServiceFixtureFactory implements JaxrsServiceFixtureFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String SPLIT_HEADERS_PROP = "org.apache.cxf.http.header.split";
    @Builder.Default
    List<Object> containerProviders = List.of();
    @Builder.Default
    List<Object> clientProviders = List.of();
    @Builder.Default
    List<Feature> containerFeatures = List.of();
    @Builder.Default
    List<Feature> clientFeatures = List.of();

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    private static <T> List<T> extend(List<T> original, T object) {
        final List<T> extended = new ArrayList<>(original);
        extended.add(object);
        return extended;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceFixtureFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> JaxrsServiceFixture<T> build(Class<T> serviceInterface, T serviceImplementation) {
        final String address = String.format("http://localhost:%s/", TestUtil.getNewPortNumber(serviceInterface));
        final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(BusFactory.getDefaultBus(true));
        factory.setResourceClasses(serviceInterface);
        factory.setResourceProvider(serviceInterface, new SingletonResourceProvider(serviceImplementation, true));
        factory.setServiceBean(serviceImplementation);
        factory.setFeatures(containerFeatures);
        factory.setAddress(address);
        factory.setProviders(containerProviders);
        factory.getProperties(true).put(SPLIT_HEADERS_PROP, true);
        return new DefaultJaxrsServiceFixture<>(serviceInterface, factory.create(), clientProviders, clientFeatures);
    }

    @Override
    public JaxrsServiceFixtureFactory withClientFeature(Feature feature) {
        return toBuilder()
                .clientFeatures(extend(clientFeatures, feature))
                .build();
    }

    @Override
    public JaxrsServiceFixtureFactory withClientProvider(Object provider) {
        return toBuilder()
                .clientProviders(extend(clientProviders, provider))
                .build();
    }

    @Override
    public JaxrsServiceFixtureFactory withContainerFeature(Feature feature) {
        return toBuilder()
                .containerFeatures(extend(containerFeatures, feature))
                .build();
    }

    @Override
    public JaxrsServiceFixtureFactory withContainerProvider(Object provider) {
        return toBuilder()
                .containerProviders(extend(containerProviders, provider))
                .build();
    }
}
