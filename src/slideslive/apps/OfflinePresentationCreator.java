package slideslive.apps;

import com.github.axet.vget.VGet;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import slideslive.db.SlidesLiveDB;
import slideslive.output.Output;
import slideslive.output.OutputWriter;
import slideslive.xml.SlideRecord;
import slideslive.xml.XMLParser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by WojtawDesktop on 15.1.15.
 */
public class OfflinePresentationCreator {
    String pathToOnline = "http://slideslive.s3.amazonaws.com/data/presentations/";
    String pathToWindowsPlayer = "player/Play-SlidesLive.exe";
    String pathToWindowsSWF = "player/Play-SlidesLive.swf";
    String pathToMacPlayer = "player/Play-SlidesLive.app";
    String targetOfflineFolder = "D:\\Offline\\WTTC";
    File presentationFolder;
    File presentationDataFolder;
    int presentationID;
    String presentationTitle = "Untitled-Presentation";
    String serviceID;
    ArrayList<SlideRecord> allSlides;
    Output outputWriter;
    SlidesLiveDB SLDB = new SlidesLiveDB();
    protected boolean downloadRunning = false;

    public OfflinePresentationCreator(int presentationID, Output outputWriter){
        this.outputWriter = outputWriter;
        this.presentationID = presentationID;
    }

    public OfflinePresentationCreator(int presentationID, Output outputWriter, String targetOfflineFolder){
        this.outputWriter = outputWriter;
        this.presentationID = presentationID;
        this.targetOfflineFolder = targetOfflineFolder;
    }

    public void createPresentation(){
        try {
            createOfflinePresentation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDatabaseReadings(){
        SLDB.initConnection();
        serviceID = SLDB.getServiceID(presentationID);
        presentationTitle = SLDB.getCanonicalName(presentationID);
        SLDB.closeConnection();
    }

    private void downloadVideo() throws UnirestException, JSONException, IOException, InterruptedException {

        HttpResponse<JsonNode> response = Unirest.post("https://savedeo.p.mashape.com/download")
                .header("X-Mashape-Key", "McUHwvervHmshSSWcRcnHINiPMcRp14a4lGjsn2gkeUA3dAmCa")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("url", "https://www.youtube.com/watch?v="+serviceID)
                .asJson();

        JSONObject jsonObject = new JSONObject(response.getBody().toString());
        JSONArray formats = (JSONArray) jsonObject.get("formats");
        JSONObject finalFormat;
        //Looking for FullHD
        for(int i = 0; i < formats.length(); i++)
        {
            JSONObject tmpObject = formats.getJSONObject(i);
            if(tmpObject.getString("format").equals("1920x1080") && tmpObject.getString("ext").equals("mp4"))
                finalFormat = tmpObject;
        }
        //Looking for HD Ready
        for(int i = 0; i < formats.length(); i++)
        {
            JSONObject tmpObject = formats.getJSONObject(i);
            if(tmpObject.getString("format").equals("1280x720") && tmpObject.getString("ext").equals("mp4"))
                finalFormat = tmpObject;
        }
        //Take whatever crap
        finalFormat = formats.getJSONObject(0);

        outputWriter.printLog("Final format "+finalFormat.get("format"));
        outputWriter.printLog("Final URL for download is\n"+finalFormat.get("url"));


        Thread downloadProgress = new Thread() {
            public void run() {
                while(downloadRunning){
                    File file = new File(presentationDataFolder.getAbsolutePath()+"/"+presentationID+".mp4");
                    if(file.exists()){
                        outputWriter.printLog("Downloaded "+(file.length()/1048576));
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        downloadRunning = true;
        downloadProgress.start();


        URL videoURL = new URL(finalFormat.get("url").toString());
        FileUtils.copyURLToFile(videoURL, new File(presentationDataFolder.getAbsolutePath()+"/"+presentationID+".mp4"));
        downloadRunning = false;
        outputWriter.printLog("Download video finished");
    }

    private void createOfflinePresentation() throws IOException {
        getDatabaseReadings();
        outputWriter.printLog("Creating offline presentation ID: "+presentationID);
        presentationFolder = new File(targetOfflineFolder+"/"+presentationTitle);
        presentationDataFolder = new File(targetOfflineFolder+"/"+presentationTitle+"/"+presentationID);
        presentationFolder.mkdir();
        presentationDataFolder.mkdir();
        try {
            downloadVideo();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        File slideFolder = new File(presentationDataFolder.getAbsoluteFile()+"/original");
        slideFolder.mkdir();
        FileUtils.copyFile(new File(pathToWindowsPlayer),new File(targetOfflineFolder+"/"+presentationTitle+"/Play-SlidesLive.exe"));
        FileUtils.copyFile(new File(pathToWindowsSWF),new File(targetOfflineFolder+"/"+presentationTitle+"/Play-SlidesLive.swf"));
        FileUtils.copyDirectoryToDirectory(new File(pathToMacPlayer), new File(targetOfflineFolder + "/" + presentationTitle));
        URL xml = new URL(pathToOnline+presentationID+"/"+presentationID+".xml");
        FileUtils.copyURLToFile(xml, new File(presentationDataFolder.getAbsolutePath()+"/"+presentationID+".xml"));
        File playerParams = new File(presentationFolder+"/playerParams.txt");
        FileUtils.writeStringToFile(playerParams, presentationID+"");

        outputWriter.printLog("Basic structure for "+presentationTitle+" created");
        //Parsing XML
        XMLParser parser = new XMLParser();
        allSlides = parser.parseXML(presentationDataFolder.getAbsolutePath()+"/"+presentationID+".xml");

        outputWriter.printLog("XML Parsed successfully");
        //Downloading the slides
        for(int i = 0; i < allSlides.size(); i++){
            String tmpName = allSlides.get(i).getSlideName();
            outputWriter.printLog("Downloading slide "+tmpName);
            URL image = new URL(pathToOnline+presentationID+"/slides/original/"+tmpName+".png");
            FileUtils.copyURLToFile(image, new File(presentationDataFolder.getAbsolutePath()+"/original/"+tmpName+".png"));
        }

        outputWriter.printLog("Presentation downloaded successfully");

    }

}
