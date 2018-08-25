package org.jaxxy.rs.util.exception;

public abstract class FormattedException extends RuntimeException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public FormattedException(String message, Object... params) {
        super(String.format(message, params));
    }

    public FormattedException(Throwable cause, String message, Object... params) {
        super(String.format(message, params), cause);
    }
}
