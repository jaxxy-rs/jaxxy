package org.jaxxy.rs.util.reflect;

import org.jaxxy.rs.util.exception.FormattedException;

public class ReflectionException extends FormattedException {
    public ReflectionException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }
}
