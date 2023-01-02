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

package org.jaxxy.security.role;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.jaxxy.util.provider.AnnotationDrivenDynamicFeature;

import java.util.Arrays;
import java.util.HashSet;

@Provider
public class RolesAllowedDynamicFeature extends AnnotationDrivenDynamicFeature<RolesAllowed> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configure(ResourceInfo resourceInfo, FeatureContext context, RolesAllowed annotation) {
        context.register(new RolesAllowedFilter(new HashSet<>(Arrays.asList(annotation.value()))));
    }
}
