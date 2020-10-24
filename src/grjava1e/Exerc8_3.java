package grjava1e;
// Exerc8_3.java

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Exerc8_3 extends Frame {
   public static void main(String[] args){new Exerc8_3();}
   Exerc8_3() {
      super("Exerc8_3. Click left or right mouse button to change the level");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(800, 600);
      add("Center", new CvRealTree());
      setVisible(true);
   }
}

class CvRealTree extends Canvas
{  int maxX, maxY, level = 1; 
   double xLast, yLast, rLast, dir;
  
// Tree: (X, FF, nil, F[+X]F[-X]+X, nil, 22.5)
   String axiom ="X",
          strF = "FF",      // Move forward twice, while drawing
          strf = "",
          strX = "F[+X]F[-X]+X",
          strY = "";
   double rotation = 22.5;

   double dirStart = 90;
   double fxStart = 0.5;  // Start position relative to window width and height
   double fyStart = 0.05; 
   double xStart, yStart;  // Start position depending on screen dimensions
   double lengthFactor = 0.45; // initial fraction of window height used for length
   double reductionFactor = 0.5; 

   Graphics g;
   int iX(double x){return (int)Math.round(x);}
   int iY(double y){return (int)Math.round(maxY-y);}

   CvRealTree() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
               level--;      // Right mouse button pressed
               if (level < 1)
                  level = 1;
            }
            else        // Left mouse button pressed
               level++; // each mouse click increase a level
            repaint();
         }
      });
   }

   double rFun(double x, double y) {
      double hScr = getSize().height, halfWScr = getSize().width;
      double horDistFromStem = Math.abs(x - xStart);
      return ((hScr - y) + (halfWScr - horDistFromStem))/2000;
   }
   
   
   void drawTo(Graphics g, double x, double y) {
      double r = rLast * 0.9; 
      double dx = x - xLast, dy = y - yLast;
      double h = rLast * dy, v = rLast * dx, h1 = r * dy, v1 = r * dx;
      double [] xPol = {xLast + h, x + h1, x - h1, xLast - h},
                yPol = {yLast - v, y - v1, y + v1, yLast + v};
      int xDev[] = new int[4], yDev[] = new int[4];
      for (int i=0; i<4; ++i) {
         xDev[i] = iX(xPol[i]);
         yDev[i] = iY(yPol[i]);
      }
      g.fillPolygon(xDev, yDev, 4);
      xLast = x;
      yLast = y;
      rLast = r;
   }
   
   void moveTo(Graphics g, double x, double y) {
      xLast = x;
      yLast = y;
   }

   @Override
public void paint(Graphics g) {
      Dimension d = getSize();
      maxX = d.width - 1;
      maxY = d.height - 1; 
      double length0 = //lengthFactor 
            0.5 * maxY; // * 0.4;
      xLast = xStart = fxStart * maxX;
      yLast = yStart = fyStart * maxY;
      rLast = rFun(xLast, yLast);
      dir = dirStart;         // Initial direction in degrees
      turtleGraphics(g, axiom, level, length0);  // Axiom and iteration depth
   }
   
   public void turtleGraphics(Graphics g, String instruction, int depth, double length0) {
      double xMark=0, yMark=0, dirMark=0, rMark=0;
      for (int i=0; i<instruction.length(); i++) {
         char ch = instruction.charAt(i);
         double length = length0; 
         switch(ch) {
         case 'F':   // Step forward
            // Draw starting at (xLast, yLast) in direction dir, with steplength 'length'
            if (depth == 0) {
               double rad = Math.PI/180 * dir, // Degrees -> radians
                  dx = length * Math.cos(rad), dy = length * Math.sin(rad);
               drawTo(g, xLast + dx, yLast + dy);
            }
            else
               turtleGraphics(g, strF, depth - 1, reductionFactor * length); 
            break;
         case 'f':
            // Draw starting at (xLast, yLast) in direction dir, with steplength 'length'
            if (depth == 0) {
               double rad = Math.PI/180 * dir, // Degrees -> radians
                  dx = length * Math.cos(rad), dy = length * Math.sin(rad);
               moveTo(g, xLast + dx, yLast + dy);
            }
            else
               turtleGraphics(g, strf, depth - 1, reductionFactor * length); 
            break;
         case 'X':
               if (depth > 0)
                  turtleGraphics(g, strX, depth - 1, reductionFactor * length);
               break;
         case 'Y':
               if (depth > 0)
                  turtleGraphics(g, strY, depth - 1, reductionFactor * length);
               break;
         case '+': // Turn right
            dir -= rotation; break;
         case '-': // Turn left
            dir += rotation; break;
         case '[':      // Save position and direction
            xMark = xLast; 
            yMark = yLast; 
            dirMark = dir;
            rMark = rLast;
            break;
         case ']': // Back to saved position and direction
            xLast = xMark;
            yLast = yMark;
            dir = dirMark;
            rLast = rMark;
            break;
         }
      }
   }
}

