package grjava1e;
// Anim.java: Animation with double buffering.

// Copied from Appendix F (solution to Exercise 5.4) of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Anim extends Frame {
   public static void main(String[] args) {new Anim();}

   Anim() {
      super("Animation (double buffering)");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      add("Center", new CvAnim());
      Dimension dim = getToolkit().getScreenSize();
      setSize(dim.width / 2, dim.height / 2);
      setLocation(dim.width / 4, dim.height / 4);
      setVisible(true);
   }
}

class CvAnim extends Canvas implements Runnable {
   float rWidth = 10.0F, rHeight = 10.0F, xC, yC, pixelSize;
   int centerX, centerY, w, h;
   Dimension d;
   Image image;
   Graphics gImage;

   float alpha = 0;
   Thread thr = new Thread(this);

   @Override
public void run() {
      try {
         for (;;) {
            alpha += 0.01;
            repaint();
            Thread.sleep(20);
         }
      } catch (InterruptedException e) {
      }
   }

   CvAnim() {
      thr.start();
   }

   void initgr() {
      d = getSize();
      int maxX = d.width - 1, maxY = d.height - 1;
      pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
      centerX = maxX / 2; centerY = maxY / 2;
      xC = rWidth / 2; yC = rHeight / 2;
   }

   int iX(float x) {return Math.round(centerX + x / pixelSize);}
   int iY(float y) {return Math.round(centerY - y / pixelSize);}
   @Override
public void update(Graphics g) {paint(g);}

   @Override
public void paint(Graphics g) {
      initgr();
      if (w != d.width || h != d.height) {
         w = d.width; h = d.height;
         image = createImage(w, h);
         gImage = image.getGraphics();
      }
      float r = 0.8F * Math.min(xC, yC), 
            x = r * (float) Math.cos(alpha), 
            y = r * (float) Math.sin(alpha);
      gImage.clearRect(0, 0, w, h);
      // Every 20 ms, the following line is drawn.
      // Each time, its endpoint (x, y) is a
      // different point on a circle:
      gImage.drawLine(iX(0), iY(0), iX(x), iY(y));
      g.drawImage(image, 0, 0, null);
   }
}