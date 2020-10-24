package grjava3e;
// CvDefPoly.java: To be used in other program files.

// Copied from Section 1.5 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

// A class that enables the user to define
// a polygon by clicking the mouse.
// Uses: Point2D (discussed below).
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

class CvDefPoly extends Canvas {
   Vector<Point2D> v = new Vector<Point2D>();
   float x0, y0, rWidth = 10.0F, rHeight = 7.5F, pixelSize;
   boolean ready = true;
   int centerX, centerY;

   CvDefPoly() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            float xA = fx(evt.getX()), yA = fy(evt.getY());
            if (ready) {
               v.removeAllElements();
               x0 = xA; y0 = yA;
               ready = false;
            }
            float dx = xA - x0, dy = yA - y0;
            if (v.size() > 0 && 
               dx * dx + dy * dy < 20 * pixelSize * pixelSize)
               // Previously 4 instead of 20 .........................
               ready = true;
            else
               v.addElement(new Point2D(xA, yA));
            
            
            // Added December 2016:
            if(evt.getModifiers()==InputEvent.BUTTON3_MASK) {
               ready = true;
            }
            
            
            
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

   int iX(float x) {return Math.round(centerX + x / pixelSize);}
   int iY(float y) {return Math.round(centerY - y / pixelSize);}
   float fx(int x) {return (x - centerX) * pixelSize;}
   float fy(int y) {return (centerY - y) * pixelSize;}

   @Override
public void paint(Graphics g) {
      initgr();
      int left = iX(-rWidth / 2), right = iX(rWidth / 2), 
          bottom = iY(-rHeight / 2), top = iY(rHeight / 2);
      g.drawRect(left, top, right - left, bottom - top);
      int n = v.size();
      if (n == 0)
         return;
      Point2D a = (v.elementAt(0));
      // Show tiny rectangle around first vertex:
      g.drawRect(iX(a.x) - 2, iY(a.y) - 2, 4, 4);
      for (int i = 1; i <= n; i++) {
         if (i == n && !ready)
            break;
         Point2D b = (v.elementAt(i % n));
         g.drawLine(iX(a.x), iY(a.y), iX(b.x), iY(b.y));
         g.drawRect(iX(b.x) - 2, iY(b.y) - 2, 4, 4); // Tiny rectangle; added
         a = b;
         g.drawString(""+(i%n), iX(b.x), iY(b.y));// to test.......
      }
   }
}
