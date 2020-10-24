package grjava2e;
// Triangles.java: This program draws 50 triangles inside each other.
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Triangles extends Frame {
   public static void main(String[] args) {new Triangles();}

   Triangles() {
      super("Triangles: 50 triangles inside each other");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(600, 400);
      add("Center", new CvTriangles());
      setVisible(true);
   }
}

class CvTriangles extends Canvas {
   int maxX, maxY, minMaxXY, xCenter, yCenter;

   void initgr() {
      Dimension d = getSize();
      maxX = d.width - 1; maxY = d.height - 1;
      minMaxXY = Math.min(maxX, maxY);
      xCenter = maxX / 2; yCenter = maxY / 2;
   }

   int iX(float x) {return Math.round(x);}

   int iY(float y) {return maxY - Math.round(y);}

   @Override
public void paint(Graphics g) {
      initgr();
      float side = 0.95F * minMaxXY, sideHalf = 0.5F * side, 
            h = sideHalf * (float) Math.sqrt(3), 
            xA, yA, xB, yB, xC, yC, xA1, yA1, xB1, yB1, xC1, yC1, p, q;
      q = 0.05F; p = 1 - q;
      xA = xCenter - sideHalf; yA = yCenter - 0.5F * h;
      xB = xCenter + sideHalf; yB = yA;
      xC = xCenter; yC = yCenter + 0.5F * h;
      for (int i = 0; i < 50; i++) {
         g.drawLine(iX(xA), iY(yA), iX(xB), iY(yB));
         g.drawLine(iX(xB), iY(yB), iX(xC), iY(yC));
         g.drawLine(iX(xC), iY(yC), iX(xA), iY(yA));
         xA1 = p * xA + q * xB; yA1 = p * yA + q * yB;
         xB1 = p * xB + q * xC; yB1 = p * yB + q * yC;
         xC1 = p * xC + q * xA; yC1 = p * yC + q * yA;
         xA = xA1; xB = xB1; xC = xC1;
         yA = yA1; yB = yB1; yC = yC1;
      }
   }
}
