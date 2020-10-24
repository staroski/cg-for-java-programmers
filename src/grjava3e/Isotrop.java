package grjava3e;
// Isotrop.java: The isotropic mapping mode.
//    Origin of logical coordinate system in canvas 
//    center; positive y-axis upward.
//    Square (turned 45 degrees) just fits into canvas.
//    Mouse click displays logical coordinates of 
//    selected point.

// Copied from Section 1.4 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Isotrop extends Frame {
   public static void main(String[] args) {new Isotrop();}

   Isotrop() {
      super("Isotropic mapping mode");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(400, 300);
      add("Center", new CvIsotrop());
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
   }
}

class CvIsotrop extends Canvas {
   int centerX, centerY;
   float pixelSize, rWidth = 10.0F, rHeight = 10.0F, xP = 1000000, yP;

   CvIsotrop() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            xP = fx(evt.getX()); yP = fy(evt.getY());
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
          bottom = iY(-rHeight / 2), top = iY(rHeight / 2), 
          xMiddle = iX(0), yMiddle = iY(0);
      g.drawLine(xMiddle, bottom, right, yMiddle);
      g.drawLine(right, yMiddle, xMiddle, top);
      g.drawLine(xMiddle, top, left, yMiddle);
      g.drawLine(left, yMiddle, xMiddle, bottom);
      if (xP != 1000000)
         g.drawString("Logical coordinates of selected point: " + xP
               + " " + yP, 20, 100);
   }
}
