package grjava1e;
// SphTria.java: Generating a 3D object file for a sphere 
//    approximation consisting of triangles. In the output file there
//    are four times as many triangles as in the input file. Suitable 
//    input files are icosa.dat, produced by program IcoDode.java, and
//    the output files produced by this program (SphTria.java) itself!

// Copied from Appendix E of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

//    To run this program, enter, for example,
//       java SphTria icosa.dat sph80.dat.
//    Uses: Tria, Input (Section 5.5), Point3D (Section 3.9).
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class SphTria {
   public static void main(String[] args) throws IOException {
      if (args.length < 2) {
         System.out.println(
               "Command:\n" + "java SphTria InputFile OutputFile");
      } else
         new SphTriaObj(args[0], args[1]);
   }
}

class SphTriaObj {
   Vector v = new Vector(); // Vertices
   Vector<Tria> t = new Vector<Tria>();       // Triangular faces
   int nV, codeRadix;
   Hashtable<Integer, Integer> ht = new Hashtable<Integer, Integer>();
   String inputFile, outputFile;

   SphTriaObj(String inputFile, String outputFile) throws IOException {
      this.inputFile = inputFile;
      this.outputFile = outputFile;
      readFile();
      computeMidpoints();
      toUnitCircle();
      writeFile();
   }

   void readFile()/* throws IOException*/ {
      Input inp = new Input(inputFile);
      if (inp.fails()) error();
      v.addElement(new Integer(0)); // Start at position 1
      for (;;) {
         int nr = inp.readInt();
         if (inp.fails()) break;
         nV = nr;
         float x = inp.readFloat(), y = inp.readFloat(), 
               z = inp.readFloat();
         v.addElement(new Point3D(x, y, z));
      }
      inp.clear();
      codeRadix = nV + 1;
      inp.clear();
      while (inp.readChar() != '\n' && !inp.fails())
         ;
      // Rest of line 'Faces:' has now been skipped.
      for (;;) {
         int a = inp.readInt(), b = inp.readInt(), c = inp.readInt();
         if (inp.fails()) break;
         t.addElement(new Tria(a, b, c));
         inp.readChar(); // Skip '.'
      }
      inp.clear();
   }

   void error() {
      System.out.println("Problem with file input file " + inputFile);
      System.exit(1);
   }

   void computeMidpoints() {
      for (int j = 0; j < t.size(); j++) {
         Tria tr = t.elementAt(j);
         int a = tr.iA, b = tr.iB, c = tr.iC;
         addMidpoint(a, b); addMidpoint(b, c); addMidpoint(c, a);
      }
   }

   void addMidpoint(int p, int q) {
      if (p < q) {
         ht.put(new Integer(codeRadix * p + q), new Integer(++nV));
         Point3D P = (Point3D) v.elementAt(p), Q = (Point3D) v
               .elementAt(q);
         v.addElement(new Point3D( // at position nV
               (P.x + Q.x) / 2, (P.y + Q.y) / 2, (P.z + Q.z) / 2));
      }
   }

   int getMidpoint(int p, int q) {
      int key = p < q ? (codeRadix * p + q) : (codeRadix * q + p);
      Integer iObj = ht.get(new Integer(key));
      return iObj.intValue();
   }

   void toUnitCircle() {
      for (int i = 1; i <= nV; i++) // nV = v.size() - 1
      {
         Point3D P = (Point3D) v.elementAt(i);
         float r = (float) Math.sqrt(P.x * P.x + P.y * P.y + P.z * P.z);
         P.x /= r; P.y /= r; P.z /= r;
      }
   }

   void writeFile() throws IOException {
      FileWriter fw = new FileWriter(outputFile);
      for (int i = 1; i < v.size(); i++) {
         Point3D P = (Point3D) v.elementAt(i);
         fw.write(i + " " + P.x + " " + P.y + " " + P.z + "\r\n");
      }
      fw.write("Faces\r\n");
      for (int j = 0; j < t.size(); j++) {
         Tria tr = t.elementAt(j);
         int a = tr.iA, b = tr.iB, c = tr.iC;
         int mab = getMidpoint(a, b), mbc = getMidpoint(b, c), 
             mca = getMidpoint(
               c, a);
         fw.write(a + " " + mab + " " + mca + ".\r\n");
         fw.write(b + " " + mbc + " " + mab + ".\r\n");
         fw.write(c + " " + mca + " " + mbc + ".\r\n");
         fw.write(mab + " " + mbc + " " + mca + ".\r\n");
      }
      fw.close();
   }
}