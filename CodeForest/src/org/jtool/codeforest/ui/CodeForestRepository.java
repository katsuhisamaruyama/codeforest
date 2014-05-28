/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.view.control.InteractionView;
import org.jtool.codeforest.ui.view.control.InteractionRecord;
import org.jtool.codeforest.ui.view.control.MemoView;
import org.jtool.codeforest.ui.view.control.Memo;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.util.Time;
import org.jtool.codeforest.util.XMLWriter;
import org.jtool.codeforest.util.XMLReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import java.io.File;

/**
 * Stores setting data as a working set of metrics.
 * @author Katsuhisa Maruyama
 */
public class CodeForestRepository {
    
    /**
     * The names of tags appearing in an XML document.
     */
    private static final String TopElem           = "codeForestRepository";
    
    private static final String NameAttr          = "name";
    private static final String DateAttr          = "date";
    private static final String TimeAttr          = "time";
    
    private static final String InteractionElem   = "interactionRecord";
    private static final String DescriptionAttr   = "desc";
    private static final String TypeAttr          = "type";
    private static final String TrunkHeightAttr   = "th";
    private static final String TrunkRadiusAttr   = "tr";
    private static final String TrunkColorAttr    = "tc";
    private static final String FoliageHeightAttr = "fh";
    private static final String FoliageRadiusAttr = "fr";
    private static final String FoliageColorAttr  = "fc";
    
    private static final String MemoElem          = "memo";
    private static final String ClassNameAttr     = "class";
    
    /**
     * The metrics of a project represented by a forest.
     */
    private ProjectMetrics projectMetrics;
    
    /**
     * An interaction view
     */
    private InteractionView interactionView;
    
    /**
     * A memo view.
     */
    private MemoView memoView;
    
    /**
     * Creates a repository that stores information on a forest.
     * @param frame the main frame
     */
    public CodeForestRepository(CodeForestFrame frame) {
        projectMetrics = frame.getProjectMetrics();
        interactionView = frame.getInteractionView();
        memoView = frame.getMemoView();
    }
    
    /**
     * The prefix of the name of an XML file. 
     */
    private static final String XML_FILENAME = "codeforest-repo";
    
    /**
     * The extension of the name of an XML file. 
     */
    private static final String XML_FILENAME_EXT = ".xml";
    
    /**
     * Obtains the name of an XML file.
     * @param mproject the project metrics
     * @return the name
     */
    private String getXMLFileName(ProjectMetrics mproject) {
        String topdir = mproject.getJavaProject().getTopDir();
        String filename = XML_FILENAME + String.valueOf(mproject.getTime()) + XML_FILENAME_EXT;
        return topdir + File.separator + filename;
    }
    
    /**
     * Writes information on a project into an XML document.
     */
    public void writeXML() {
        File file = new File(getXMLFileName(projectMetrics));
        if (file.exists()) {
            file.delete();
        }
        
        Document doc = getDocument(projectMetrics);
        XMLWriter.write(file, doc);
        System.out.println("- Export: " + file.getAbsolutePath());
    }
    
    /**
     * Obtains the XML document object storing information on a project.
     * @param mproject the project metrics
     * @return the XML document object
     */
    public Document getDocument(ProjectMetrics mproject) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            export(doc, mproject);
            return doc;
            
        } catch (ParserConfigurationException e) {
            System.err.println("DOM: Export error occurred: " + e.getMessage() + ".");
        }
        return null;
    }
    
    /**
     * Exports information on a project to an XML document object.
     * @param doc the XML document object
     * @param mproject the project metrics
     */
    private void export(Document doc, ProjectMetrics mproject) {
        Element rootElem = doc.createElement(CodeForestRepository.TopElem);
        rootElem.setAttribute(CodeForestRepository.NameAttr, mproject.getName());
        rootElem.setAttribute(CodeForestRepository.DateAttr, Time.toString(Time.getCurrentTime()));
        doc.appendChild(rootElem);
        
        for (InteractionRecord record : interactionView.getInteractionRecordList()) {
            exportInteractionRecord(doc, rootElem, record);
        }
        
        for (Memo memo : memoView.getMemoList()) {
            exportMemo(doc, rootElem, memo);
        }
    }
    
    /**
     * Exports information on interaction records to an XML document object.
     * @param doc the XML document object
     * @param parent the parent of the XML element storing the information
     * @param record the interaction records
     */
    private void exportInteractionRecord(Document doc, Element parent, InteractionRecord record) {
        Element recordElem = doc.createElement(CodeForestRepository.InteractionElem);
        recordElem.setAttribute(CodeForestRepository.TimeAttr, String.valueOf(record.getTime()));
        recordElem.setAttribute(CodeForestRepository.DescriptionAttr, record.getDescription());
        recordElem.setAttribute(CodeForestRepository.TypeAttr, String.valueOf(record.getType()));
        
        recordElem.setAttribute(CodeForestRepository.TrunkHeightAttr, record.getTrunkHeight());
        recordElem.setAttribute(CodeForestRepository.TrunkRadiusAttr, record.getTrunkRadius());
        recordElem.setAttribute(CodeForestRepository.TrunkColorAttr, record.getTrunkColor());
        recordElem.setAttribute(CodeForestRepository.FoliageHeightAttr, record.getFoliageHeight());
        recordElem.setAttribute(CodeForestRepository.FoliageRadiusAttr, record.getFoliageRadius());
        recordElem.setAttribute(CodeForestRepository.FoliageColorAttr, record.getFoliageColor());
        
        parent.appendChild(recordElem);
    }
    
    /**
     * Exports information on memos to an XML document object.
     * @param doc the XML document object
     * @param parent the parent of the XML element storing the information
     * @param memo the memos
     */
    private void exportMemo(Document doc, Element parent, Memo memo) {
        Element memoElem = doc.createElement(CodeForestRepository.MemoElem);
        memoElem.setAttribute(CodeForestRepository.TimeAttr, String.valueOf(memo.getTime()));
        memoElem.setAttribute(CodeForestRepository.ClassNameAttr, memo.getClassName());
        memoElem.appendChild(doc.createTextNode(memo.getComments()));
        
        parent.appendChild(memoElem);
    }
    
    /**
     * Reads the XML document and stores information on interaction records and memos.
     */
    public void readXML() {
        File file = new File(getXMLFileName(projectMetrics));
        if (file.canRead()) {
            Document doc = XMLReader.read(file);
            
            NodeList list = doc.getElementsByTagName(CodeForestRepository.TopElem);
            if (list.getLength() > 0) {
                Element rootElem = (Element)list.item(0);
                
                readInteractionRecord(rootElem);
                readMemo(rootElem);
                
                interactionView.sort();
                memoView.sort();
            }
        }
    }
    
    /**
     * Reads information on interaction records.
     * @param rootElem the XML element that stores the interaction records
     */
    private void readInteractionRecord(Element rootElem) {
        NodeList interactionRecordNodes = rootElem.getElementsByTagName(CodeForestRepository.InteractionElem);
        for (int i = 0; i < interactionRecordNodes.getLength(); i++) {
            Node node = interactionRecordNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element)node;
                
                long time = getLong(elem.getAttribute(CodeForestRepository.TimeAttr));
                String description = elem.getAttribute(CodeForestRepository.DescriptionAttr);
                int type = getInteger(elem.getAttribute(CodeForestRepository.TypeAttr));
                String th = elem.getAttribute(CodeForestRepository.TrunkHeightAttr);
                String tr = elem.getAttribute(CodeForestRepository.TrunkRadiusAttr);
                String tc = elem.getAttribute(CodeForestRepository.TrunkColorAttr);
                String fh = elem.getAttribute(CodeForestRepository.FoliageHeightAttr);
                String fr = elem.getAttribute(CodeForestRepository.FoliageRadiusAttr);
                String fc = elem.getAttribute(CodeForestRepository.FoliageColorAttr);
                
                SettingData data = new SettingData(th, tr, tc, fh, fr, fc);
                InteractionRecord record = new InteractionRecord(time, description, type, data);
                interactionView.add(record);
            }
        }
    }
    
    /**
     * Reads information on memos.
     * @param rootElem the XML element that stores the memos
     */
    private void readMemo(Element rootElem) {
        NodeList memoNodes = rootElem.getElementsByTagName(CodeForestRepository.MemoElem);
        for (int i = 0; i < memoNodes.getLength(); i++) {
            Node node = memoNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element)node;
                
                long time = getLong(elem.getAttribute(CodeForestRepository.TimeAttr));
                String className = elem.getAttribute(CodeForestRepository.ClassNameAttr);
                String comments = "";
                
                NodeList nodes = elem.getChildNodes();
                Node text = nodes.item(0);
                if (text.getNodeType() == Node.TEXT_NODE) {
                    comments = ((Text)text).getNodeValue();
                }
                
                Memo memo = new Memo(time, className, comments);
                memoView.add(memo);
            }
        }
    }
    
    /**
     * Translates the integer string to its value.
     * @param str the integer string
     * @return the integer value
     */
    private int getInteger(String str) {
        return Integer.parseInt(str);
    }
    
    /**
     * Translates the long string to its value.
     * @param str long string
     * @return the long value
     */
    private long getLong(String str) {
        return Long.parseLong(str);
    }
}
