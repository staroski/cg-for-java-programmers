package grjava2e;
// ArrowPt.java: Arrow rotated through 30 degrees
//    about a point selected by the user.
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ArrowPt extends Frame {
   public static void main(String[] args) {
      new ArrowPt();
   }

   ArrowPt() {
      super("Arrow rotated about arbitrary point");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(500, 300);
      add("Center", new CvArrowPt());
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
   }
}

class CvArrowPt extends Canvas {
   int centerX, centerY, currentX, currentY;
   float pixelSize, xP = 1e9F, yP, rWidth = 100.0F, rHeight = 100.0F;
   float[] x = {0, 0, -2, 2}, y = {-7, 7, 0, 0};

   CvArrowPt() {
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

   void moveTo(float x, float y) {
      currentX = iX(x); currentY = iY(y);
   }

   void lineTo(Graphics g, float x, float y) {
      int x1 = iX(x), y1 = iY(y);
      g.drawLine(currentX, currentY, x1, y1);
      currentX = x1; currentY = y1;
   }

   void drawArrow(Graphics g, float[] x, float[] y) {
      moveTo(x[0], y[0]);
      lineTo(g, x[1], y[1]);
      lineTo(g, x[2], y[2]);
      lineTo(g, x[3], y[3]);
      lineTo(g, x[1], y[1]);
   }

   @Override
public void paint(Graphics g) {
      initgr();
      // Show initial arrow:
      drawArrow(g, x, y);
      if (xP > 1e8F)
         return;
      float phi = (float) (Math.PI / 6), 
            c = (float) Math.cos(phi), s = (float) Math.sin(phi), 
            r11 = c, r12 = s, 
            r21 = -s, r22 = c, 
            r31 = -xP * c + yP * s + xP, r32 = -xP * s - yP * c + yP;
      for (int j = 0; j < 4; j++) {
         float xNew = x[j] * r11 + y[j] * r21 + r31, 
               yNew = x[j] * r12 + y[j] * r22 + r32;
         x[j] = xNew; y[j] = yNew;
      }
      // Arrow after rotation:
      drawArrow(g, x, y);
   }
}
