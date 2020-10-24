package grjava3e;
// CvZBuf.java: Canvas class for ZBuf.java.
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

class CvZBuf extends Canvas3D {
   private int maxX, maxY, centerX, centerY, 
      maxX0 = -1, maxY0 = -1;
   private float buf[][];
   private Obj3D obj;
   private Point2D imgCenter;

   int iX(float x) {
      return Math.round(centerX + x - imgCenter.x);
   }

   int iY(float y) {
      return Math.round(centerY - y + imgCenter.y);
   }

   @Override
Obj3D getObj() {
      return obj;
   }

   @Override
void setObj(Obj3D obj) {
      this.obj = obj;
   }

   @Override
public void paint(Graphics g) {
      if (obj == null)
         return;
      obj.setSpecular(specularDesired); 
         // specularDesired defined in Canvas3D
      Vector<Polygon3D> polyList = obj.getPolyList();
      if (polyList == null)
         return;
      int nFaces = polyList.size();
      if (nFaces == 0)
         return;
      float xe, ye, ze;

      Dimension dim = getSize();
      maxX = dim.width - 1;
      maxY = dim.height - 1;
      centerX = maxX / 2;
      centerY = maxY / 2;
      // ze-axis towards eye, so ze-coordinates of
      // object points are all negative. Since screen
      // coordinates x and y are used to interpolate for
      // the z-direction, we have to deal with 1/z instead
      // of z. With negative z, a small value of 1/z means
      // a small value of |z| for a nearby point. We there-
      // fore begin with large buffer values 1e30:
      if (maxX != maxX0 || maxY != maxY0) {
         buf = new float[dim.width][dim.height];
         maxX0 = maxX;
         maxY0 = maxY;
      }
      for (int iy = 0; iy < dim.height; iy++)
         for (int ix = 0; ix < dim.width; ix++)
            buf[ix][iy] = 1e30F;

      obj.eyeAndScreen(dim);
      imgCenter = obj.getImgCenter();
      obj.planeCoeff(); // Compute a, b, c and h.
      Point3D[] e = obj.getE();
      Point2D[] vScr = obj.getVScr();

      for (int j = 0; j < nFaces; j++) {
         Polygon3D pol = polyList.elementAt(j);
         if (pol.getNrs().length < 3 || pol.getH() >= 0)
            continue;
         int cCode = obj.colorCodePhong(
            pol.getA(), pol.getB(), pol.getC()); 
         g.setColor(new Color(cCode)); 

         pol.triangulate(obj);

         Tria[] t = pol.getT();
         for (int i = 0; i < t.length; i++) {
            Tria tri = t[i];
            int iA = tri.iA, iB = tri.iB, iC = tri.iC;
            Point2D a = vScr[iA], b = vScr[iB], c = vScr[iC];
            double zAi = 1 / e[iA].z, zBi = 1 / e[iB].z,
                  zCi = 1 / e[iC].z;
            // We now compute the coefficients a, b and c
            // (written here as aa, bb and cc)
            // of the imaginary plane ax + by + czi = h,
            // where zi is 1/z (and x, y and z are
            // eye coordinates. Then we compute
            // the partial derivatives dzdx and dzdy:
            double u1 = b.x - a.x, v1 = c.x - a.x, 
			       u2 = b.y - a.y, v2 = c.y - a.y, 
				   cc = u1 * v2 - u2 * v1;
            if (cc <= 0)
               continue;
            double xA = a.x, yA = a.y, xB = b.x, yB = b.y, 
			   xC = c.x, yC = c.y, xD = (xA + xB + xC) / 3,
               yD = (yA + yB + yC) / 3, 
			   zDi = (zAi + zBi + zCi) / 3,
               u3 = zBi - zAi, v3 = zCi - zAi,
               aa = u2 * v3 - u3 * v2, bb = u3 * v1 - u1 * v3,
               dzdx = -aa / cc, dzdy = -bb / cc,
               yBottomR = Math.min(yA, Math.min(yB, yC)),
               yTopR = Math.max(yA, Math.max(yB, yC));
            int yBottom = (int) Math.ceil(yBottomR),
                yTop = (int) Math.floor(yTopR);

            for (int y = yBottom; y <= yTop; y++) { 
			   // Compute horizontal line segment (xL, xR)
			   // for coordinate y:
               double xI, xJ, xK, xI1, xJ1, xK1, xL, xR;
               xI = xJ = xK = 1e30;
               xI1 = xJ1 = xK1 = -1e30;
               if ((y - yB) * (y - yC) <= 0 && yB != yC)
                  xI = xI1 = xC + (y-yC)/(yB-yC) * (xB-xC);
               if ((y - yC) * (y - yA) <= 0 && yC != yA)
                  xJ = xJ1 = xA + (y-yA)/(yC-yA) * (xC-xA);
               if ((y - yA) * (y - yB) <= 0 && yA != yB)
                  xK = xK1 = xB + (y-yB)/(yA-yB) * (xA-xB);
               // xL = xR = xI;
               xL = Math.min(xI, Math.min(xJ, xK));
               xR = Math.max(xI1, Math.max(xJ1, xK1));
               int iy = iY(y), iXL = iX((float) (xL + 0.5)),
                     iXR = iX((float) (xR - 0.5));
               double zi = 
			      1.01* zDi + (y-yD)*dzdy + (xL-xD)*dzdx;
               /*
                  for (int x=iXL; x<=iXR; x++) { 
				     if (zi < buf[x][iy]) { // < is nearer  
					    g.drawLine(x, iy, x, iy); 
						buf[x][iy] = (float)zi; 
				     }
                     zi += dzdx; }
               */
               // The above comment code is optimized below:
               // ---
               boolean leftmostValid = false;
               int xLeftmost = 0;
               for (int ix = iXL; ix <= iXR; ix++) {
                  if (zi < buf[ix][iy]) { // < means nearer
                     if (!leftmostValid) {
                        xLeftmost = ix;
                        leftmostValid = true;
                     }
                     buf[ix][iy] = (float) zi;
                  } else 
				  if (leftmostValid) {
                     g.drawLine(xLeftmost, iy, ix - 1, iy);
                     leftmostValid = false;
                  }
                  zi += dzdx;
               }
               if (leftmostValid)
                  g.drawLine(xLeftmost, iy, iXR, iy);
               // ---
            }
         }
      }
   }
}
