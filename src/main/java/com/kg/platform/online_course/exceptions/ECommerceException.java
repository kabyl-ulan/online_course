package com.kg.platform.online_course.exceptions;

public class ECommerceException extends RuntimeException{
    public ECommerceException(String msg, Exception exception) {
        super(msg,exception);
    }
    public ECommerceException(String msg) {
        super(msg);
    }
}
