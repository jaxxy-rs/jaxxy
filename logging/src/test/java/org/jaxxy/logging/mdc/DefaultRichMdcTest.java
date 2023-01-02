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

package org.jaxxy.logging.mdc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultRichMdcTest {

    @Mock
    private MdcValueEncoder encoder;

    @Test
    void verifyPutNull() {
        MDC.put("hello", "world");

        final DefaultRichMdc mdc = new DefaultRichMdc(encoder);
        mdc.put("hello", null);
        assertThat(MDC.get("hello")).isNull();

        verifyNoMoreInteractions(encoder);
    }

    @Test
    void verifyPutNonNull() {
        when(encoder.encode("world")).thenReturn("WURLD");
        MDC.remove("hello");
        final DefaultRichMdc mdc = new DefaultRichMdc(encoder);
        mdc.put("hello", "world");
        assertThat(MDC.get("hello")).isEqualTo("WURLD");
        verify(encoder).encode("world");
        verifyNoMoreInteractions(encoder);

    }

}