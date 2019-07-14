package org.springdoc.sample2;

public class ApiException extends Exception{

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")

	private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}
