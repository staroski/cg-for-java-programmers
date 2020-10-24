package grjava3e;
// Fr3D.java: Frame class to deal with menu commands and other
//    user actions.

// Copied from Section 5.5 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Fr3D extends Frame implements ActionListener {
   protected MenuItem open, exit, eyeUp, eyeDown, eyeLeft, eyeRight,
         incrDist, decrDist, phongChoice;
   protected String sDir;
   protected Canvas3D cv;
   protected Menu mF, mV;
   private MenuItem exportHPGL;
   private Boolean hiddenLines, lineDrawing;

   Fr3D(String argFileName, Canvas3D cv, String textTitle) {
      super(textTitle);
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
      this.cv = cv;
      MenuBar mBar = new MenuBar();
      setMenuBar(mBar);
      mF = new Menu("File"); mV = new Menu("View");
      mBar.add(mF); mBar.add(mV);
      hiddenLines = cv instanceof CvHLines;
      lineDrawing = //hiddenLines || cv instanceof CvWireframe;
    		  !(cv instanceof CvPainter || cv instanceof CvZBuf);

      open = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
      eyeDown = new MenuItem("Viewpoint Down", 
                              new MenuShortcut(KeyEvent.VK_DOWN));
      eyeUp = new MenuItem("Viewpoint Up", 
                              new MenuShortcut(KeyEvent.VK_UP));
      eyeLeft = new MenuItem("Viewpoint to Left", 
                              new MenuShortcut(KeyEvent.VK_LEFT));
      eyeRight = new MenuItem("Viewpoint to Right", 
                              new MenuShortcut(KeyEvent.VK_RIGHT));

      incrDist = new MenuItem("Increase viewing distance",
                              new MenuShortcut(KeyEvent.VK_INSERT));
      decrDist = new MenuItem("Decrease viewing distance",
                              new MenuShortcut(KeyEvent.VK_DELETE));
      exit = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_Q));
      mF.add(open); mF.add(exit);
      mV.add(eyeDown); mV.add(eyeUp);
      mV.add(eyeLeft); mV.add(eyeRight);
      mV.add(incrDist); mV.add(decrDist);
     
      open.addActionListener(this);
      exit.addActionListener(this);
      eyeDown.addActionListener(this);
      eyeUp.addActionListener(this);
      eyeLeft.addActionListener(this);
      eyeRight.addActionListener(this);
      incrDist.addActionListener(this);
      decrDist.addActionListener(this);
      
      if (hiddenLines){
          exportHPGL = new MenuItem("Export HP-GL");
          mF.add(exportHPGL);     // mF defined in Fr3D
          exportHPGL.addActionListener(this); 
      } 
      else 
      if (!lineDrawing) {
         phongChoice = new MenuItem(
         (cv.specularDesired ? "Diffuse" : "Specular") + " illumination");
         mV.add(phongChoice);
         phongChoice.addActionListener(this);
      }

      
      add("Center", cv);
      Dimension dim = getToolkit().getScreenSize();
      setSize(dim.width / 2, dim.height / 2);
      setLocation(dim.width / 4, dim.height / 4);
      if (argFileName != null) {
         Obj3D obj = new Obj3D();
         if (obj.read(argFileName)) {cv.setObj(obj); cv.repaint();}
         else {
            System.out.println("Cannot open input file " + argFileName);
         }
            
      }
//      cv.setBackground(new Color(180, 180, 255));
      setVisible(true);
   }

   void vp(float dTheta, float dPhi, float fRho) {// Viewpoint
      Obj3D obj = cv.getObj();
      if (obj == null || !obj.vp(cv, dTheta, dPhi, fRho))
         Toolkit.getDefaultToolkit().beep();
   }

   @Override
public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() instanceof MenuItem) {
         MenuItem mi = (MenuItem) ae.getSource();
         if (mi == open) {
            FileDialog fDia = new FileDialog(Fr3D.this, "Open",
                  FileDialog.LOAD);
            fDia.setDirectory(sDir);
            fDia.setFile("*.dat");
            fDia.setVisible(true);
            String sDir1 = fDia.getDirectory();
            String sFile = fDia.getFile();
            String fName = sDir1 + sFile;
            Obj3D obj = new Obj3D();
            if (obj.read(fName)) {
               sDir = sDir1;
               cv.setObj(obj);
               cv.repaint();
            }
         } 
         else {
            if (mi == exit) System.exit(0); else 
            if (mi == eyeDown) vp(0, .1F, 1); else 
            if (mi == eyeUp) vp(0, -.1F, 1); else 
            if (mi == eyeLeft) vp(-.1F, 0, 1); else 
            if (mi == eyeRight) vp(.1F, 0, 1); else 
            if (mi == incrDist) vp(0, 0, 2); else 
            if (mi == decrDist) vp(0, 0, .5F); else
            if (mi == phongChoice) {
               cv.specularDesired = !cv.specularDesired; 
               phongChoice.setLabel(
            (cv.specularDesired ? "Diffuse" : "Specular") + " illumination");
               cv.repaint();
            } 
            else
            if (mi == exportHPGL) {
               Obj3D obj = cv.getObj();
               if (obj != null) {
                  ((CvHLines)cv).setHPGL(new HPGL(obj));
                  cv.repaint();
               } 
            }
         } 
      }
   }
}
