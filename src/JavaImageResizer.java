import java.awt.Graphics;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DecimalFormat;


import javax.imageio.ImageIO;

import sun.java2d.ScreenUpdateManager;
/**
 * This class will resize all the images in a given folder
 * @author 
 *
 */
public class JavaImageResizer 
{
	public enum PixelDensity
	{
		MDPI,
		HDPI,
		XHDPI,
		XXHDPI,
		XXXHDPI
	};
	
    public static void main(String[] args) throws IOException 
    {

        String basePath="B:\\PROJECTS\\EPI\\Android\\Android Images";
    	String hdpiPath = "\\HDPI";
    	String mdpiPath = "\\MDPI";
    	String sourcePath = basePath + hdpiPath+"\\";
    	String targetPAth = basePath + mdpiPath+"\\";
        
    	File sourceFolder = new File(basePath+hdpiPath);
        File targetFolder = new File (basePath + mdpiPath);
        

        File[] listOfSourceFiles = sourceFolder.listFiles();
        File[] listOfTargetFiles = targetFolder.listFiles();
        
        System.out.println("Total No of Source Files: "+listOfSourceFiles.length);
        
        for(File file: targetFolder.listFiles()) file.delete();
        System.out.println("Deleted "+ listOfTargetFiles.length +" in target folder \n");
        
        BufferedImage img = null;
        BufferedImage tempImg = null;
        File newFile = null;
        
        for (int i = 0; i < listOfSourceFiles.length; i++) 
        {
              if (listOfSourceFiles[i].isFile()) 
              {
                System.out.println("File " + listOfSourceFiles[i].getName());
                img = ImageIO.read(new File(sourcePath +listOfSourceFiles[i].getName()));
                int type = img.getType();
                tempImg = resizeImage(img, img.getWidth(), img.getHeight(), PixelDensity.HDPI, PixelDensity.MDPI, type);
               
                newFile = new File(targetPAth+listOfSourceFiles[i].getName());
                
                if(getMIMEType(listOfSourceFiles[i]).equalsIgnoreCase("image/jpeg"))
                {
                	ImageIO.write(tempImg, "jpg", newFile);
                }
                else if(getMIMEType(listOfSourceFiles[i]).equalsIgnoreCase("image/png"))
                {
                	ImageIO.write(tempImg, "png", newFile);
                }    
              }
        }
        System.out.println("DONE");
    }

    private static String getMIMEType(File file)    
    {
	     FileNameMap fileNameMap = URLConnection.getFileNameMap();
	     String type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
	     return type;
    }
    
    
    /**
     * This function resize the image file and returns the BufferedImage object that can be saved to file system.
     */
    public static BufferedImage resizeImage(final Image image, int width, int height, PixelDensity sourceSize, PixelDensity targetSize, int type) 
    {
    	int targetWidth=height;
    	int targetHeight=width;
    	
    	if(sourceSize == PixelDensity.HDPI && targetSize== PixelDensity.MDPI)
    	{
    		targetHeight = (int)Math.round(height / 1.5);
    		targetWidth = (int)Math.round(width / 1.5);
    	}
    	
    	if(sourceSize == PixelDensity.MDPI && targetSize== PixelDensity.HDPI)
    	{
    		targetHeight = (int)Math.round(height * 1.5);
    		targetWidth = (int)Math.round(width * 1.5);
    	}
    	
	    final BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, type);
	    final Graphics2D graphics2D = bufferedImage.createGraphics();
	  
	    graphics2D.setComposite(AlphaComposite.Src);
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    graphics2D.drawImage(image, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	
	    return bufferedImage;
    }  
    
}
