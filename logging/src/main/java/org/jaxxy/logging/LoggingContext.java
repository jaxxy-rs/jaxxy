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

package org.jaxxy.logging;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;

public interface LoggingContext {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Puts a value into the logging context.
     *
     * @param key   the key
     * @param value the value
     */
    void put(String key, Object value);

    /**
     * Returns the {@link ContainerRequestContext} for the current getRequestContext.
     * @return the getRequestContext context
     */
    ContainerRequestContext getRequestContext();

    /**
     * Returns the {@link ResourceInfo} for the current getRequestContext.
     * @return the resource info
     */
    ResourceInfo getResourceInfo();
}
