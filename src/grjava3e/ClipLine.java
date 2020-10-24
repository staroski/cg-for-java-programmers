package grjava3e;
// ClipLine.java: Cohen-Sutherland line clipping.
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClipLine extends Frame {
   public static void main(String[] args) {new ClipLine();}

   ClipLine() {
      super("Click on two opposite corners of a rectangle");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(500, 300);
      add("Center", new CvClipLine());
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
   }
}

class CvClipLine extends Canvas {
   float xmin, xmax, ymin, ymax, rWidth = 10.0F, rHeight = 7.5F,
         pixelSize;
   int maxX, maxY, centerX, centerY, np = 0;

   CvClipLine() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            float x = fx(evt.getX()), y = fy(evt.getY());
            if (np == 2) np = 0;
            if (np == 0) {xmin = x; ymin = y;
            } 
            else {
               xmax = x; ymax = y;
               if (xmax < xmin) {
                  float t = xmax; xmax = xmin; xmin = t;
               }
               if (ymax < ymin) {
                  float t = ymax; ymax = ymin; ymin = t;
               }
            }
            np++;
            repaint();
         }
      });
   }

   void initgr() {
      Dimension d = getSize();
      maxX = d.width - 1; maxY = d.height - 1;
      pixelSize = Math.max(rWidth / maxX, rHeight / maxY);
      centerX = maxX / 2; centerY = maxY / 2;
   }

   
   int iX(float x) {return Math.round(centerX + x / pixelSize);}
   int iY(float y) {return Math.round(centerY - y / pixelSize);}
   float fx(int x) {return (x - centerX) * pixelSize;}
   float fy(int y) {return (centerY - y) * pixelSize;}

   void drawLine(Graphics g, float xP, float yP, float xQ, float yQ) {
      g.drawLine(iX(xP), iY(yP), iX(xQ), iY(yQ));
   }

   int clipCode(float x, float y) {
      return (x < xmin ? 8 : 0) | (x > xmax ? 4 : 0) | 
             (y < ymin ? 2 : 0) | (y > ymax ? 1 : 0);
   }

   void clipLine(Graphics g, float xP, float yP, float xQ, float yQ,
         float xmin, float ymin, float xmax, float ymax) {
      int cP = clipCode(xP, yP), cQ = clipCode(xQ, yQ);
      float dx, dy;
      while ((cP | cQ) != 0) {
         if ((cP & cQ) != 0) return;
         dx = xQ - xP; dy = yQ - yP;
         if (cP != 0) {
            if ((cP & 8) == 8) {yP += (xmin - xP) * dy / dx; xP = xmin;} 
            else 
            if ((cP & 4) == 4) {yP += (xmax - xP) * dy / dx; xP = xmax;} 
            else 
            if ((cP & 2) == 2) {xP += (ymin - yP) * dx / dy; yP = ymin;} 
            else 
            if ((cP & 1) == 1) {xP += (ymax - yP) * dx / dy; yP = ymax;}
            cP = clipCode(xP, yP);
         } 
         else if (cQ != 0) {
            if ((cQ & 8) == 8) {yQ += (xmin - xQ) * dy / dx; xQ = xmin;} 
            else 
            if ((cQ & 4) == 4) {yQ += (xmax - xQ) * dy / dx; xQ = xmax;} 
            else 
            if ((cQ & 2) == 2) {xQ += (ymin - yQ) * dx / dy; yQ = ymin;} 
            else 
            if ((cQ & 1) == 1) {xQ += (ymax - yQ) * dx / dy; yQ = ymax;}
            cQ = clipCode(xQ, yQ);
         }
      }
      drawLine(g, xP, yP, xQ, yQ);
   }

   @Override
public void paint(Graphics g) {
      initgr();
      if (np == 1) { // Draw horizontal and vertical lines through
                     // first defined point:
         drawLine(g, fx(0), ymin, fx(maxX), ymin);
         drawLine(g, xmin, fy(0), xmin, fy(maxY));
      } else 
      if (np == 2) { // Draw rectangle:
         drawLine(g, xmin, ymin, xmax, ymin);
         drawLine(g, xmax, ymin, xmax, ymax);
         drawLine(g, xmax, ymax, xmin, ymax);
         drawLine(g, xmin, ymax, xmin, ymin);

         // Draw 20 concentric regular pentagons, as
         // far as they lie within the rectangle:
         float rMax = Math.min(rWidth, rHeight) / 2, 
               deltaR = rMax / 20, dPhi = (float) (0.4 * Math.PI);

         for (int j = 1; j <= 20; j++) {
            float r = j * deltaR;
            // Draw a pentagon:
            float xA, yA, xB = r, yB = 0;

            for (int i = 1; i <= 5; i++) {
               float phi = i * dPhi;
               xA = xB; yA = yB;
               xB = (float) (r * Math.cos(phi));
               yB = (float) (r * Math.sin(phi));
               clipLine(g, xA, yA, xB, yB, xmin, ymin, xmax, ymax);
            }
         }
      }
   }
}
