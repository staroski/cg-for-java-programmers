package grjava2e;
// Wireframe.java: Perspective drawing using an input file that lists
//    vertices and faces. 

// Copied from Section 5.6 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

// Uses: Point2D (Section 1.5), 
//       Triangle, Tools2D (Section 2.13),
//       Point3D (Section 3.9),
//       Input, Obj3D, Tria, Polygon3D, Canvas3D, Fr3D (Section 5.5), 
//       CvWireframe (Section 5.6).
import java.awt.Frame;

public class Wireframe extends Frame {
   public static void main(String[] args) {
      new Fr3D(args.length > 0 ? args[0] : null, new CvWireframe(),
            "Wire-frame model");
   }
}