package slideslive.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import slideslive.output.Output;

public class XMLCuter {
	private Output output;
	private File xmlDocument;
	private int[] cuts;
	private int[] delays;
    private int[] cStart;
    private int[] cEnd;
	private StringBuilder resultXMLString = new StringBuilder();
    ArrayList<SlideRecord> allSlides;
	
	public XMLCuter(Output output, String xmlForProccessing){
		this.output = output;
		xmlDocument = new File(xmlForProccessing);
	}

    public boolean cutSpaces(int[] cutStart, int[] cutEnd) {
        output.printLog("XML Cutting");
        this.cStart = cutStart;
        this.cEnd = cutEnd;

        if(cStart.length != cEnd.length){
            System.out.println("Fields are not same size");
            return false;
        }
        if(!xmlDocument.exists()){
            System.out.println("Problem accessing XML document");
            return false;
        }

        //First parse data from the original file
        XMLParser parser = new XMLParser();
        allSlides = parser.parseXML(xmlDocument);

        createRightTiming();
        createFinalString();

        try {
            writeXMLFile();
        } catch (IOException e) {
            output.printErr(""+e.getStackTrace());
            return false;
        }

        return true;
    }


    private boolean writeXMLFile() throws IOException {
		System.out.print(resultXMLString.toString());
		
		Writer out = new OutputStreamWriter(new FileOutputStream(xmlDocument, false), "UTF8");			
		out.write(resultXMLString.toString());
		out.close();
		
		return true;	
	}

    private void createRightTiming() {
        //For each cut, find slides that are inside, remove them and recalculate the rest
        for(int i=0; i < cStart.length; i++){
            Iterator<SlideRecord> iterator = allSlides.iterator();
            int cutSizeInSeconds = cEnd[i] - cStart[i];
            output.printLog(""+cutSizeInSeconds);
            if(cutSizeInSeconds <= 0) output.printErr("Suspicious cut size");
            while(iterator.hasNext()){
                SlideRecord tmp = iterator.next();

                if(tmp.getSlideTime() > cStart[i] && tmp.getSlideTime() < cEnd[i]){
                    iterator.remove();
                    output.printLog(tmp.getSlideTime()+" was removed, because is part of cut "+cStart[i]+" - "+cEnd[i]);
                }else if(tmp.getSlideTime() >= cEnd[i]){
                    tmp.setSlideTime(tmp.getSlideTime() - cutSizeInSeconds);
                }
            }
        }

        //Put zero in first slide
        allSlides.get(0).setSlideTime(0);
    }

    private void createFinalString() {
        resultXMLString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<videoContent>\n");
        int newOrderID = 1;
        Iterator<SlideRecord> iterator = allSlides.iterator();
        while(iterator.hasNext()){
            SlideRecord tmp = iterator.next();
            resultXMLString.append("\t<slide>\n");
            resultXMLString.append("\t\t<orderId>"+newOrderID+"</orderId>\n");
            resultXMLString.append("\t\t<timeSec>"+tmp.getSlideTime()+"</timeSec>\n");
            resultXMLString.append("\t\t<slideName>"+tmp.getSlideName()+"</slideName>\n");
            resultXMLString.append("\t</slide>\n");
            newOrderID++;
        }

        resultXMLString.append("</videoContent>");
        System.out.println(resultXMLString.toString());
    }
}