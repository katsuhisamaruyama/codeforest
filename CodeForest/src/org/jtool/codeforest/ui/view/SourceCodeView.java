/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.CommonMetrics;
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
    
    private Font font12;
    
    private JavaSourceViewer sourceViewer;
    
    private SimpleJavaSourceViewerConfiguration sourceViewerConf;
    
    private String currentPath = "";
    
    public SourceCodeView(Composite parent) {
        createPane(parent);
    }
    
    public void dispose() {
        font12 = null;
        sourceViewerConf = null;
        currentPath = null;
    }
    
    private void createPane(Composite parent) {
        parent.setLayout(new FillLayout());
        
        font12 = new Font(parent.getDisplay(), "Courier", 12, SWT.NONE);
        
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
        styledText.setFont(font12);
        
        parent.pack();
    }
    
    public void setMetrics(CommonMetrics metrics) {
        if (!(metrics instanceof ClassMetrics)) {
            return;
        }
        
        ClassMetrics mclass = (ClassMetrics)metrics;
        setSourceCode(mclass.getJavaClass());
    }
    
    private void setSourceCode(final JavaClass jclass) {
        if (sourceViewer == null) {
            return;
        }
            
        final Control control = sourceViewer.getControl();
        if (control != null && !(control.isDisposed())) {
            control.getDisplay().syncExec(new Runnable() {
                
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
}
