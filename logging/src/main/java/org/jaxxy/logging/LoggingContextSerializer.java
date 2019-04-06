package org.jaxxy.logging;

import java.util.function.Function;

@FunctionalInterface
public interface LoggingContextSerializer extends Function<Object,String> {

}
