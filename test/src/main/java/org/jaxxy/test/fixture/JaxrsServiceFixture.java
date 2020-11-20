/*
 * Copyright (c) 2020 The Jaxxy Authors.
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

import javax.ws.rs.client.WebTarget;

/**
 * A <code>JaxrsServiceFixture</code> allows for testing JAX-RS services and clients.
 *
 * @param <T> the service interface type
 */
public interface JaxrsServiceFixture<T> extends AutoCloseable {

    /**
     * Creates a type-safe client proxy for the service.
     *
     * @return the proxy
     */
    T createClientProxy();

    /**
     * Creates a {@link WebTarget} which points to the service&apos;s <code>baseUrl</code>.
     *
     * @return the web target
     */
    WebTarget createWebTarget();

    /**
     * Returns the base URL of the service.
     *
     * @return the base URL of the service
     */
    String baseUrl();
}
