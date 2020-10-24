package grjava3e;
// Tools2D.java: Class to be used in other program files.
// Uses: Point2D (Section 1.5).

class Tools2D {
   static float area2(Point2D a, Point2D b, Point2D c) {
      return (a.x - c.x) * (b.y - c.y) - (a.y - c.y) * (b.x - c.x);
   }
   
   static float distance2(Point2D p, Point2D q) {
      float dx = p.x - q.x, dy = p.y - q.y;
      return dx * dx + dy * dy;
   }

   static boolean insideTriangle(Point2D a, Point2D b, Point2D c,
         Point2D p){ // ABC is assumed to be counter-clockwise
      return area2(a, b, p) >= 0 && 
             area2(b, c, p) >= 0 && 
             area2(c, a, p) >= 0;
   }
/*   
   static boolean intersect(Point2D a, Point2D b, Point2D p, Point2D q){
      // Do P and Q lie on the different sides of the line through
      // A and B, while A and B lie on the different sides of the 
      // line through P and Q?
      return area2(a, b, p) * area2(a, b, q) <= 0 && 
          area2(p, q, a) * area2(p, q, b) <= 0;
   }  
*/
   static Point2D circumcenter(Point2D a, Point2D b, Point2D c) {
      // Does p lie inside the circle through the points a, b and c?
      // We compute the circle center as the point of intersection of
      // the perpendicular bisectors of (a, b) and (b, c).
      float u1 = b.x - a.x, u2 = b.y - a.y, 
            v1 = -u2, v2 = u1, 
            w1 = c.x - b.x, w2 = c.y - b.y, 
            t1 = -w2, t2 = w1;
      // We now solve the following system of linear equations for lambda:
      // a.x + 0.5 * u1 + v1 * lambda = b.x + 0.5 * w1 + t1 * mu
      // a.y + 0.5 * u2 + v2 * lambda = b.y + 0.5 * w2 + t2 * mu
      // The left-hand sides respresent the perpendicular bisector of AB and
      // the right-hand side that of BC.
      // Multiplying the first equation by t2 and the second by t1 and
      // subtracting, we eliminate mu and can express lambda in known values
      // as follows:
      float lambda = (t2 * (b.x - a.x) - t1 * (b.y - a.y) + 0.5F * t2 * (w1 - u1) - 0.5F * t1 * (w2 - u2)) / (t2 * v1 - t1 * v2);
      // Substituting this in the left-hand sides of the above two equations, we
      // find the point of intersection (center) of the bisectors discussed
      // above:
      return new Point2D(a.x + 0.5F * u1 + v1 * lambda,
                         a.y + 0.5F * u2 + v2 * lambda);   
   }  
/*   
   static boolean pointInCircumcircle(Point2D p, Point2D a, Point2D b,
         Point2D c) {
      // Does p lie inside the circle through the points a, b and c?
      Point2D center = circumcenter(a, b, c);
      return distance2(p, center) <= Tools2D.distance2(a, center);
   }
*/   
}

