package slideslive.util;

import slideslive.output.Output;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by WojtawDesktop on 7.12.15.
 */
public class TimecodeFormatter {
    Output output;

    public TimecodeFormatter(Output output){
        this.output = output;
    }

    public int getSecondsFromHHMMSS(String timecode) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(timecode);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int seconds = (int) (date.getTime() / 1000);
        output.printLog("seconds: "+seconds);
        return seconds;
    }

    public int[] getSecondsArrayFromHHMMSSArray(String[] timesArray){
        int[] resultArray = new int[timesArray.length];
        for(int i=0; i<timesArray.length; i++){
            resultArray[i] = getSecondsFromHHMMSS(timesArray[i]);
        }
        return resultArray;
    }
}
