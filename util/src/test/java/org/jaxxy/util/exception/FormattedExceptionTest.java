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

package org.jaxxy.util.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FormattedExceptionTest {
    @Test
    public void testConstructor() {
        final MyException e = new MyException("Hello %s %d", "foo", 12);
        assertThat(e.getMessage()).isEqualTo("Hello foo 12");
    }

    @Test
    public void testConstructorWithCause() {
        final RuntimeException cause = new RuntimeException("Oops!");
        final MyException e = new MyException(cause, "Hello %s %d", "foo", 12);
        assertThat(e.getMessage()).isEqualTo("Hello foo 12");
        assertThat(e.getCause()).isSameAs(cause);
    }

    public static class MyException extends FormattedException {
        public MyException(String message, Object... params) {
            super(message, params);
        }

        public MyException(Throwable cause, String message, Object... params) {
            super(cause, message, params);
        }
    }
}