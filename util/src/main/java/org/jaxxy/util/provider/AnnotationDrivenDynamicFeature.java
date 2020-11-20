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

package org.jaxxy.util.provider;

import java.lang.annotation.Annotation;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.jaxxy.util.reflect.Types;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.reflect.MethodUtils.getAnnotation;

public abstract class AnnotationDrivenDynamicFeature<A extends Annotation> implements DynamicFeature {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<A> annotationType;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected AnnotationDrivenDynamicFeature() {
        this.annotationType = Types.typeParamFromClass(getClass(), AnnotationDrivenDynamicFeature.class, 0);
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract void configure(ResourceInfo resourceInfo, FeatureContext context, A annotation);

//----------------------------------------------------------------------------------------------------------------------
// DynamicFeature Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        ofNullable(getAnnotation(resourceInfo.getResourceMethod(), annotationType, true, false))
                .ifPresent(annotation -> configure(resourceInfo, context, annotation));
    }
}
