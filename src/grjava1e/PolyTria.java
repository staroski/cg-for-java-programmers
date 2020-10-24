package grjava1e;
// PolyTria.java: Drawing a polygon and dividing it into triangles.
// Uses: CvDefPoly, Point2D (Section 1.5),
//       Tria, Tools2D (discussed above).

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PolyTria extends Frame {
   public static void main(String[] args) {new PolyTria();}

   PolyTria() {
      super("Define polygon vertices by clicking");
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      setSize(1500, 900);
      add("Center", new CvPolyTria());
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      setVisible(true);
   }
}

class CvPolyTria extends CvDefPoly { // see Section 1.5   
   @Override
public void paint(Graphics g) {
      int n = v.size(); // v is defined in superclass CvDefPoly
      if (n >= 3 && ready) {
         Point2D[] p = new Point2D[n];
         for (int i = 0; i < n; i++)
            p[i] = v.elementAt(i);
         // If not counter-clockwise, reverse the order:
         if (!ccw(p))
            for (int i = 0; i < n; i++)
               p[i] = v.elementAt(n - i - 1);
         Polygon2D polygon = new Polygon2D(p); 
         Tria[] t = polygon.triangulate();
         initgr();
         if (t != null) {
            for (int j = 0; j < t.length; j++) {
               g.setColor(new Color(rand(), rand(), rand()));
               int iA = t[j].iA, iB = t[j].iB, iC = t[j].iC;
               int[] x = new int[3], y = new int[3];
               x[0] = iX(p[iA].x); y[0] = iY(p[iA].y);
               x[1] = iX(p[iB].x); y[1] = iY(p[iB].y);
               x[2] = iX(p[iC].x); y[2] = iY(p[iC].y);
               g.fillPolygon(x, y, 3);
               Point2D a = new Point2D(x[0], y[0]), b = new Point2D(x[1], y[1]), c = new Point2D(x[2], y[2]); 
               /* 
                // Demo Delaunay:circumcircles
                
               Point2D center = Tools2D.circumcenter(a, b, c);
               float radius = (float)Math.sqrt(Tools2D.distance2(a,  center));
               g.drawOval((int)(center.x - radius), (int)(center.y - radius), (int)(2 * radius), (int)(2 * radius));

               */
               
               
               
            }
         }
      }
      g.setColor(Color.black);
      super.paint(g);
   }

   int rand() {return (int) (Math.random() * 256);}

   static boolean ccw(Point2D[] p) {
      int n = p.length, k = 0;
      for (int i = 1; i < n; i++)
         if (p[i].x <= p[k].x && (p[i].x < p[k].x || p[i].y < p[k].y))
            k = i;
      // p[k] is a convex vertex.
      int prev = k - 1, next = k + 1;
      if (prev == -1) prev = n - 1;
      if (next == n) next = 0;
      return Tools2D.area2(p[prev], p[k], p[next]) > 0;
   }
}