package org.jaxxy.test.fixture;

import org.apache.cxf.feature.Feature;

public interface JaxrsServiceFixtureFactory {

    JaxrsServiceFixtureFactory withContainerProvider(Object provider);

    JaxrsServiceFixtureFactory withClientProvider(Object provider);

    JaxrsServiceFixtureFactory withContainerFeature(Feature feature);

    JaxrsServiceFixtureFactory withClientFeature(Feature feature);

    <T> JaxrsServiceFixture<T> build(Class<T> serviceInterface, T serviceImplementation);
}
