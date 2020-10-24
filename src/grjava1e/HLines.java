package grjava1e;
// HLines.java: Perspective drawing with hidden-line elimination.
// When you compile this program, the .class or the .java files of the
// following classes should also be in your current directory:
//       CvHLines, Fr3D, Polygon3D, Obj3D, Input, Canvas3D, 
//       Point3D, Point2D, Triangle, Tria, Tools2D, HPGL.
import java.awt.Frame;

public class HLines extends Frame {

   public static void main(String[] args) {
      new Fr3D(args.length > 0 ? args[0] : null, new CvHLines(),
            "Hidden-lines algorithm");
   }
}