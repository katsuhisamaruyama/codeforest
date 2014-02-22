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
    
    private long time;
    
    private String className;
    
    private String comments;
    
    public Memo(long time, String className, String comments) {
        this.time = time;
        this.className = className;
        this.comments = comments;
    }
    
    public long getTime() {
        return time;
    }
    
    public String getTimeRepresentation() {
        return Time.toString(time);
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setComments(String contents) {
        this.comments = contents;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void print() {
        System.out.println("Memo = " + getTimeRepresentation() + " " + getComments());
    }
}
