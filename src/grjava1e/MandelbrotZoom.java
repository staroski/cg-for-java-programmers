package grjava1e;
// MandelbrotZoom.java: Mandelbrot set, cropping and zooming in.
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MandelbrotZoom extends Frame {
   public static void main(String[] args) {new MandelbrotZoom();}

   MandelbrotZoom() {
      super("Drag left mouse button to crop and zoom. " + 
            "Click right mouse button to restore.");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(800, 600);
      add("Center", new CvMandelbrotZoom());
      setVisible(true);
   }
}

class CvMandelbrotZoom extends Canvas {
   final double minRe0 = -2.0, maxRe0 = 1.0, 
                minIm0 = -1.0, maxIm0 = 1.0;
   double minRe = minRe0, maxRe = maxRe0, 
          minIm = minIm0, maxIm = maxIm0, factor, r;
   int n, xs, ys, xe, ye, w, h;

   void drawWhiteRectangle(Graphics g) {
      g.drawRect(Math.min(xs, xe), Math.min(ys, ye), 
                 Math.abs(xe - xs), Math.abs(ye - ys));
   }

   boolean isLeftMouseButton(MouseEvent e) {
      return (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0;
   }

   CvMandelbrotZoom() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent e) {
            if (isLeftMouseButton(e)) {
               xs = xe = e.getX(); // Left button
               ys = ye = e.getY();
            } else {
               minRe = minRe0; // Right button
               maxRe = maxRe0;
               minIm = minIm0;
               maxIm = maxIm0;
               repaint();
            }
         }

         @Override
        public void mouseReleased(MouseEvent e) {
            if (isLeftMouseButton(e)) {
               xe = e.getX(); // Left mouse button released
               ye = e.getY(); // Test if points are really distinct:
               if (xe != xs && ye != ys) {
                  int xS = Math.min(xs, xe), xE = Math.max(xs, xe), 
                      yS = Math.min(ys, ye), yE = Math.max(ys, ye), 
                      w1 = xE - xS, h1 = yE - yS, a = w1 * h1, 
                      h2 = (int) Math.sqrt(a / r), w2 = (int) (r * h2), 
                      dx = (w2 - w1) / 2, dy = (h2 - h1) / 2;
                  xS -= dx; xE += dx;
                  yS -= dy; yE += dy; // aspect ration corrected
                  maxRe = minRe + factor * xE; 
                  maxIm = minIm + factor * yE;
                  minRe += factor * xS;
                  minIm += factor * yS;
                  repaint();
               }
            }
         }
      });

      addMouseMotionListener(new MouseMotionAdapter() {
         @Override
        public void mouseDragged(MouseEvent e) {
            if (isLeftMouseButton(e)) {
               Graphics g = getGraphics();
               g.setXORMode(Color.black);
               g.setColor(Color.white);
               if (xe != xs || ye != ys)
                  drawWhiteRectangle(g); // Remove old rectangle:
               xe = e.getX(); ye = e.getY();
               drawWhiteRectangle(g); // Draw new rectangle:
            }
         }
      });
   }

   /*
   public void paint(Graphics g) {
      w = getSize().width; h = getSize().height; 
      r = w / h; // Aspect ratio, used in mouseReleased
      factor = Math.max((maxRe - minRe) / w, (maxIm - minIm) / h);
      for (int yPix = 0; yPix < h; ++yPix) {
         double cIm = minIm + yPix * factor;
         for (int xPix = 0; xPix < w; ++xPix) {
            double cRe = minRe + xPix * factor, x = cRe, y = cIm;
            int nMax = 100, n;
            for (n = 0; n < nMax; ++n) {
               double x2 = x * x, y2 = y * y;
               if (x2 + y2 > 4) break; // Outside
               y = 2 * x * y + cIm;
               x = x2 - y2 + cRe;
           }
           g.setColor(n == nMax ? Color.black            // Inside
               : new Color(100 + 155 * n / nMax, 0, 0)); // Outside
           g.drawLine(xPix, yPix, xPix, yPix);
         }
      }
   }
   */
   //Version of paint for Julia set:
   @Override
public void paint(Graphics g) {
      Dimension d = getSize(); 
      w = getSize().width;
      h = getSize().height;
      r = w/h;
      double cRe = -0.76, cIm = 0.084; 
      factor = Math.max((maxRe - minRe)/w, (maxIm - minIm)/h);
      for(int yPix=0; yPix<h; ++yPix) {
         for(int xPix=0; xPix<w; ++xPix) {
            double x = minRe + xPix * factor,
                   y = minIm + yPix * factor;
            int nMax = 100, n;
            for (n=0; n<nMax; ++n) {
               double x2 = x * x, y2 = y * y;
               if (x2 + y2 > 4)
                  break;   // Outside
               y = 2 * x * y + cIm;
               x = x2 - y2 + cRe;
            }
            g.setColor(n == nMax ? Color.black             // Inside
                 : new Color(100 + 155 * n / nMax, 0, 0)); // Outside
            g.drawLine(xPix, yPix, xPix, yPix);
         }
      }
   }

}