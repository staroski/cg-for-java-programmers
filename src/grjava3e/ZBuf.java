package grjava3e;
// ZBuf.java: Perspective drawing using an input file that 
//    lists vertices and faces.
//    Z-buffer algorithm used for hidden-face elimination.

// Copied from Section 7.4 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

// Uses: CvZBuf (Section 7.4),
//       Point2D (Section 1.5), Point3D (Section 3.9) and
//       Obj3D, Polygon3D, Tria, Fr3D, Canvas3D (Section 5.5).
import java.awt.Frame;

public class ZBuf extends Frame {
   public static void main(String[] args) {
      new Fr3D(args.length > 0 ? args[0] : null, new CvZBuf(), 
         "ZBuf");
   }
}

