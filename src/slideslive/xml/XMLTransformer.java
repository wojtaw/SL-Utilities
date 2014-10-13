package slideslive.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import slideslive.output.Output;

public class XMLTransformer {
	private Output output;
	private File xmlDocument;
	private int offset;
	private int audioLength;
	private StringBuilder resultXMLString = new StringBuilder();	
	private int presentationID;	
	private String[] transformedNames;
	private int[] transformedTimes;
	
	public XMLTransformer(Output output, String xmlForProccessing, int offset, int audioLength){
		this.offset = offset;
		this.output = output;
		this.audioLength = audioLength;
		xmlDocument = new File(xmlForProccessing);
	}
	
	public boolean transformXML(){
		output.printLog("XML Transformation");
		if(!xmlDocument.exists()) return false;
		
		try {			
			parseXML();
			validateAndCreateXMLString();
			writeXMLFile();
		} catch (ParserConfigurationException e) {
			output.printErr(""+e.getStackTrace());
			return false;
		} catch (SAXException e) {
			output.printErr(""+e.getStackTrace());
			return false;
		} catch (IOException e) {
			output.printErr(""+e.getStackTrace());
			return false;
		}	
		return true;
	}
	

	private void validateAndCreateXMLString() {
		//Determine last position of negative time and last time to be encountered
		int startPosition = 0;
		int endPosition = 0;
		for (int i = 0; i < transformedTimes.length; i++) {
			if(transformedTimes[i] < 0) startPosition = i;
			if(transformedTimes[i] <= audioLength) endPosition = i;
		}

		//Put zero in first slide
		transformedTimes[startPosition] = 0;
		
		
		int newOrderID = 1;
		resultXMLString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<videoContent>\n");
		for (int i = startPosition; i <= endPosition; i++) {        
		      resultXMLString.append("\t<slide>\n");
		      resultXMLString.append("\t\t<orderId>"+newOrderID+"</orderId>\n");
		      resultXMLString.append("\t\t<timeSec>"+transformedTimes[i]+"</timeSec>\n");
		      resultXMLString.append("\t\t<slideName>"+transformedNames[i]+"</slideName>\n");
		      resultXMLString.append("\t</slide>\n");
		      newOrderID++;
		}
		resultXMLString.append("</videoContent>");	
	}

	private void parseXML() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlDocument);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("slide");
 
		transformedNames = new String[nList.getLength()];
		transformedTimes = new int[nList.getLength()];
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		      Element eElement = (Element) nNode;
		      
		      int newTime = (Integer.parseInt(getTagValue("timeSec", eElement))-offset);
		      
		      transformedTimes[temp] = newTime;
		      transformedNames[temp] = getTagValue("slideName", eElement);
		   }
		}
	}
	
	private String parseSlideName(Element eElement){
		String workingString = getTagValue("slideMedium", eElement);
		int trimIndex = workingString.lastIndexOf("/") + 1;
		return workingString.substring(trimIndex);
	}
	

	private String getTagValue(String sTag, Element eElement){
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		
		Node nValue = (Node) nlList.item(0);
		
		return nValue.getNodeValue();
	}		
	
	private boolean writeXMLFile() throws IOException {
		
		Writer out = new OutputStreamWriter(new FileOutputStream(xmlDocument, false));			
		out.write(resultXMLString.toString());
		out.close();
		return true;	
	}	
	

}
