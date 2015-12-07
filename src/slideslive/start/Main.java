package slideslive.start;

import slideslive.apps.OfflinePresentationCreator;
import slideslive.apps.VegasMarkerExporter;
import slideslive.image.SlidesCrop;
import slideslive.output.Output;
import slideslive.output.OutputWriter;
import slideslive.start.*;
import slideslive.xml.XMLCuter;
import slideslive.xml.XMLTransformer;


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

        int[] cutTimes = {519,1092,1589,1677,1769,1965,2161,2230,2291,2321,2452,3089};
        int[] cutDelays = {9,16,20,63,100,55,10,10,12,40,13,25};

        int[] cutStart = {519,1092,1589,1677,1769,1965,2161,2230,2291,2321,2452,3089};
        int[] cutEnd = {528,1108,1609,1740,1869,2020,2171,2240,2303,2361,2465,3114};
            //XMLTransformer xmlTransformer = new XMLTransformer(outputWriter, "C:/vojtaciml/TMP/38891488.xml", 1315, 4538); // -1392

            //If you put true, you define delays by length of each cut, if false, you specify exact delay to the original
            XMLCuter xmlCuter = new XMLCuter(outputWriter, "C:\\Users\\WojtawDesktop\\Desktop\\38894829.xml");
            xmlCuter.cutSpaces(cutStart, cutEnd);
            //XMLTransformer xmlTransformer = new XMLTransformer(outputWriter, args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			/*
            if(xmlTransformer.transformXML()){
				System.exit(0);
			} else {
				outputWriter.printErr("XML transformer analyzer failed");
				System.exit(1);
			}

        } else {
            outputWriter.printErr("Incorrect arguments! 1 - Path to XML file, 2 - offset in seconds, 3 - external audio length in seconds");
            System.exit(1);
        }
        */
	}
}

