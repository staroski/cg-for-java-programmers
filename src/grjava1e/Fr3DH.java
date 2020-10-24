package grjava1e;
// Fr3DH.java: Frame class for HLines.java.
// This class extends Fr3D to enable writing HP-GL output files.

// Copied from Section 6.8 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

class Fr3DH extends Fr3D {
   private MenuItem exportHPGL;
   CvHLines cv;

   Fr3DH(String argFileName, CvHLines cv, String textTitle) {
      super(argFileName, cv, textTitle);
      exportHPGL = new MenuItem("Export HP-GL");
      mF.add(exportHPGL);
      exportHPGL.addActionListener(this);
      this.cv = cv;
   }

   @Override
public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() instanceof MenuItem) {
         MenuItem mi = (MenuItem) ae.getSource();
         if (mi == exportHPGL) {
            Obj3D obj = cv.getObj();
            if (obj != null) {
               cv.setHPGL(new HPGL(obj));
               cv.repaint();
            } else
               Toolkit.getDefaultToolkit().beep();
         } else
            super.actionPerformed(ae);
      }
   }
}