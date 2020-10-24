package grjava3e;
// Texture.java: draw (1) a vertical strip of repeated color gradient,
// (2) a texture created using BufferedImagepaint, and applied on a 
// text string "TEXTURE", and (3) a texture defined by an image file.

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Texture extends Frame {
   public static void main(String[] args){new Texture();}
   Texture() {
      super("Java 2D Texture.");
      addWindowListener(new WindowAdapter()
         {@Override
        public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(800, 450); 
      add("Center", new CvTexture());
      setVisible(true);
   }
}
class CvTexture extends Canvas {
   private BufferedImage image;
   @Override
public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2 = (Graphics2D)g;
      // Gradient Strip
      GradientPaint gp = 
         new GradientPaint(20,100,Color.yellow,20,160,Color.blue,true);
      g2.setPaint(gp);
      g2.fillRect(20, 20, 30, 350);
      // Generating texture of 6x6 pixels
      image = new BufferedImage(6, 6, BufferedImage.TYPE_INT_RGB);
      Graphics2D gi = image.createGraphics();
      gi.setColor(Color.gray);
      gi.fillRect(0,0,6,6);
      gi.setColor(Color.red);
      gi.drawLine(0,0,6,3);
      gi.drawLine(0,6,6,3);
      TexturePaint tp = 
         new TexturePaint(image, new Rectangle(50,20,6,6));
      g2.setPaint(tp);
      Font f = new Font("Arial", Font.BOLD, 150);
      g2.setFont(f);
      g2.drawString("TEXTURE", 70, 130);
      // Image file as texture
      URL url = getClass().getClassLoader().getResource("mondriaan.png");
      try { 
        image = ImageIO.read(url);
        } catch (IOException ex) {
        ex.printStackTrace();
        } 
      tp = new TexturePaint(image, new Rectangle2D.Double(70,150, 
           image.getWidth(), image.getHeight()));
      g2.setPaint(tp);
      Shape rectangle = new Rectangle(70,150,700,220);
      g2.fill(rectangle);       
   }
}
