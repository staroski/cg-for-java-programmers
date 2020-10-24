package grjava1e;
// Torus.java: Generating a data file for a torus. R is the radius of 
//    a large horizontal circle, on which n equidistant points will be
//    the centers of small vertical circles with radius 1. The values
//    of n and R as well as the output file name are to be supplied as
//    program arguments.

// Copied from Appendix E of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.io.FileWriter;
import java.io.IOException;

public class Torus {
   public static void main(String[] args) throws IOException {
      if (args.length != 3) {
         System.out.println(
"Supply n (> 2), R (>= 1) " + "and a filename as program arguments.\n");
         System.exit(1);
      }
      int n = 0;
      double R = 0;
      try {
         n = Integer.valueOf(args[0]).intValue();
         R = Double.valueOf(args[1]).doubleValue();
         if (n <= 2 || R < 1)
            throw new NumberFormatException();
      } catch (NumberFormatException e) {
         System.out.println("n must be an integer > 2");
         System.out.println("R must be a real number >= 1");
         System.exit(1);
      }
      new TorusObj(n, R, args[2]);
   }
}

class TorusObj {
   TorusObj(int n, double R, String fileName) throws IOException {
      FileWriter fw = new FileWriter(fileName);
      double delta = 2 * Math.PI / n;
      for (int i = 0; i < n; i++) {
         double alpha = i * delta, cosa = Math.cos(alpha), sina = Math
               .sin(alpha);
         for (int j = 0; j < n; j++) {
            double beta = j * delta, x = R + Math.cos(beta);
            float x1 = (float) (cosa * x), 
                  y1 = (float) (sina * x), 
                  z1 = (float) Math.sin(beta);
            fw.write(
            (i * n + j + 1) + " " + x1 + " " + y1 + " " + z1 + "\r\n");
         }
      }
      fw.write("Faces:\r\n");
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            int i1 = (i + 1) % n, j1 = (j + 1) % n, 
                a = i * n + j + 1, b = i1 * n + j + 1, 
                c = i1 * n + j1 + 1, d = i * n + j1 + 1;
            fw.write(a + " " + b + " " + c + " " + d + ".\r\n");
         }
      }
      fw.close();
   }
}