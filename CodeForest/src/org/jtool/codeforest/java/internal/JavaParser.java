/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Message;

/**
 * A Java language parser for creating abstract syntax trees (ASTs).
 * @author Katsuhisa Maruyama
 */
public class JavaParser {
	
    /**
     * A Java language parser embedded in Eclipse.
     */
    private ASTParser parser;
    
    public JavaParser() {
        parser = ASTParser.newParser(AST.JLS4);
    }
    
    /**
     * Parsers the contents of a Java file and creates its AST.
     * @param icu the compilation unit that requires parsing
     * @return the root node of the created AST
     */
    public CompilationUnit parse(ICompilationUnit icu) {
        parser.setResolveBindings(true);
        parser.setStatementsRecovery(true);
        parser.setBindingsRecovery(true);
        parser.setSource(icu);
        
        CompilationUnit cu = (CompilationUnit)parser.createAST(null);
        // cu.recordModifications();
        malformedCheck(cu, icu.getPath().toString());
        
        return cu;
    }
    
    /**
     * Parsers the contents of a Java file and creates its AST.
     * @param classpaths the given classpath entries to be used to resolve bindings
     * @param sourcepaths the given sourcepath entries to be used to resolve bindings
     * @param encoding the encoding of the corresponding sourcepath entries or null if the platform encoding can be used
     * @param contents the contents of the Java file 
     * @param name the name of the Java file, representing the created AST
     * @return the root node of the created AST
     */
    public CompilationUnit standaloneParse(String[] classpaths, String[] sourcepaths, String encoding, String contents, String name) {
        parser.setResolveBindings(true);
        parser.setStatementsRecovery(true);
        parser.setBindingsRecovery(true);
        
        String[] encodings;
        if (encoding == null) {
            encodings = new String[]{ "US-ASCII", "UTF-8", "SJIS" };
        } else {
            encodings = new String[]{ "US-ASCII", "UTF-8", "SJIS", encoding };
        }
        parser.setEnvironment(classpaths, sourcepaths, encodings, true);
        parser.setUnitName(name);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(contents.toCharArray());
        
        CompilationUnit cu = (CompilationUnit)parser.createAST(null);
        // cu.recordModifications();
        malformedCheck(cu, name);
        
        return cu;
    }
    
    /**
     * Checks the form of a given compilation unit.
     * @param cu the compilation unit to be checked
     * @param name the name of the compilation unit
     */
    private void malformedCheck(CompilationUnit cu, String name) {
        if (!((cu.getFlags() & ASTNode.MALFORMED) == ASTNode.MALFORMED)) {
            System.err.println("complete parse: " + name);
        } else {
            for (Message msg : cu.getMessages()) {
            	System.err.println("imcomplete parse: " + name + " " + msg.getMessage());
            }
        }
    }
}
