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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TypesTest {
    @Test
    public void shouldInstantiate() {
        final Instantiable obj = Types.instantiate(Instantiable.class);
        assertThat(obj).isNotNull();
    }

    @Test
    public void shouldThrowReflectionExceptionWhenNotInstantiable() {
        assertThatThrownBy(() -> Types.instantiate(NotInstantiable.class))
                .isInstanceOf(ReflectionException.class)
                .hasMessage("Unable to instantiate object of type " + NotInstantiable.class.getCanonicalName() + ".");

    }

    @Test
    public void sholdRetrieveTypeParam() {
        final Class<?> typeParam = Types.typeParamFromClass(ConcreteType.class, GenericSuperType.class, 0);
        assertThat(typeParam).isEqualTo(String.class);
    }

    public interface GenericSuperType<T> {
        T someMethod();
    }

    public class ConcreteType implements GenericSuperType<String> {
        @Override
        public String someMethod() {
            return null;
        }
    }

    public static class NotInstantiable {
        private NotInstantiable() {

        }
    }
    public static class Instantiable {

    }
}