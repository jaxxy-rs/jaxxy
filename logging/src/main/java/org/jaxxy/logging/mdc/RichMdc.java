package org.jaxxy.logging.mdc;

public interface RichMdc {
    <T> void put(String key, T value);
}
