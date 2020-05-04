package com.nure.exceptions;

import java.util.function.Supplier;

public class NoUserException extends IllegalArgumentException {
    public NoUserException(String login){
        super("No user with login "+login+" is detected");
    }

}
