package org.lds.mediafinder.utils;

/**
 * Exception for testing errors.
 * @author Allen Sudweeks
 */
public class TestException extends Exception {
    
    private String message;
    
    public TestException(String message) {
        this.message = message;        
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
