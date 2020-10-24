package grjava1e;
// IcoDode.java: Generating input files for
//    both an icosahedron and a dodecahedron.

// Copied from Appendix E of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

//    Uses: Point3D (Section 3.9), Tria (Section 5.5).
import java.io.FileWriter;
import java.io.IOException;

public class IcoDode {
   public static void main(String[] args) throws IOException {
      new Both();
   }
}

class Both {
   Point3D[] icoV;
   Tria[] icoF;

   Both() throws IOException {
      outIcosahedron();
      outDodecahedron();
   }

   void outIcosahedron() throws IOException {
      double zTop = 0.5 * Math.sqrt(5);
      // Vertices (icoV[1], ..., icoV[12]):
      icoV = new Point3D[13];
      icoV[1] = new Point3D(0, 0, zTop); // North pole
      double angle36 = Math.PI / 5, angle72 = 2 * angle36;
      for (int i = 2; i <= 6; i++) {
         double alpha = (i - 2) * angle72;
         icoV[i] = new Point3D(Math.cos(alpha), Math.sin(alpha), 0.5);
      }
      for (int i = 7; i <= 11; i++) {
         double alpha = angle36 + (i - 7) * angle72;
         icoV[i] = new Point3D(Math.cos(alpha), Math.sin(alpha), -0.5);
      }
      icoV[12] = new Point3D(0, 0, -zTop);
      // Faces (icoF[1], ..., icoF[20]):
      icoF = new Tria[21]; //
      icoF[1] = new Tria(1, 2, 3);
      icoF[2] = new Tria(1, 3, 4);
      icoF[3] = new Tria(1, 4, 5);
      icoF[4] = new Tria(1, 5, 6);
      icoF[5] = new Tria(1, 6, 2);
      icoF[6] = new Tria(2, 7, 3);
      icoF[7] = new Tria(3, 7, 8);
      icoF[8] = new Tria(3, 8, 4);
      icoF[9] = new Tria(4, 8, 9);
      icoF[10] = new Tria(4, 9, 5);
      icoF[11] = new Tria(5, 9, 10);
      icoF[12] = new Tria(5, 10, 6);
      icoF[13] = new Tria(6, 10, 11);
      icoF[14] = new Tria(6, 11, 2);
      icoF[15] = new Tria(2, 11, 7);

      icoF[16] = new Tria(12, 8, 7);
      icoF[17] = new Tria(12, 9, 8);
      icoF[18] = new Tria(12, 10, 9);
      icoF[19] = new Tria(12, 11, 10);
      icoF[20] = new Tria(12, 7, 11);
      FileWriter fwI = new FileWriter("icosa.dat");
      for (int i = 1; i <= 12; i++) {
         Point3D P = icoV[i];
         fwI.write(i + " " + P.x + " " + P.y + " " + P.z + "\r\n");
      }
      fwI.write("Faces:\r\n");
      for (int j = 1; j <= 20; j++) {
         Tria t = icoF[j];
         fwI.write(t.iA + " " + t.iB + " " + t.iC + ".\r\n");
      }
      fwI.close();
   }

   void outDodecahedron() throws IOException {
      FileWriter fwD = new FileWriter("dodeca.dat");
      for (int j = 1; j <= 20; j++)
         writeVertexInCenter(fwD, j);
      fwD.write("Faces:\r\n");
      // Horizontal, at the top:
      writeFace(fwD, 1, 2, 3, 4, 5);
      // Slightly facing upward:
      writeFace(fwD, 1, 6, 7, 8, 2);
      writeFace(fwD, 2, 8, 9, 10, 3);
      writeFace(fwD, 3, 10, 11, 12, 4);
      writeFace(fwD, 4, 12, 13, 14, 5);
      writeFace(fwD, 5, 14, 15, 6, 1);
      // Horizontal, at the bottom:
      writeFace(fwD, 20, 19, 18, 17, 16);
      // Slightly facing downward:
      writeFace(fwD, 20, 15, 14, 13, 19);
      writeFace(fwD, 19, 13, 12, 11, 18);
      writeFace(fwD, 18, 11, 10, 9, 17);
      writeFace(fwD, 17, 9, 8, 7, 16);
      writeFace(fwD, 16, 7, 6, 15, 20);
      fwD.close();
   }

   void writeVertexInCenter(FileWriter fwD, int j) throws IOException {
      Tria t = icoF[j];
      Point3D A = icoV[t.iA], B = icoV[t.iB], C = icoV[t.iC];
      float x = (A.x + B.x + C.x) / 3, 
            y = (A.y + B.y + C.y) / 3, 
            z = (A.z + B.z + C.z) / 3;
      fwD.write(j + " " + x + " " + y + " " + z + "\r\n");
   }

   void writeFace(FileWriter fwD, int a, int b, int c, int d, int e)
         throws IOException {
      fwD.write(a + " " + b + " " + c + " " + d + " " + e + ".\r\n");
   }
}