package grjava3e;
// Fr3DHDashed.java: Frame class for HLinesDashed.java.
// This class extends Fr3D to enable writing HP-GL output files.

import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

class Fr3DHDashed extends Fr3D {
   private MenuItem exportHPGL;
   CvHLinesDashed cv;

   Fr3DHDashed(String argFileName, CvHLinesDashed cv, String textTitle) {
      super(argFileName, cv, textTitle);
      exportHPGL = new MenuItem("Export HP-GL");
      mF.add(exportHPGL); 
      exportHPGL.addActionListener(this);
      this.cv = cv;
   }

   @Override
public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() instanceof MenuItem) {
         MenuItem mi = (MenuItem)ae.getSource();
         if (mi == exportHPGL) {
            Obj3D obj = cv.getObj();
            if (obj != null) {
               cv.setHPGL(new HPGL(obj));
               cv.repaint();
            }
            else 
               Toolkit.getDefaultToolkit().beep();
         } 
         else
            super.actionPerformed(ae);
      }
   }
}


