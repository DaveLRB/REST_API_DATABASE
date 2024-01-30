package com.school.users.exceptions;

public class UserListNotFoundException extends RuntimeException {
    public UserListNotFoundException(){
        super("User list not found");
    }
}
