package grjava3e;
// Transparency.java: draws 11 blue circles over 11 red squares,
// with transparency alpha changed from 0.0 to 1.0 
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;

public class Transparency extends Frame {
   public static void main(String[] args){new Transparency();}
   Transparency() {
      super("Java 2D Transparency.");
      addWindowListener(new WindowAdapter()
         {@Override
        public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(1040, 150); 
      add("Center", new CvTransparency());
      setVisible(true);
   }
}

class CvTransparency extends Canvas {
   private static int gap=20,width=60,offset=10,deltaX=gap+width+offset;
   private Ellipse2D blueCircle = new Ellipse2D.Double(gap+offset, 
      gap+offset, width+offset, width+offset);
   private Rectangle redSquare = new Rectangle(gap, gap, width, width);
   
   private AlphaComposite makeComposite(float alpha) {
      int type = AlphaComposite.SRC_OVER;
      return(AlphaComposite.getInstance(type, alpha));
   }
   private void drawShapes(Graphics2D g2, float alpha) {
      Composite myComposite = g2.getComposite();
      g2.setPaint(Color.red);
      g2.fill(redSquare);
      g2.setComposite(makeComposite(alpha));
      g2.setPaint(Color.blue);
      g2.fill(blueCircle);
      g2.setComposite(myComposite);
   }
   @Override
public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2 = (Graphics2D)g;
      for(int i=0; i<11; i++) {
         drawShapes(g2, i*0.1F);
         g2.translate(deltaX, 0);
      }
   }
}
