package grjava1e;
// Bresenham.java: Bresenham algorithms for lines and circles
//                 demonstrated by using superpixels.

// Copied from Appendix F (solution to Exercise 4.3) of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Bresenham extends Frame {
   public static void main(String[] args) {new Bresenham();}

   Bresenham() {
      super("Bresenham");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(340, 230);
      add("Center", new CvBresenham());
      setVisible(true);
   }
}

class CvBresenham extends Canvas {
   float rWidth = 10.0F, rHeight = 7.5F, pixelSize;
   int centerX, centerY, dGrid = 10, maxX, maxY;

   void initgr() {
      Dimension d;
      d = getSize();
      maxX = d.width - 1; maxY = d.height - 1;
      pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
      centerX = maxX / 2; centerY = maxY / 2;
   }

   int iX(float x) {return Math.round(centerX + x / pixelSize);}
   int iY(float y) {return Math.round(centerY - y / pixelSize);}

   void putPixel(Graphics g, int x, int y) {
      int x1 = x * dGrid, y1 = y * dGrid, h = dGrid / 2;
      g.drawOval(x1 - h, y1 - h, dGrid, dGrid);
   }

   void drawLine(Graphics g, int xP, int yP, int xQ, int yQ) {
      int x = xP, y = yP, D = 0, HX = xQ - xP, HY = yQ - yP, c, M, 
          xInc = 1, yInc = 1;
      if (HX < 0) {xInc = -1; HX = -HX;}
      if (HY < 0) {yInc = -1; HY = -HY;}
      if (HY <= HX) {
         c = 2 * HX; M = 2 * HY;
         for (;;) {
            putPixel(g, x, y);
            if (x == xQ) break;
            x += xInc;
            D += M;
            if (D > HX) {y += yInc; D -= c;}
         }
      } else {
         c = 2 * HY; M = 2 * HX;
         for (;;) {
            putPixel(g, x, y);
            if (y == yQ) break;
            y += yInc;
            D += M;
            if (D > HY) {x += xInc; D -= c;}
         }
      }
   }

   void drawCircle(Graphics g, int xC, int yC, int r) {
      int x = 0, y = r, u = 1, v = 2 * r - 1, E = 0;
      while (x < y) {
         putPixel(g, xC + x, yC + y); // NNE
         putPixel(g, xC + y, yC - x); // ESE
         putPixel(g, xC - x, yC - y); // SSW
         putPixel(g, xC - y, yC + x); // WNW
         x++; E += u; u += 2;
         if (v < 2 * E) {y--; E -= v; v -= 2;}
         if (x > y) break;
         putPixel(g, xC + y, yC + x); // ENE
         putPixel(g, xC + x, yC - y); // SSE
         putPixel(g, xC - y, yC - x); // WSW
         putPixel(g, xC - x, yC + y); // NNW
      }
   }

   void showGrid(Graphics g) {
      for (int x = dGrid; x <= maxX; x += dGrid)
         for (int y = dGrid; y <= maxY; y += dGrid)
            g.drawLine(x, y, x, y);
   }

   @Override
public void paint(Graphics g) {
      initgr();
      showGrid(g);
      drawLine(g, 1, 1, 12, 5);
      drawCircle(g, 23, 10, 8);
   }
}