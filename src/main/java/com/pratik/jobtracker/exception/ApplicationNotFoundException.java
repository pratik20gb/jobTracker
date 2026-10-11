package com.pratik.jobtracker.exception;

public class ApplicationNotFoundException extends RuntimeException{
    public ApplicationNotFoundException(long id){
        super("Application not found with id: "+ id);
    }
}
