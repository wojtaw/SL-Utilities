package slideslive.start;

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
        VegasMarkerExporter markerExporter = new VegasMarkerExporter();
        markerExporter.exportMarkerFile(38892145);
        //SlidesCrop slidesCrop = new SlidesCrop(outputWriter);

        /*
        if(args.length == 3 || manualLaunch){

            int[] cutTimes = {1420,100000};
            int[] cutDelays = {0,-214};
            //XMLTransformer xmlTransformer = new XMLTransformer(outputWriter, "C:/vojtaciml/TMP/38891488.xml", 1315, 4538); // -1392

            XMLCuter xmlCuter = new XMLCuter(outputWriter, "C:\\Users\\WojtawDesktop\\Desktop\\38891617.xml", cutTimes, cutDelays);
            xmlCuter.transformXML();
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

