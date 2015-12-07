package slideslive.start;

import slideslive.apps.OfflinePresentationCreator;
import slideslive.apps.VegasMarkerExporter;
import slideslive.image.SlidesCrop;
import slideslive.output.Output;
import slideslive.output.OutputWriter;
import slideslive.start.*;
import slideslive.util.TimecodeFormatter;
import slideslive.xml.XMLCuter;
import slideslive.xml.XMLTransformer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Main {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {

		Output outputWriter = new OutputWriter();		
		//Check arguments and run analyzer or return error
        boolean manualLaunch = true;
        /*
        VegasMarkerExporter markerExporter = new VegasMarkerExporter();
        markerExporter.exportMarkerFile(38893143);

        */

        /*
        int[] toBeDownloaded = new int[]{38893796,38893952,38893793,38893789,38893953,38893954};

        for(int i = 0; i < toBeDownloaded.length; i++){
            OfflinePresentationCreator offlinePresentationCreator = new OfflinePresentationCreator(toBeDownloaded[i],outputWriter,"D:\\Offline\\Goethe");
            offlinePresentationCreator.createPresentation();
        }
        */



        //if(args.length == 3 || manualLaunch){

        //Templates
        //String[] start = {"","","","","","","","","","","",""};
        //String[] end = {"","","","","","","","","","","",""};

        String[] start = {"0:08:39","0:18:12","0:26:29","0:27:57","0:29:29","0:32:45","0:36:01","0:37:10","0:38:11","0:38:41","0:40:52","0:51:29"};
        String[] end = {"0:08:48","0:18:28","0:26:49","0:29:00","0:31:09","0:33:40","0:36:11","0:37:20","0:38:23","0:39:21","0:41:05","0:51:54"};

        TimecodeFormatter timecodeFormatter = new TimecodeFormatter(outputWriter);
        int[] cutStart = timecodeFormatter.getSecondsArrayFromHHMMSSArray(start);
        int[] cutEnd = timecodeFormatter.getSecondsArrayFromHHMMSSArray(end);
        /*

        int[] cutStart = {519,1092,1589,1677,1769,1965,2161,2230,2291,2321,2452,3089};
        int[] cutEnd = {528,1108,1609,1740,1869,2020,2171,2240,2303,2361,2465,3114};


        //XMLTransformer xmlTransformer = new XMLTransformer(outputWriter, "C:/vojtaciml/TMP/38891488.xml", 1315, 4538); // -1392

        //If you put true, you define delays by length of each cut, if false, you specify exact delay to the original
        XMLCuter xmlCuter = new XMLCuter(outputWriter, "C:\\Users\\WojtawDesktop\\Desktop\\38894829.xml");
        xmlCuter.cutSpaces(cutStart, cutEnd);
        */
	}
}

