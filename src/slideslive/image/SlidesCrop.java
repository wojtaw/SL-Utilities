package slideslive.image;

import slideslive.output.Output;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by WojtawDesktop on 5.10.14.
 */
public class SlidesCrop {
    private Output output;

    public SlidesCrop(Output output){
        this.output = output;
        File slide = new File("0-0829.png");
        String newSlideName = slide.getName();
        BufferedImage slideImage = null;
        try {
            slideImage = ImageIO.read(slide);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finishedSlide = cropSlide(slideImage);
        saveSlide(finishedSlide,newSlideName);

    }

    private BufferedImage cropSlide(BufferedImage src) {
        BufferedImage finishedSlide = src.getSubimage(100, 0, src.getWidth()-200, src.getHeight()-200);
        return finishedSlide;
    }

    private boolean saveSlide(BufferedImage imageData,String slideName){
        File outputfile = new File(slideName);
        try {
            ImageIO.write(imageData, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}
