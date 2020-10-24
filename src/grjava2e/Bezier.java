package grjava2e;
// Bezier.java: Bezier curve segments.
// Uses: Point2D (Section 1.4).
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Bezier extends Frame {
   public static void main(String[] args) {new Bezier();}

   Bezier() {
      super("Define endpoints and control points of curve segment");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(500, 300);
      add("Center", new CvBezier());
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
   }
}

class CvBezier extends Canvas {
   Point2D[] p = new Point2D[4];
   int np = 0, centerX, centerY;
   float rWidth = 10.0F, rHeight = 7.5F, eps = rWidth / 100F,
         pixelSize;

   CvBezier() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            float x = fx(evt.getX()), y = fy(evt.getY());
            if (np == 4) np = 0;
            p[np++] = new Point2D(x, y);
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

   Point2D middle(Point2D a, Point2D b) {
      return new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2);
   }

   void bezier(Graphics g, Point2D p0, Point2D p1, Point2D p2,
         Point2D p3) {
      int x0 = iX(p0.x), y0 = iY(p0.y), 
          x3 = iX(p3.x), y3 = iY(p3.y);
      if (Math.abs(x0 - x3) <= 1 && Math.abs(y0 - y3) <= 1)
         g.drawLine(x0, y0, x3, y3);
      else {
         Point2D 
            a = middle(p0, p1), b = middle(p3, p2), c = middle(p1, p2), 
            a1 = middle(a, c), b1 = middle(b, c), c1 = middle(a1, b1);
         bezier(g, p0, a, a1, c1);
         bezier(g, c1, b1, b, p3);
      }
   }

   @Override
public void paint(Graphics g) {
      initgr();
      int left = iX(-rWidth / 2), right = iX(rWidth / 2), 
          bottom = iY(-rHeight / 2), top = iY(rHeight / 2);
      g.drawRect(left, top, right - left, bottom - top);

      for (int i = 0; i < np; i++) { 
         // Show tiny rectangle around point:
         g.drawRect(iX(p[i].x) - 2, iY(p[i].y) - 2, 4, 4);
         if (i > 0)
            // Draw line p[i-1]p[i]:
            g.drawLine(iX(p[i - 1].x), iY(p[i - 1].y), 
                       iX(p[i].x), iY(p[i].y));
      }
      if (np == 4) bezier(g, p[0], p[1], p[2], p[3]);
   }
}
