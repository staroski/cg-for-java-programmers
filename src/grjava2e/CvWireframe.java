package grjava2e;
// CvWireframe.java: Canvas class for class Wireframe.

// Copied from Section 5.6 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

class CvWireframe extends Canvas3D {
   private int maxX, maxY, centerX, centerY;
   private Obj3D obj;
   private Point2D imgCenter;

   @Override
Obj3D getObj() {return obj;}
   @Override
void setObj(Obj3D obj) {this.obj = obj;}
   int iX(float x) {return Math.round(centerX + x - imgCenter.x);}
   int iY(float y) {return Math.round(centerY - y + imgCenter.y);}

   @Override
public void paint(Graphics g) {
      if (obj == null) return;
      Vector<Polygon3D> polyList = obj.getPolyList();
      if (polyList == null) return;
      int nFaces = polyList.size();
      if (nFaces == 0) return;

      Dimension dim = getSize();
      maxX = dim.width - 1; maxY = dim.height - 1;
      centerX = maxX / 2; centerY = maxY / 2;
      // ze-axis towards eye, so ze-coordinates of
      // object points are all negative.
      // obj is a java object that contains all data:
      // - Vector w (world coordinates)
      // - Array e (eye coordinates)
      // - Array vScr (screen coordinates)
      // - Vector polyList (Polygon3D objects)

      // Every Polygon3D value contains:
      // - Array 'nrs' for vertex numbers
      // - Values a, b, c, h for the plane ax+by+cz=h.
      // (- Array t (with nrs.length-2 elements of type Tria))

      obj.eyeAndScreen(dim);
      // Computation of eye and screen coordinates.

      imgCenter = obj.getImgCenter();
      obj.planeCoeff(); // Compute a, b, c and h.
      Point3D[] e = obj.getE();
      Point2D[] vScr = obj.getVScr();

      g.setColor(Color.black);

      for (int j = 0; j < nFaces; j++) {
         Polygon3D pol = polyList.elementAt(j);
         int nrs[] = pol.getNrs();
         if (nrs.length < 3)
            continue;
         for (int iA = 0; iA < nrs.length; iA++) {
            int iB = (iA + 1) % nrs.length;
            int na = Math.abs(nrs[iA]), nb = Math.abs(nrs[iB]);
            // abs in view of minus signs discussed in Section 6.4.
            Point2D a = vScr[na], b = vScr[nb];
            g.drawLine(iX(a.x), iY(a.y), iX(b.x), iY(b.y));
         }
      }
   }
}