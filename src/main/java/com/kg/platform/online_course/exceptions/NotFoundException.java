package com.kg.platform.online_course.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
    public NotFoundException() {
    }
}
