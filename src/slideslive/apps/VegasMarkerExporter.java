package slideslive.apps;

import slideslive.xml.SlideRecord;
import slideslive.xml.XMLParser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * Vegas Marker Exporter is utility for exporting SlidesLive synchronized presentations into Sony Vegas marker file that can imported.
 * Supply it with SlidesLive presentation ID and it will create txt marker file. Copy all the data and paste them into vegas Edit Details menu
 * Go to "Edit details: }Alt-6 or View/Edit Details, select Markers and hit upper left button for select all. Then paste results and you got markers
 */
public class VegasMarkerExporter {
    String pathToXML = "http://slideslive.s3.amazonaws.com/data/presentations/";
    int presentationID;
    ArrayList<SlideRecord> allSlides;

    public VegasMarkerExporter(){

    }

    public void exportMarkerFile(int presentationID){
        this.presentationID = presentationID;
        downloadPresentationXML();
        XMLParser parser = new XMLParser();
        allSlides = parser.parseXML("tmp/markerSource.xml");
        for(int i = 0; i < allSlides.size(); i++){
            System.out.println(allSlides.get(i).getSlideName());
        }
    }

    private boolean downloadPresentationXML(){
        try {
            URL website = new URL(pathToXML+presentationID+"/"+presentationID+".xml");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = null;
            fos = new FileOutputStream("tmp/markerSource.xml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
