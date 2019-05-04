package org.jaxxy.logging.mdc;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;

@RequiredArgsConstructor
public class DefaultRichMdc implements RichMdc {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final MdcValueEncoder encoder;

//----------------------------------------------------------------------------------------------------------------------
// RichMdc Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> void put(String key, T value) {
        if (value != null) {
            MDC.put(key, encoder.encode(value));
        } else {
            MDC.remove(key);
        }
    }
}
