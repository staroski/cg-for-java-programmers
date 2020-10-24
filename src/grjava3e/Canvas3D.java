package grjava3e;
// Canvas3D.java: Abstract class.

// Copied from Section 5.5 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.Canvas;

abstract class Canvas3D extends Canvas {
   abstract Obj3D getObj();
   abstract void setObj(Obj3D obj);
   boolean specularDesired;
}