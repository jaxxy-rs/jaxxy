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

package org.jaxxy.util.reflect;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;

public class Methods {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static Method getMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        try {
            return type.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e, "Unable to find method %s on class %s.", name, type.getCanonicalName());
        }
    }

    /**
     * Invokes a method reflectively, but "softens" all exceptions, wrapping them with a {@link ReflectionException}
     * (even any exceptions thrown by the method itself).
     *
     * @param method     the method to invoke
     * @param target     the target object on which to invoke the method
     * @param parameters the method parameters
     * @param <T>        the type to return
     * @return the result of the method invocation
     * @throws ReflectionException if any {@link ReflectiveOperationException} happens.
     */
    @SuppressWarnings("unchecked")
    public static <T> T softenedInvoke(Method method, Object target, Object... parameters) {
        try {
            return (T) method.invoke(target, parameters);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e, "Unable ot softenedInvoke method %s on object of type %s.", method.getName(), ofNullable(target)
                    .map(Object::getClass)
                    .map(Class::getCanonicalName)
                    .orElse("null"));
        }
    }

    /**
     * Invokes a static method reflectively, but "softens" all exceptions, wrapping them with a
     * {@link ReflectionException} (even any exceptions thrown by the method itself).
     *
     * @param method the method to invoke
     * @param params the method parameters
     * @param <T>    the type to return
     * @return the result of the method invocation
     */
    public static <T> T softenedInvokeStatic(Method method, Object... params) {
        return softenedInvoke(method, null, params);
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private Methods() {
        // Preventing instantiation.
    }
}
