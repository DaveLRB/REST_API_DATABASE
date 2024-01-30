package com.school.users.exceptions;

public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(){
        super("User ID was not found");
    }
}
