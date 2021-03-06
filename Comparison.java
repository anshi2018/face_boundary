package analysis;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


//@author bibhu

public class Comparison {
	static int num =0;
	static String base;
	
	public static void holdImage(BufferedImage image) throws IOException {
	    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	    result.getGraphics().drawImage(image, 0, 0, null);
	    WritableRaster raster = result.getRaster();
	    int[] pixels = new int[image.getWidth()];
	    for (int y = 0; y < image.getHeight(); y++) {
	        raster.getPixels(0, y, image.getWidth(), 1, pixels);
	        for (int i = 0; i < pixels.length; i++) {
	            if (pixels[i] ==0) pixels[i] = 255;
	            else pixels[i] = 0;
	        }
	        raster.setPixels(0, y, image.getWidth(), 1, pixels);
	    }
	    File f = new File("F:\\mini pro\\cop_"+base+".jpg");
		if (!ImageIO.write(result, "JPEG", f)) {
		  throw new RuntimeException("Unexpected error writing image");
		}
	}
	public static BufferedImage thresholdImage(BufferedImage image,BufferedImage comp) throws IOException {
		int threshold = 100;
	    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	    result.getGraphics().drawImage(image, 0, 0, null);
	    BufferedImage compresult =new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	    compresult.getGraphics().drawImage(comp, 0, 0, null);
	    WritableRaster raster = result.getRaster();
	    WritableRaster raster2 = compresult.getRaster();
	    int[] pixels = new int[image.getWidth()];
	    int[] compixels =new int[comp.getWidth()];
	    double point=0.0,border=0.0;
	    for (int y = 0; y < image.getHeight(); y++) {
	        raster.getPixels(0, y, image.getWidth(), 1, pixels);
	        raster2.getPixels(0, y, comp.getWidth(), 1, compixels);
	        
	        for (int i = 0; i < pixels.length; i++) {
	            if (pixels[i] ==255){
	            	point++;
	            	if(compixels[i]!=255||(i-1>=0&&compixels[i-1]!=255)||(i-2>=0&&compixels[i-2]!=255)||
	            			(i+1<=image.getWidth()&&compixels[i+1]!=255)||
	            			(i+2<=image.getWidth()&&compixels[i+2]!=255))	
	            	border++;
	            }
	        }
	        
	        raster.setPixels(0, y, image.getWidth(), 1, pixels);
	        raster2.getPixels(0, y, comp.getWidth(), 1, compixels);
	    }
	    System.out.println(point+" "+border+" "+(border/point)*100.0);
	   return result;
	}
	
	public static void main(String args[]) throws IOException{
		base ="obama";
		
		BufferedImage src = ImageIO.read(new File("F:\\mini pro\\"+base+".jpg"));
		BufferedImage comp = ImageIO.read(new File("F:\\mini pro\\crop_"+ base+".jpg"));
		holdImage(src);
		BufferedImage newsrc= ImageIO.read(new File("F:\\mini pro\\cop_obama.jpg"));
		BufferedImage result = thresholdImage(newsrc,comp);
		
		JFrame jframe = new JFrame();
		jframe.getContentPane().setLayout(new FlowLayout());
		jframe.getContentPane().add(new JLabel(new ImageIcon(result)));
		jframe.pack();
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
