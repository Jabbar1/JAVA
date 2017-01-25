package com.shaik.exception;

/**
 * Created by jabbars on 1/24/2017.
 */
public class BaseException extends RuntimeException{

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }
}
