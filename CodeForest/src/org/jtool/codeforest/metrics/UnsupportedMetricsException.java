/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

/**
 * An object encapsulating a exception with respect to metric measurements.
 * @author Katsuhisa Maruyama
 */
public class UnsupportedMetricsException extends Exception {
    
    private static final long serialVersionUID = -1035705745652052966L;
    
    /**
     * Creates a new exception without its message.
     */
    public UnsupportedMetricsException() {
        super();
    }
    
    /**
     * Creates a new exception with its message.
     * @param mesg the message about the exception
     */
    public UnsupportedMetricsException(String mesg) {
        super("Unsupported: " + mesg);
    }
    
    /**
     * Returns the message.
     * @return the message
     */
    public String getMessage() {
        return super.getMessage();
    }
}