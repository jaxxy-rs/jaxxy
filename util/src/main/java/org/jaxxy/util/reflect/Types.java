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

package org.jaxxy.util.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.apache.commons.lang3.reflect.TypeUtils;

public class Types {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static <T> T instantiate(Class<T> type) {
        try {
            return type.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e, "Unable to instantiate object of type %s.", type.getCanonicalName());
        }
    }

    public static <T extends P, P, C extends Type> C typeParamFromClass(Class<T> concreteClass, Class<P> definingClass, int varIndex) {
        return typeParamFromType(concreteClass, definingClass, varIndex);
    }

    @SuppressWarnings("unchecked")
    public static <P, C extends Type> C typeParamFromType(Type type, Class<P> definingClass, int varIndex) {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(type, definingClass);
        TypeVariable<Class<P>> typeVar = definingClass.getTypeParameters()[varIndex];
        return (C) typeArguments.get(typeVar);
    }
}
