package grjava3e;
// Rota3DTetra.java: Rotating a tetrahedron that
//   has horizontal top and bottom edges, in such
//   a way that it obtains a horizontal bottom face.
//   Uses: Point3D, Rota3D (Section 3.9), Input (Section 5.6).
import java.io.FileWriter;
import java.io.IOException;

public class Rota3DTetra {
   public static void main(String[] args) throws IOException { 
      // Specify AB as directed axis of rotation and alpha as the 
      // rotation angle:
      Point3D A = new Point3D(1, -1, -1), B = new Point3D(-1, 1, -1);
      double alpha = Math.atan(Math.sqrt(2));
      Rota3D.initRotate(A, B, alpha);
      Point3D P = new Point3D(0, 0, 0);
      Input inp = new Input("tetra.dat");
      if (inp.fails()) {
         System.out.println("Supply file tetra.dat, see Section E.1");
         System.exit(0);
      }
      FileWriter fw = new FileWriter("tetra1.dat");
      for (;;) {
         int i = inp.readInt();
         if (inp.fails())
            break;
         P.x = inp.readFloat();
         P.y = inp.readFloat();
         P.z = inp.readFloat();
         Point3D P1 = Rota3D.rotate(P);
         fw.write(i + " " + P1.x + " " + P1.y + " " + P1.z + "\r\n");
      }
      inp.clear();
      // Copy the rest of file tetra.dat to tetra1.dat:
      for (;;) {
         char ch = inp.readChar();
         if (inp.fails()) break;
         fw.write(ch);
      }
      fw.close();
   }
}