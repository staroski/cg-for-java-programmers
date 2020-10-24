package grjava3e;
// Polygon3D.java: Polygon in 3D, represented by vertex numbers 
//                 referring to coordinates stored in an Obj3D object.
// Uses: Point2D (Section 1.5), Tria (Section 5.5.3), 
//       Obj3D (Section 5.6)
import java.util.Vector;

class Polygon3D {
   private int[] nrs;
   private double a, b, c, h;
   private Tria[] t;

   Polygon3D(Vector<Integer> vnrs) {
      int n = vnrs.size();
      nrs = new int[n];
      for (int i = 0; i < n; i++)
         nrs[i] = vnrs.elementAt(i).intValue();
   }

   int[] getNrs() {return nrs;}
   double getA() {return a;}
   double getB() {return b;}
   double getC() {return c;}
   double getH() {return h;}

   void setAbch(double a, double b, double c, double h) {
      this.a = a; this.b = b; this.c = c; this.h = h;
   }

   Tria[] getT() {return t;}

   Tria[] triangulate(Obj3D obj) {
      // Successive vertex numbers (CCW) in vector nrs.
      // Resulting triangles will be put in array t.
      Point2D[] vScr = obj.getVScr();            
      Polygon2D polygon = new Polygon2D(vScr, nrs);
      t = polygon.triangulate();
      return t;
   }
}