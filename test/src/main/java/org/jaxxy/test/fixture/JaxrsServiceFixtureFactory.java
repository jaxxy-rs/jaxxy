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
