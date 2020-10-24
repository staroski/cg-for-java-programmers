package grjava3e;
// Painter.java: Perspective drawing using an input file that lists
//    vertices and faces. Based on the Painter's algorithm.
// Uses: Fr3D (Section 5.6) and CvPainter (Section 6.4),
//       Point2D (Section 1.4), Point3D (Section 3.9), 
//       Obj3D, Polygon3D, Tria, Fr3D, Canvas3D (Section 5.6).
import java.awt.Frame;

public class Painter extends Frame {
   public static void main(String[] args) {
      new Fr3D(args.length > 0 ? args[0] : null, new CvPainter(),
            "Painter");
   }
}