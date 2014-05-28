/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.util.Time;

/**
 * Stores the contents of a memo.
 * @author Katsuhisa Maruyama
 */
public class Memo {
    
    /**
     * The time when the memo was written.
     */
    private long time;
    
    /**
     * The name of a class which the memo was attached to.
     */
    private String className;
    
    /**
     * The comments written by a user.
     */
    private String comments;
    
    /**
     * Creates a memo.
     * @param time the time when the memo was written.
     * @param className the name of a class which the memo was attached to
     * @param comments the comments written by a user
     */
    public Memo(long time, String className, String comments) {
        this.time = time;
        this.className = className;
        this.comments = comments;
    }
    
    /**
     * Returns the time when the memo was written.
     * @return the time
     */
    public long getTime() {
        return time;
    }
    
    /**
     * Returns the time string when the memo was written.
     * @return the time string in the usual form
     */
    public String getTimeRepresentation() {
        return Time.toString(time);
    }
    
    /**
     * Returns the name of a class which the memo was attached to.
     * @return the class name
     */
    public String getClassName() {
        return className;
    }
    
    /**
     * Sets the comments written by a user.
     * @param the comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /**
     * Returns the comments written by a user.
     * @return the comments
     */
    public String getComments() {
        return comments;
    }
    
    /**
     * Displays information on this memo.
     */
    public void print() {
        System.out.println("Memo = " + getTimeRepresentation() + " " + getComments());
    }
}
