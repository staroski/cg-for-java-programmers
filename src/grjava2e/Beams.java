package grjava2e;
// Beams.java: Generating input files for a spiral of beams. The
//    values of n, a and alpha (in degrees) as well as the output 
//    file name are to be supplied as program arguments.

// Copied from Appendix E of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

//    Uses: Point3D (Section 3.9).
import java.io.FileWriter;
import java.io.IOException;

public class Beams {
   public static void main(String[] args) throws IOException {
      if (args.length != 4) {
         System.out.println(
               "Supply n (> 0), a (>= 0.5), alpha (in degrees)\n" + 
               "and a filename as program arguments.\n");
         System.exit(1);
      }
      int n = 0;
      double a = 0, alphaDeg = 0;
      try {
         n = Integer.valueOf(args[0]).intValue();
         a = Double.valueOf(args[1]).doubleValue();
         alphaDeg = Double.valueOf(args[2]).doubleValue();
         if (n <= 0 || a < 0.5)
            throw new NumberFormatException();
      } catch (NumberFormatException e) {
         System.out.println("n must be an integer > 0");
         System.out.println("a must be a real number >= 0.5");
         System.out.println("alpha must be a real number");
         System.exit(1);
      }
      new BeamsObj(n, a, alphaDeg * Math.PI / 180, args[3]);
   }
}

class BeamsObj {
   FileWriter fw;

   BeamsObj(int n, double a, double alpha, String fileName)
         throws IOException {
      fw = new FileWriter(fileName);
      Point3D[] P = new Point3D[9];
      double b = a - 1;
      P[1] = new Point3D(a, -a, 0);
      P[2] = new Point3D(a, a, 0);
      P[3] = new Point3D(b, a, 0);
      P[4] = new Point3D(b, -a, 0);
      P[5] = new Point3D(a, -a, 1);
      P[6] = new Point3D(a, a, 1);
      P[7] = new Point3D(b, a, 1);
      P[8] = new Point3D(b, -a, 1);
      for (int k = 0; k < n; k++) { // Beam k:
         double phi = k * alpha, 
                cosPhi = Math.cos(phi), sinPhi = Math.sin(phi);
         int m = 8 * k;
         for (int i = 1; i <= 8; i++) {
            double x = P[i].x, y = P[i].y;
            float x1 = (float) (x * cosPhi - y * sinPhi), 
                  y1 = (float) (x * sinPhi + y * cosPhi), 
                  z1 = P[i].z + k;
            fw.write((m + i) + " " + x1 + " " + y1 + " " + z1 + "\r\n");
         }
      }
      fw.write("Faces:\r\n");
      for (int k = 0; k < n; k++) { // Beam k again:
         int m = 8 * k;
         face(m, 1, 2, 6, 5);
         face(m, 4, 8, 7, 3);
         face(m, 5, 6, 7, 8);
         face(m, 1, 4, 3, 2);
         face(m, 2, 3, 7, 6);
         face(m, 1, 5, 8, 4);
      }
      fw.close();
   }

   void face(int m, int a, int b, int c, int d) throws IOException {
      a += m;
      b += m;
      c += m;
      d += m;
      fw.write(a + " " + b + " " + c + " " + d + ".\r\n");
   }
}