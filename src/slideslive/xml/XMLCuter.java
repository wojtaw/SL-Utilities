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

public class XMLCuter {
	private Output output;
	private File xmlDocument;
	private int[] cuts;
	private int[] delays;
    private int[] cStart;
    private int[] cEnd;
	private StringBuilder resultXMLString = new StringBuilder();	
	private int presentationID;	
	private String[] transformedNames;
	private int[] xmlTimes;
	
	public XMLCuter(Output output, String xmlForProccessing){
		this.output = output;
		xmlDocument = new File(xmlForProccessing);
	}

    //Spaces define wether calculation by delay or spaces length should be used
	public boolean transformXML(int[] cuts, int[] delays){
        this.cuts = cuts;
        this.delays = delays;
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

    public boolean cutSpaces(int[] cutStart, int[] cutEnd) {
        output.printLog("XML Cutting");
        this.cStart = cutStart;
        this.cEnd = cutEnd;

        if(cStart.length = cEnd.length){
            System.out.println("Fields are not same size");
            return false;
        }
        if(!xmlDocument.exists()){
            System.out.println("Problem accessing XML document");
            return false;
        }

        try {
            parseXML();
            createRightTiming();
            //writeXMLFile();
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
		//Put zero in first slide
		xmlTimes[0] = 0;
		
		
		int newOrderID = 1;
		int currentCutIndex = 0;

	
		resultXMLString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<videoContent>\n");
		for (int i = 0; i < xmlTimes.length; i++) {
			if(xmlTimes[i] > cuts[currentCutIndex] && currentCutIndex < cuts.length-1) currentCutIndex++;
			
			//Do the calculations
			xmlTimes[i] -= delays[currentCutIndex];
			
			//If time is cut out because of pause, continue with this loop
			if(currentCutIndex > 0) System.out.println("Current "+xmlTimes[i]+" / "+cuts[currentCutIndex-1]);
			if(currentCutIndex > 0 && xmlTimes[i] < cuts[currentCutIndex-1]){
                System.out.println("Not"+currentCutIndex);
            } else {
				resultXMLString.append("\t<slide>\n");
				resultXMLString.append("\t\t<orderId>"+newOrderID+"</orderId>\n");
				resultXMLString.append("\t\t<timeSec>"+xmlTimes[i]+"</timeSec>\n");
				resultXMLString.append("\t\t<slideName>"+transformedNames[i]+"</slideName>\n");
				resultXMLString.append("\t</slide>\n");
				newOrderID++;		
			}	
		}
		resultXMLString.append("</videoContent>");
        System.out.println(resultXMLString.toString());
	}

	private void parseXML() throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlDocument);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("slide");
 
		transformedNames = new String[nList.getLength()];
		xmlTimes = new int[nList.getLength()];
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		      Element eElement = (Element) nNode;
		      
		      int newTime = (Integer.parseInt(getTagValue("timeSec", eElement)));
		      
		      xmlTimes[temp] = newTime;
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
		System.out.print(resultXMLString.toString());
		
		Writer out = new OutputStreamWriter(new FileOutputStream(xmlDocument, false), "UTF8");			
		out.write(resultXMLString.toString());
		out.close();
		
		return true;	
	}

//New method of cuting


    private void createRightTiming() {
        //Put zero in first slide
        xmlTimes[0] = 0;


        int newOrderID = 1;
        int currentCutIndex = 0;


        resultXMLString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<videoContent>\n");
        for (int i = 0; i < xmlTimes.length; i++) {
            if(xmlTimes[i] > cuts[currentCutIndex] && currentCutIndex < cuts.length-1) currentCutIndex++;

            //Do the calculations
            xmlTimes[i] -= delays[currentCutIndex];

            //If time is cut out because of pause, continue with this loop
            if(currentCutIndex > 0) System.out.println("Current "+xmlTimes[i]+" / "+cuts[currentCutIndex-1]);
            if(currentCutIndex > 0 && xmlTimes[i] < cuts[currentCutIndex-1]){
                System.out.println("Not"+currentCutIndex);
            } else {
                resultXMLString.append("\t<slide>\n");
                resultXMLString.append("\t\t<orderId>"+newOrderID+"</orderId>\n");
                resultXMLString.append("\t\t<timeSec>"+xmlTimes[i]+"</timeSec>\n");
                resultXMLString.append("\t\t<slideName>"+transformedNames[i]+"</slideName>\n");
                resultXMLString.append("\t</slide>\n");
                newOrderID++;
            }
        }
        resultXMLString.append("</videoContent>");
        System.out.println(resultXMLString.toString());
    }
}
