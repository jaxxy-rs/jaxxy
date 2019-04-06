package org.jaxxy.test.fixture;

import org.apache.cxf.feature.Feature;

public interface JaxrsServiceFixtureFactory {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Builds a {@link JaxrsServiceFixture} for the specified service interface and implementation object.
     *
     * @param serviceInterface      the service interface
     * @param serviceImplementation the service implementation object
     * @param <T>                   the service interface type
     * @return the fixture
     */
    <T> JaxrsServiceFixture<T> build(Class<T> serviceInterface, T serviceImplementation);

    /**
     * Returns a new {@link JaxrsServiceFixtureFactory} with the container {@link Feature} added.
     *
     * @param feature the new feature to be added
     * @return a new factory
     */
    JaxrsServiceFixtureFactory withClientFeature(Feature feature);

    /**
     * Returns a new {@link JaxrsServiceFixtureFactory} with the client provider added.
     *
     * @param provider the new provider to be added
     * @return a new factory
     */
    JaxrsServiceFixtureFactory withClientProvider(Object provider);

    /**
     * Returns a new {@link JaxrsServiceFixtureFactory} with the container feature added.
     *
     * @param feature the new feature to be added
     * @return a new factory
     */
    JaxrsServiceFixtureFactory withContainerFeature(Feature feature);

    /**
     * Returns a new {@link JaxrsServiceFixtureFactory} with the container provider added.
     *
     * @param provider the new provider to be added
     * @return a new factory
     */
    JaxrsServiceFixtureFactory withContainerProvider(Object provider);
}
