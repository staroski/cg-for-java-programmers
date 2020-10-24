package grjava2e;
// Rota3DTest.java: Rotating a cube about an axis 
//    parallel to a diagonal of its top plane.
//    Uses: Point3D, Rota3D (discussed above).
public class Rota3DTest {
   public static void main(String[] args) {
      Point3D a = new Point3D(0, 0, 2), b = new Point3D(1, 1, 2);
      double alpha = Math.PI;
      // Specify AB as directed axis of rotation
      // and alpha as the rotation angle:
      Rota3D.initRotate(a, b, alpha);
      // Vertices of a cube; 0, 1, 2, 3 at the bottom,
      // 4, 5, 6, 7 at the top. Vertex 0 at the origin O:
      Point3D[] v = {
            new Point3D(0, 0, 0), new Point3D(1, 0, 0),
            new Point3D(1, 1, 0), new Point3D(0, 1, 0),
            new Point3D(0, 0, 1), new Point3D(1, 0, 1),
            new Point3D(1, 1, 1), new Point3D(0, 1, 1)};
      System.out.println(
            "Cube rotated through 180 degrees about line AB,");
      System.out.println("where A = (0, 0, 2) and B = (1, 1, 2)");
      System.out.println("Vertices of cube:");
      System.out.println("    Before rotation    After rotation");
      for (int i = 0; i < 8; i++) {
         Point3D p = v[i];
         // Compute P1, the result of rotating P:
         Point3D p1 = Rota3D.rotate(p);
         System.out.println(i + ":  " + 
            p.x + " " + p.y + " " + p.z + "        " + 
            f(p1.x) + " " + f(p1.y) + " " + f(p1.z));
      }
   }

   static double f(double x) {return Math.abs(x) < 1e-10 ? 0.0 : x;}
}
