package grjava1e;
// Backface.java: A cube in perspective with back-face culling.
// Uses: Point2D (Section 1.4), Point3D (Section 3.9),
//       Tools2D (Section 2.13).

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Backface extends Frame {
   public static void main(String[] args) {new Backface();}

   Backface() {
      super("Press mouse button ...");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      add("Center", new CvBackface());
      Dimension dim = getToolkit().getScreenSize();
      setSize(dim.width / 2, dim.height / 2);
      setLocation(dim.width / 4, dim.height / 4);
      setVisible(true);
   }
}

class CvBackface extends Canvas {
   int centerX, centerY;
   ObjFaces obj = new ObjFaces();
   Color[] color = {Color.blue, Color.green, Color.cyan, 
                    Color.magenta, Color.red, Color.yellow};
   float dPhi = 0.1F;

   CvBackface() {
      addMouseListener(new MouseAdapter() {
         @Override
        public void mousePressed(MouseEvent evt) {
            obj.theta += 0.1F;
            obj.phi += dPhi;
            if (obj.phi > 2 || obj.phi < 0.3)
               dPhi = -dPhi;
            repaint();
         }
      });
   }

   int iX(float x) {return Math.round(centerX + x);}
   int iY(float y) {return Math.round(centerY - y);}

   @Override
public void paint(Graphics g) {
      Dimension dim = getSize();
      int maxX = dim.width - 1, maxY = dim.height - 1, 
            minMaxXY = Math.min(maxX, maxY);
      centerX = maxX / 2; centerY = maxY / 2;
      obj.d = obj.rho * minMaxXY / obj.objSize;
      obj.eyeAndScreen();
      Point2D[] p = new Point2D[4];
      for (int j = 0; j < 6; j++) {
         Polygon pol = new Polygon();
         Square sq = obj.f[j];
         for (int i = 0; i < 4; i++) {
            int vertexNr = sq.nr[i];
            p[i] = obj.vScr[vertexNr];
            pol.addPoint(iX(p[i].x), iY(p[i].y));
         }
         g.setColor(color[j]);
         if (Tools2D.area2(p[0], p[1], p[2]) > 0)
            g.fillPolygon(pol);
      }
   }
}

class ObjFaces { // Contains 3D object data of cube faces
   float rho, theta = 0.3F, phi = 1.3F, d;
   Point3D[] w;    // World coordinates
   Point3D[] e;    // Eye coordinates
                   // (e = wV where V is a 4 x 4 matrix)
   Point2D[] vScr; // Screen coordinates
   Square[] f;     // The six (square) faces of a cube.
   float v11, v12, v13, v21, v22, v23, v32, v33, v43, 
         // Elements of viewing matrix V.
         xe, ye, ze, objSize;

   ObjFaces() {
      w = new Point3D[8];
      e = new Point3D[8];
      vScr = new Point2D[8];
      f = new Square[6];
      // Bottom surface:
      w[0] = new Point3D(1, -1, -1);
      w[1] = new Point3D(1, 1, -1);
      w[2] = new Point3D(-1, 1, -1);
      w[3] = new Point3D(-1, -1, -1);
      // Top surface:
      w[4] = new Point3D(1, -1, 1);
      w[5] = new Point3D(1, 1, 1);
      w[6] = new Point3D(-1, 1, 1);
      w[7] = new Point3D(-1, -1, 1);
      f[0] = new Square(0, 1, 5, 4); // Front
      f[1] = new Square(1, 2, 6, 5); // Right
      f[2] = new Square(2, 3, 7, 6); // Back
      f[3] = new Square(3, 0, 4, 7); // Left
      f[4] = new Square(4, 5, 6, 7); // Top
      f[5] = new Square(0, 3, 2, 1); // Bottom
      objSize = (float) Math.sqrt(12F);
         // distance between two opposite vertices.
      rho = 3 * objSize; // For reasonable perspective effect
   }

   void initPersp() {
      float costh = (float) Math.cos(theta), 
            sinth = (float) Math.sin(theta), 
            cosph = (float) Math.cos(phi),
            sinph = (float) Math.sin(phi);
      v11 = -sinth; v12 = -cosph * costh; v13 = sinph * costh;
      v21 = costh;  v22 = -cosph * sinth; v23 = sinph * sinth;
                    v32 = sinph;          v33 = cosph;
                                          v43 = -rho;
   }

   void eyeAndScreen() {
      initPersp();
      for (int i = 0; i < 8; i++) {
         Point3D p = w[i];
         float x = v11 * p.x + v21 * p.y;
         float y = v12 * p.x + v22 * p.y + v32 * p.z;
         float z = v13 * p.x + v23 * p.y + v33 * p.z + v43;
         Point3D Pe = e[i] = new Point3D(x, y, z);
         vScr[i] = new Point2D(-d * Pe.x / Pe.z, -d * Pe.y / Pe.z);
      }
   }
}

class Square {
   int nr[];
   Square(int iA, int iB, int iC, int iD) {
      nr = new int[4];
      nr[0] = iA; nr[1] = iB; nr[2] = iC; nr[3] = iD;
   }
}
