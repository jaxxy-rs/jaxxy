package org.jaxxy.test.fixture;

public class JaxrsServiceFixtures {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static JaxrsServiceFixtureFactory createFactory() {
        return DefaultJaxrsServiceFixtureFactory.builder().build();
    }
}
