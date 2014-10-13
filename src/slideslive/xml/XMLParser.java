package slideslive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by WojtawDesktop on 13.10.14.
 */
public class XMLParser {
    private File xmlDocument;

    public XMLParser(){

    }

    public ArrayList<SlideRecord> parseXML(File xmlDocument){
        this.xmlDocument = xmlDocument;
        if(!checkIfValidDocument()) System.out.println("Invalid XML document");
        return generateSlideArray();
    }

    public ArrayList<SlideRecord> parseXML(String pathToXML){
        this.xmlDocument = new File(pathToXML);
        if(!checkIfValidDocument()) System.out.println("Invalid XML document");
        return generateSlideArray();
    }

    private boolean checkIfValidDocument() {
        if(xmlDocument.exists()) return true;
        else return false;
    }

    private ArrayList<SlideRecord> generateSlideArray(){
        ArrayList<SlideRecord> allSlides = new ArrayList<SlideRecord>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        NodeList nList = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlDocument);
            doc.getDocumentElement().normalize();

            nList = doc.getElementsByTagName("slide");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                int tmpTime = Integer.parseInt(getTagValue("timeSec", eElement));
                String tmpName = getTagValue("slideName", eElement);
                SlideRecord tmpSlide = new SlideRecord(tmpName,tmpTime);
                allSlides.add(tmpSlide);
            }
        }

        return allSlides;
    }


    private String getTagValue(String sTag, Element eElement){
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }

}
