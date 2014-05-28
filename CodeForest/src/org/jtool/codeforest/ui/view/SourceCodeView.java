/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.eclipse.model.java.JavaClass;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.internal.ui.text.SimpleJavaSourceViewerConfiguration;

/**
 * Provides a source code view.
 * @author Katsuhisa Maruyama
 */
@SuppressWarnings("restriction")
public class SourceCodeView {
    
    /**
     * Information on the font.
     */
    private Font font11;
    
    /**
     * A viewer displaying source code.
     */
    private JavaSourceViewer sourceViewer;
    
    /**
     * The configuration of the source code viewer.
     */
    private SimpleJavaSourceViewerConfiguration sourceViewerConf;
    
    /**
     * The current path.
     */
    private String currentPath = "";
    
    /**
     * Creates a source code view.
     * @param parent the parent of the source code view
     */
    public SourceCodeView(Composite parent) {
        createPane(parent);
    }
    
    /**
     * Creates the pane of this source code view.
     * @param parent the parent of the source code view
     */
    private void createPane(Composite parent) {
        parent.setLayout(new FillLayout());
        
        IDocument document = new Document();
        JavaTextTools tools = JavaPlugin.getDefault().getJavaTextTools();
        tools.setupJavaDocumentPartitioner(document, IJavaPartitions.JAVA_PARTITIONING);
        IPreferenceStore store = JavaPlugin.getDefault().getPreferenceStore();
        sourceViewerConf = new SimpleJavaSourceViewerConfiguration(tools.getColorManager(),
                store, null, IJavaPartitions.JAVA_PARTITIONING, true);
        
        sourceViewer = new JavaSourceViewer(parent, null, null, false,
                SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL, store);
        sourceViewer.setEditable(false);
        sourceViewer.configure(sourceViewerConf);
        sourceViewer.setDocument(document);
        
        StyledText styledText = sourceViewer.getTextWidget();
        if (Platform.getOS().compareTo(Platform.OS_MACOSX) == 0) {
            font11 = new Font(parent.getDisplay(), "Monaco", 11, SWT.NONE);
        } else {
            font11 = new Font(parent.getDisplay(), "Courier New", 11, SWT.NONE);
        }
        styledText.setFont(font11);
        
        parent.pack();
    }
    
    /**
     * Displays source code of a selected class.
     * @param jclass the selected class
     */
    public void changeSelection(final JavaClass jclass) {
        if (sourceViewer == null) {
            return;
        }
            
        final Control control = sourceViewer.getControl();
        if (control != null && !(control.isDisposed())) {
            control.getDisplay().syncExec(new Runnable() {
                
                /**
                 * Runs a new thread.
                 */
                public void run() {
                    try {
                        String path = jclass.getJavaFile().getPath();
                        if (path.compareTo(currentPath) != 0) {
                            String source = jclass.getSource();
                            sourceViewer.getTextWidget().setText(source);
                            currentPath = path;
                        }
                        
                        reveal(jclass.getStartPosition());
                        
                    } catch (Exception e) { /* empty */ }
                }
            });
        }
    }
    
    /**
     * Reveals code fragments with a given offset value.
     * @param offset the offset value of the revealed code fragments
     */
    private void reveal(int offset) {
        StyledText styledText = sourceViewer.getTextWidget();
        int line = styledText.getLineAtOffset(offset);
        
        int bottom = styledText.getLineCount() - 1;
        if (line + 10 < bottom) {
            offset = styledText.getOffsetAtLine(line + 10);
        } else {
            offset = styledText.getOffsetAtLine(bottom);
        }
        
        styledText.setSelection(offset);
        
        sourceViewer.getTextWidget().setLineBackground(line, 1, new Color(null, 200, 220, 255));
    }
    
    /**
     * Disposes this source code view.
     */
    public void dispose() {
        font11.dispose();
        
        sourceViewer = null;
        sourceViewerConf = null;
        currentPath = null;
    }
}
