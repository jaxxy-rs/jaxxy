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

package org.jaxxy.io;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;
import org.jaxxy.util.reflect.Types;

@Provider
@Slf4j
public abstract class MessageBodyProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Predicate<Class<?>> supportedTypePredicate;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MessageBodyProvider() {
        this.supportedTypePredicate = Types.predicateWithDefaultBlacklist()
                .whitelist(Types.typeParamFromClass(getClass(), MessageBodyProvider.class, 0))
                .build();
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyReader Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isSupportedType(type);
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyWriter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isSupportedType(type);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Override to customize the types supported by this provider.
     *
     * @param type the type
     * @return whether or not the type is to be supported by this provider
     */
    protected boolean isSupportedType(Class<?> type) {
        return supportedTypePredicate.test(type);
    }
}
