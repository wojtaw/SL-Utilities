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

        String[] start = {"1:01:14","1:04:02","1:06:28","1:10:52","1:21:08","1:27:42","1:37:27","1:49:00"};
        String[] end = {"1:01:23","1:04:25","1:08:56","1:11:23","1:23:23","1:32:19","1:41:15","1:50:54"};

        TimecodeFormatter timecodeFormatter = new TimecodeFormatter(outputWriter);
        int[] cutStart = timecodeFormatter.getSecondsArrayFromHHMMSSArray(start);
        int[] cutEnd = timecodeFormatter.getSecondsArrayFromHHMMSSArray(end);
        /*

        int[] cutStart = {519,1092,1589,1677,1769,1965,2161,2230,2291,2321,2452,3089};
        int[] cutEnd = {528,1108,1609,1740,1869,2020,2171,2240,2303,2361,2465,3114};


        //XMLTransformer xmlTransformer = new XMLTransformer(outputWriter, "C:/vojtaciml/TMP/38891488.xml", 1315, 4538); // -1392

        */
        XMLCuter xmlCuter = new XMLCuter(outputWriter, "C:\\Users\\WojtawDesktop\\Desktop\\38894824.xml");
        xmlCuter.cutSpaces(cutStart, cutEnd);
    }
}

