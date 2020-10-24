package grjava3e;
// CvHLines.java: Used in the file HLines.java.

// Copied from Appendix D of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

class CvHLines extends Canvas3D {
   private int maxX, maxY, centerX, centerY, nTria, nVertices;
   private Obj3D obj;
   private Point2D imgCenter;
   private Tria[] tr;
   private HPGL hpgl;
   private int[] refPol;
   private Vector<Integer> connect[];
   private double hLimit;
   private Vector<Polygon3D> polyList;
   private float maxScreenRange;

   @Override
Obj3D getObj() {return obj;}
   @Override
void setObj(Obj3D obj) {this.obj = obj;}
   void setHPGL(HPGL hpgl) {this.hpgl = hpgl;}

   @Override
public void paint(Graphics g) {
      if (obj == null)
         return;
      Vector<Polygon3D> polyList = obj.getPolyList();
      if (polyList == null) return;
      int nFaces = polyList.size();
      if (nFaces == 0) return;
      Dimension dim = getSize();
      maxX = dim.width - 1; maxY = dim.height - 1;
      centerX = maxX / 2; centerY = maxY / 2;
      // ze-axis towards eye, so ze-coordinates of
      // object points are all negative. Since screen
      // coordinates x and y are used to interpolate for
      // the z-direction, we have to deal with 1/z instead
      // of z. With negative z, a small value of 1/z means
      // a small value of |z| for a nearby point.

      // obj is a java object that contains all data,
      // with w, e and vScr parallel (with vertex numbers
      // as index values):
      // - Vector w (with Point3D elements)
      // - Array e (with Point3D elements)
      // - Array vScr (with Point2D elements)
      // - Vector polyList (with Polygon3D elements)

      // Every Polygon3D value contains:
      // - Array 'nrs' for vertex numbers (n elements)
      // - Values a, b, c, h for the plane ax+by+cz=h.
      // - Array t (with n-2 elements of type Tria)

      // Every Tria value consists of the three vertex
      // numbers A, B and C.
      maxScreenRange = obj.eyeAndScreen(dim);
      imgCenter = obj.getImgCenter();
      obj.planeCoeff(); // Compute a, b, c and h.

      hLimit = -1e-6 * obj.getRho();
      buildLineSet();

      // Construct an array of triangles in each polygon and count 
      // the total number of triangles.
      nTria = 0;
      for (int j = 0; j < nFaces; j++) {
         Polygon3D pol = polyList.elementAt(j);
         if (pol.getNrs().length > 2 && pol.getH() <= hLimit) {
            pol.triangulate(obj);
            nTria += pol.getT().length;
         }
      }
      tr = new Tria[nTria]; // Triangles of all polygons
      refPol = new int[nTria]; // tr[i] belongs to refPol[i]
      int iTria = 0;

      for (int j = 0; j < nFaces; j++) {
         Polygon3D pol = polyList.elementAt(j);
         Tria[] t = pol.getT(); // Triangles of one polygon
         if (pol.getNrs().length > 2 && pol.getH() <= hLimit) {
            for (int i = 0; i < t.length; i++) {
               Tria tri = t[i];
               tr[iTria] = tri;
               refPol[iTria++] = j;
            }
         }
      }
      Point3D[] e = obj.getE();
      Point2D[] vScr = obj.getVScr();
      for (int i = 0; i < nVertices; i++) {
         for (int j = 0; j < connect[i].size(); j++) {
            int jj = connect[i].elementAt(j).intValue();
            lineSegment(g, e[i], e[jj], vScr[i], vScr[jj], i, jj, 0);
         }
      }
      hpgl = null;
   }

   private void buildLineSet() { 
      // Build the array 'connect', where connect[i] 
      // is a Vector<Integer> containing all vertex numbers j, such that 
      // (i, connect[i].elementAt(j).intValue()) is an edge of the 3D object.
      polyList = obj.getPolyList();
      nVertices = obj.getVScr().length;
      connect = new Vector[nVertices];
      for (int i=0; i<nVertices; i++)
         connect[i] = new Vector<Integer>();
      int nFaces = polyList.size();

      for (int j = 0; j < nFaces; j++) {
         Polygon3D pol = polyList.elementAt(j);
         int[] nrs = pol.getNrs();
         int n = nrs.length;
         if (n > 2 && pol.getH() > 0) continue;
         int ii = Math.abs(nrs[n - 1]);
         for (int k = 0; k < n; k++) {
            int jj = nrs[k];
            if (jj < 0) 
               jj = -jj; // abs
            else {
               int i1 = Math.min(ii, jj), j1 = Math.max(ii, jj); 
               Integer j1Int = new Integer(j1);
               if (connect[i1].indexOf(j1Int) == -1) // Not yet present?
                  connect[i1].addElement(j1Int);                   
            }
            ii = jj;
         }
      }
   }

   int iX(float x) {return Math.round(centerX + x - imgCenter.x);}
   int iY(float y) {return Math.round(centerY - y + imgCenter.y);}

   private String toString(float t) {
   // From screen device units (pixels) to HP-GL units (0-10000):
      int i = Math.round(5000 + t * 9000 / maxScreenRange);
      String s = "";
      int n = 1000;
      for (int j = 3; j >= 0; j--) {
         s += i / n;
         i %= n;
         n /= 10;
      }
      return s;
   }

   private String hpx(float x) {return toString(x - imgCenter.x);}
   private String hpy(float y) {return toString(y - imgCenter.y);}

   private void drawLine(Graphics g, float x1, float y1, 
         float x2, float y2) {
      if (x1 != x2 || y1 != y2) {
         g.drawLine(iX(x1), iY(y1), iX(x2), iY(y2));
         if (hpgl != null) {
            hpgl.write("PU;PA" + hpx(x1) + "," + hpy(y1) + ";");
            hpgl.write("PD;PA" + hpx(x2) + "," + hpy(y2) + ";\n");
         }
      }
   }

   private void lineSegment(Graphics g, Point3D p, Point3D q,
         Point2D pScr, Point2D qScr, int iP, int iQ, int iStart) {
      double u1 = qScr.x - pScr.x, u2 = qScr.y - pScr.y;
      double minPQx = Math.min(pScr.x, qScr.x);
      double maxPQx = Math.max(pScr.x, qScr.x);
      double minPQy = Math.min(pScr.y, qScr.y);
      double maxPQy = Math.max(pScr.y, qScr.y);
      double zP = p.z, zQ = q.z; // p and q give eye-coordinates
      double minPQz = Math.min(zP, zQ);
      Point3D[] e = obj.getE();
      Point2D[] vScr = obj.getVScr();
      for (int i = iStart; i < nTria; i++) {
         Tria t = tr[i];
         int iA = Math.abs(t.iA), iB = Math.abs(t.iB), iC = Math.abs(t.iC);
         Point2D aScr = vScr[iA], bScr = vScr[iB], cScr = vScr[iC];

         // 1. Minimax test for x and y screen coordinates:
         if (maxPQx <= aScr.x && maxPQx <= bScr.x && maxPQx <= cScr.x || 
             minPQx >= aScr.x && minPQx >= bScr.x && minPQx >= cScr.x ||
             maxPQy <= aScr.y && maxPQy <= bScr.y && maxPQy <= cScr.y ||
             minPQy >= aScr.y && minPQy >= bScr.y && minPQy >= cScr.y)
            continue; // This triangle does not obscure PQ.

         // 2. Test if PQ is an edge of ABC:
         if ((iP == iA || iP == iB || iP == iC) && 
             (iQ == iA || iQ == iB || iQ == iC))
            continue; // This triangle does not obscure PQ.

         // 3. Test if PQ is clearly nearer than ABC:
         double zA = e[iA].z, zB = e[iB].z, zC = e[iC].z;
         if (minPQz >= zA && minPQz >= zB && minPQz >= zC)
            continue; // This triangle does not obscure PQ.

         // 4. Do P and Q (in 2D) lie in a half plane defined
         // by line AB, on the side other than that of C?
         // Similar for the edges BC and CA.
         double eps = 0.1; // Relative to numbers of pixels
         if (Tools2D.area2(aScr, bScr, pScr) < eps && 
             Tools2D.area2(aScr, bScr, qScr) < eps ||
             Tools2D.area2(bScr, cScr, pScr) < eps && 
             Tools2D.area2(bScr, cScr, qScr) < eps || 
             Tools2D.area2(cScr, aScr, pScr) < eps && 
             Tools2D.area2(cScr, aScr, qScr) < eps)
            continue; // This triangle does not obscure PQ.

         // 5. Test (2D) if A, B and C lie on the same side
         // of the infinite line through P and Q:
         double pqa = Tools2D.area2(pScr, qScr, aScr);
         double pqb = Tools2D.area2(pScr, qScr, bScr);
         double pqc = Tools2D.area2(pScr, qScr, cScr);

         if (pqa < +eps && pqb < +eps && pqc < +eps || 
             pqa > -eps && pqb > -eps && pqc > -eps)
            continue; // This triangle does not obscure PQ.

         // 6. Test if neither P nor Q lies behind the
         // infinite plane through A, B and C:
         int iPol = refPol[i];
         Polygon3D pol = polyList.elementAt(iPol);
         double a = pol.getA(), b = pol.getB(), c = pol.getC(), 
               h = pol.getH(), eps1 = 1e-5 * Math.abs(h), 
               hP = a * p.x + b * p.y + c * p.z, 
               hQ = a * q.x + b * q.y + c * q.z;
         if (hP > h - eps1 && hQ > h - eps1)
            continue; // This triangle does not obscure PQ.

         // 7. Test if both P and Q behind triangle ABC:
         boolean pInside = 
             Tools2D.insideTriangle(aScr, bScr, cScr, pScr);
         boolean qInside = 
             Tools2D.insideTriangle(aScr, bScr, cScr, qScr);
         if (pInside && qInside)
            return; // This triangle obscures PQ.

         // 8. If P nearer than ABC and inside, PQ visible;
         // the same for Q:
         double h1 = h + eps1;
         if (hP > h1 && pInside || hQ > h1 && qInside)
            continue; // This triangle does not obscure PQ.

         // 9. Compute the intersections I and J of PQ
         // with ABC in 2D.
         // If, in 3D, such an intersection lies in front of
         // ABC, this triangle does not obscure PQ.
         // Otherwise, the intersections lie behind ABC and
         // this triangle obscures part of PQ:
         double lambdaMin = 1.0, lambdaMax = 0.0;
         for (int ii = 0; ii < 3; ii++) {
            double v1 = bScr.x - aScr.x, v2 = bScr.y - aScr.y, 
                  w1 = aScr.x - pScr.x, w2 = aScr.y - pScr.y, 
                  denom = u2 * v1 - u1 * v2;
            if (denom != 0) {
               double mu = (u1 * w2 - u2 * w1) / denom;
               // mu = 0 gives A and mu = 1 gives B.
               if (mu > -0.0001 && mu < 1.0001) {
                  double lambda = (v1 * w2 - v2 * w1) / denom;
                  // lambda = PI/PQ
                  // (I is point of intersection)
                  if (lambda > -0.0001 && lambda < 1.0001) {
                     if (pInside != qInside && 
                         lambda > 0.0001 && lambda < 0.9999) {
                        lambdaMin = lambdaMax = lambda;
                        break; // Only one point of intersection
                     }
                     if (lambda < lambdaMin) lambdaMin = lambda;
                     if (lambda > lambdaMax) lambdaMax = lambda;
                  }
               }
            }
            Point2D temp = aScr; aScr = bScr; bScr = cScr; cScr = temp;
         }
         float d = obj.getD();
         if (!pInside && lambdaMin > 0.001) {
            double iScrx = pScr.x + lambdaMin * u1, 
                   iScry = pScr.y + lambdaMin * u2;
            // Back from screen to eye coordinates:
            double zI = 1 / (lambdaMin / zQ + (1 - lambdaMin) / zP), 
                   xI = -zI * iScrx / d, yI = -zI * iScry / d;
            if (a * xI + b * yI + c * zI > h1)
               continue; // This triangle does not obscure PQ.

            Point2D iScr = new Point2D((float) iScrx, (float) iScry);
            if (Tools2D.distance2(iScr, pScr) >= 1.0)
               lineSegment(g, p, new Point3D(xI, yI, zI), pScr, iScr,
                     iP, -1, i + 1);
         }
         if (!qInside && lambdaMax < 0.999) {
            double jScrx = pScr.x + lambdaMax * u1, 
                  jScry = pScr.y + lambdaMax * u2;
            double zJ = 1 / (lambdaMax / zQ + (1 - lambdaMax) / zP), 
                   xJ = -zJ * jScrx / d, yJ = -zJ * jScry / d;
            if (a * xJ + b * yJ + c * zJ > h1)
               continue; // This triangle does not obscure PQ.
            Point2D jScr = new Point2D((float) jScrx, (float) jScry);
            if (Tools2D.distance2(jScr, qScr) >= 1.0)
               lineSegment(g, q, new Point3D(xJ, yJ, zJ), 
                           qScr, jScr, iQ, -1, i + 1);
         }
         return;
         // if no continue-statement has been executed
      }
      drawLine(g, pScr.x, pScr.y, qScr.x, qScr.y);
      // No triangle obscures PQ.
   }
}
