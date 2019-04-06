package org.jaxxy.test.fixture;

public class JaxrsServiceFixtures {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates an empty {@link JaxrsServiceFixtureFactory}.
     *
     * @return an empty {@link JaxrsServiceFixtureFactory}
     */
    public static JaxrsServiceFixtureFactory createFactory() {
        return DefaultJaxrsServiceFixtureFactory.builder().build();
    }
}
