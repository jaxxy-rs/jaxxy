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

package org.jaxxy.util.reflect;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionExceptionTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    void testConstructor() {
        final ReflectiveOperationException cause = new ReflectiveOperationException("Oops!");
        final ReflectionException e = new ReflectionException(cause, "Hello, %s!", "Jaxxy");
        assertThat(e.getMessage()).isEqualTo("Hello, Jaxxy!");
        assertThat(e.getCause()).isEqualTo(cause);
    }
}