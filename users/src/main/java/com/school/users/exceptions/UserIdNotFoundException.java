package com.school.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(String s){
        super(s);
    }
}
