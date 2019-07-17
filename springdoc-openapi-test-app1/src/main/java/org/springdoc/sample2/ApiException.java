package org.springdoc.sample2;

public final class ApiException extends Exception {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")

	private final int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
