package com.example.dswii_t3_diaz_cesar.exception;

public class MaxUploadSizeExceedException extends RuntimeException {
    public MaxUploadSizeExceedException (String message){
        super(message);
    }
}