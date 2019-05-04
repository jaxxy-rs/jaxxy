package org.jaxxy.logging.mdc;

@FunctionalInterface
public interface MdcValueEncoder {
    String encode(Object value);
}
