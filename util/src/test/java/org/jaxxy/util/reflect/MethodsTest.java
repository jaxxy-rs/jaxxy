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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MethodsTest {


    @Test
    public void shouldGetMethod() throws Exception {
        final Method actual = Methods.getMethod(Reflector.class, "reflect", String.class);
        assertThat(actual).isEqualTo(Reflector.class.getMethod("reflect", String.class));
    }

    @Test
    public void shouldThrowReflectionExceptionIfMethodNotFound() {
        assertThatThrownBy(() -> Methods.getMethod(Reflector.class, "someBogusMethod", String.class))
                .isInstanceOf(ReflectionException.class);
    }

    @Test
    public void shouldInvoke() {
        final Reflector reflector = new Reflector();
        final Method method = Methods.getMethod(Reflector.class, "reflect", String.class);
        String actual = Methods.softenedInvoke(method, reflector, "Hello");
        assertThat(actual).isEqualTo("olleH");
    }

    @Test
    public void shouldThrowReflectionExceptionIfMethodThrowsException() {
        final Reflector reflector = new Reflector();
        final Method method = Methods.getMethod(Reflector.class, "reflect");
        assertThatThrownBy(() -> Methods.softenedInvoke(method, reflector))
                .isInstanceOf(ReflectionException.class);
    }

    @Test
    public void shouldInvokeStatic() {
        final Method method = Methods.getMethod(Reflector.class, "instantiate");
        final Reflector actual = Methods.softenedInvokeStatic(method);
        assertThat(actual).isNotNull();
    }
}