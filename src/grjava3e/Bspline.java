package grjava3e;
// Bspline.java: B-spline curve fitting. 
// Uses: Point2D (Section 1.4).
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class Bspline extends Frame {
   public static void main(String[] args) {new Bspline();}

   Bspline() {
      super("Define points; press any key after the final one");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e){
            System.exit(0);
         }
      });
      setSize(500, 300); add("Center", new CvBspline());
      setCursor(Cursor.getPredefinedCursor(
         Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
   }
}

class CvBspline extends Canvas {
   Vector<Point2D> V = new Vector<Point2D>();
   int np = 0, centerX, centerY;
   float rWidth = 10.0F, rHeight = 7.5F, eps = rWidth / 100F,
         pixelSize;
   boolean ready = false;

   CvBspline() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            float x = fx(evt.getX()), y = fy(evt.getY());
            if (ready) {
               V.removeAllElements();
               np = 0; 
               ready = false;
            }
            V.addElement(new Point2D(x, y));
            np++; repaint();
         }
      });

      addKeyListener(new KeyAdapter() {
         @Override
        public void keyTyped(KeyEvent evt) {
            evt.getKeyChar();
            if (np >= 4) ready = true;
            repaint();
         }
      });
   }

   void initgr() {
      Dimension d = getSize();
      int maxX = d.width - 1, maxY = d.height - 1;
      pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
      centerX = maxX / 2; centerY = maxY / 2;
   }

   int iX(float x){return Math.round(centerX + x / pixelSize);}
   int iY(float y){return Math.round(centerY - y / pixelSize);}
   float fx(int x){return (x - centerX) * pixelSize;}
   float fy(int y){return (centerY - y) * pixelSize;}

   void bspline(Graphics g, Point2D[] p) {
      int m = 50, n = p.length;
      float xA, yA, xB, yB, xC, yC, xD, yD, a0, a1, a2, a3, 
	     b0, b1, b2, b3, x = 0, y = 0, x0, y0;
      boolean first = true;
      for (int i = 1; i < n - 2; i++) {
         xA = p[i-1].x; xB = p[i].x; 
		 xC = p[i+1].x; xD = p[i+2].x;
         yA = p[i-1].y; yB = p[i].y; 
		 yC = p[i+1].y; yD = p[i+2].y;
         a3 = (-xA + 3 * (xB - xC) + xD) / 6; 
         b3 = (-yA + 3 * (yB - yC) + yD) / 6;
         a2 = (xA - 2 * xB + xC)/2; b2 = (yA - 2 * yB + yC)/2;
         a1 = (xC - xA) / 2; b1 = (yC - yA) / 2;
         a0 = (xA + 4 * xB + xC)/6; b0 = (yA + 4 * yB + yC)/6;
         for (int j = 0; j <= m; j++) {
            x0 = x; y0 = y;
            float t = (float) j / (float) m;
            x = ((a3 * t + a2) * t + a1) * t + a0;
            y = ((b3 * t + b2) * t + b1) * t + b0;
            if (first) first = false;
            else
               g.drawLine(iX(x0), iY(y0), iX(x), iY(y));
         }
      }
   }

   @Override
public void paint(Graphics g) {
      initgr();
      int left = iX(-rWidth / 2), right = iX(rWidth / 2), 
          bottom = iY(-rHeight / 2), top = iY(rHeight / 2);
      g.drawRect(left, top, right - left, bottom - top);
      Point2D[] p = new Point2D[np]; V.copyInto(p);
      if (!ready) {
         for (int i = 0; i < np; i++) { 
            // Show tiny rectangle around point:
            g.drawRect(iX(p[i].x) - 2, iY(p[i].y) - 2, 4, 4);
            if (i > 0) // Draw line p[i-1]p[i]:
               g.drawLine(iX(p[i - 1].x), iY(p[i - 1].y), 
                          iX(p[i].x), iY(p[i].y));
         }
      }
      if (np >= 4) bspline(g, p);
   }
}
