package grjava2e;
// Rota3D.java: Class used in other program files
//    for rotations about an arbitrary axis.
// Uses: Point3D (discussed above).
class Rota3D {
   static double r11, r12, r13, r21, r22, r23,
                 r31, r32, r33, r41, r42, r43;

/* The method initRotate computes the general rotation matrix

            | r11  r12  r13  0 |
       R =  | r21  r22  r23  0 |
            | r31  r32  r33  0 |
            | r41  r42  r43  1 |

   to be used as [x1  y1  z1  1] = [x  y  z  1] R
   by the method 'rotate'.
   Point (x1, y1, z1) is the image of (x, y, z).
   The rotation takes place about the directed axis  
   AB and through the angle alpha.
*/
   static void initRotate(Point3D a, Point3D b, double alpha) {
      double v1 = b.x - a.x, v2 = b.y - a.y, v3 = b.z - a.z, 
             theta = Math.atan2(v2, v1), 
             phi = Math.atan2(Math.sqrt(v1 * v1 + v2 * v2), v3);
      initRotate(a, theta, phi, alpha);
   }

   static void initRotate(Point3D a, double theta, double phi,
         double alpha) {
      double cosAlpha, sinAlpha, cosPhi, sinPhi, cosTheta, sinTheta, 
             cosPhi2, sinPhi2, cosTheta2, sinTheta2, c, 
             a1 = a.x, a2 = a.y, a3 = a.z;
      cosPhi = Math.cos(phi); sinPhi = Math.sin(phi);
      cosPhi2 = cosPhi * cosPhi; sinPhi2 = sinPhi * sinPhi;
      cosTheta = Math.cos(theta); sinTheta = Math.sin(theta);
      cosTheta2 = cosTheta * cosTheta; sinTheta2 = sinTheta * sinTheta;
      cosAlpha = Math.cos(alpha); sinAlpha = Math.sin(alpha);
      c = 1.0 - cosAlpha;
      r11 = cosTheta2 * (cosAlpha * cosPhi2 + sinPhi2) 
            + cosAlpha * sinTheta2;
      r12 = sinAlpha * cosPhi + c * sinPhi2 * cosTheta * sinTheta;
      r13 = sinPhi * (cosPhi * cosTheta * c - sinAlpha * sinTheta);
      r21 = sinPhi2 * cosTheta * sinTheta * c - sinAlpha * cosPhi;
      r22 = sinTheta2 * (cosAlpha * cosPhi2 + sinPhi2)
            + cosAlpha * cosTheta2;
      r23 = sinPhi * (cosPhi * sinTheta * c + sinAlpha * cosTheta);
      r31 = sinPhi * (cosPhi * cosTheta * c + sinAlpha * sinTheta);
      r32 = sinPhi * (cosPhi * sinTheta * c - sinAlpha * cosTheta);
      r33 = cosAlpha * sinPhi2 + cosPhi2;
      r41 = a1 - a1 * r11 - a2 * r21 - a3 * r31;
      r42 = a2 - a1 * r12 - a2 * r22 - a3 * r32;
      r43 = a3 - a1 * r13 - a2 * r23 - a3 * r33;
   }

   static Point3D rotate(Point3D p) {
      return new Point3D(
            p.x * r11 + p.y * r21 + p.z * r31 + r41,
            p.x * r12 + p.y * r22 + p.z * r32 + r42,
            p.x * r13 + p.y * r23 + p.z * r33 + r43);
   }
}
