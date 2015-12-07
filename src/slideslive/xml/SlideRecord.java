package slideslive.xml;

/**
 * Created by WojtawDesktop on 13.10.14.
 */
public class SlideRecord {
    private String slideName;
    private int slideTime;

    public SlideRecord(String slideName,int slideTime){
        this.slideName = slideName;
        this.slideTime = slideTime;
    }

    public String getSlideName() {
        return slideName;
    }

    public int getSlideTime() {
        return slideTime;
    }

    public void setSlideName(String name){
        slideName = name;
    }

    public void setSlideTime(int time){
        slideTime = time;
    }
}
