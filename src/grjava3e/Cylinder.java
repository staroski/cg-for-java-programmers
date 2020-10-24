package grjava3e;
// Cylinder.java: Generating an input file for a 
//                (possibly hollow) cylinder.

// Copied from Section 6.6 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

public class Cylinder extends Frame {
   public static void main(String[] args) {new Cylinder();}
   Cylinder() {new DlgCylinder(this);}
}

class DlgCylinder extends Dialog {
   TextField tfN = new TextField(5);
   TextField tfOuterDiam = new TextField(5);
   TextField tfInnerDiam = new TextField(5);
   Button button = new Button("  OK  ");
   FileWriter fw;

   DlgCylinder(Frame fr) {
      super(fr, "Cylinder (possibly hollow); height = 1", true);
      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {
            dispose();
            System.exit(0);
         }
      });
      Panel p1 = new Panel(), p2 = new Panel(), p3 = new Panel();
      p1.add(new Label("Number of vertices on outer circle: "));
      p1.add(tfN);
      p2.add(new Label(
            "Diameters D and d (cylinder is hollow if d > 0): "));
      p2.add(tfOuterDiam); p2.add(tfInnerDiam);
      p3.add(new Label("Generate 3D object file?"));
      p3.add(button);
      setLayout(new BorderLayout());
      add("North", p1);
      add("Center", p2);
      add("South", p3);

      button.addActionListener(new ActionListener() {
         @Override
        public void actionPerformed(ActionEvent ae) {
            int n = 0;
            float dOuter = 0, dInner = 0;
            try {
               n = Integer.valueOf(tfN.getText()).intValue();
               dOuter = 
                  Float.valueOf(tfOuterDiam.getText()).floatValue();
               dInner = 
                  Float.valueOf(tfInnerDiam.getText()).floatValue();
               if (dInner < 0) dInner = 0;
               if (n < 3 || dOuter <= dInner)
                  Toolkit.getDefaultToolkit().beep();
               else {
                  try {
                     genCylinder(n, dOuter / 2, dInner / 2);
                  } catch (IOException ioe) {
                  }
                  dispose();
                  System.exit(0);
               }
            } catch (NumberFormatException nfe) {
               Toolkit.getDefaultToolkit().beep();
            }
         }
      });
      Dimension dim = getToolkit().getScreenSize();
      setSize(3 * dim.width / 4, dim.height / 4);
      setLocation(dim.width / 8, dim.height / 8);
      setVisible(true);
   }

   void genCylinder(int n, float rOuter, float rInner)
         throws IOException {
      int n2 = 2 * n, n3 = 3 * n, n4 = 4 * n;
      fw = new FileWriter("Cylinder.dat");
      double delta = 2 * Math.PI / n;
      for (int i = 1; i <= n; i++) {
         double alpha = i * delta, 
               cosa = Math.cos(alpha), sina = Math.sin(alpha);
         for (int inner = 0; inner < 2; inner++) {
            double r = (inner == 0 ? rOuter : rInner);
            if (r > 0)
               for (int bottom = 0; bottom < 2; bottom++) {
                  int k = (2 * inner + bottom) * n + i;
                  // Vertex numbers for i = 1:
                  // Top: 1 (outer) 2n+1 (inner)
                  // Bottom: n+1 (outer) 3n+1 (inner)
                  wi(k); // w = write, i = int, r = real
                  wr(r * cosa); wr(r * sina); // x and y
                  wi(1 - bottom); // bottom: z = 0; top: z = 1
                  fw.write("\r\n");
               }
         }
      }
      fw.write("Faces:\r\n");
      // Top boundary face:
      for (int i = 1; i <= n; i++) wi(i);
      if (rInner > 0) {
         wi(-n3); // Invisible edge, see Section 7.5
         for (int i = n3 - 1; i >= n2 + 1; i--) wi(i);
         wi(n3); wi(-n); // Invisible edge again.
      }
      fw.write(".\r\n");
      // Bottom boundary face:
      for (int i = n2; i >= n + 1; i--) wi(i);
      if (rInner > 0) {
         wi(-(n3 + 1));
         for (int i = n3 + 2; i <= n4; i++) wi(i);
         wi(n3 + 1); wi(-(n + 1));
      }
      fw.write(".\r\n");
      // Vertical, rectangular faces:
      for (int i = 1; i <= n; i++) {
         int j = i % n + 1;
         // Outer rectangle:
         wi(j); wi(i); wi(i + n); wi(j + n); fw.write(".\r\n");
         if (rInner > 0) { // Inner rectangle:
            wi(i + n2); wi(j + n2); wi(j + n3); wi(i + n3);
            fw.write(".\r\n");
         }
      }
      fw.close();
   }

   void wi(int x) throws IOException {
      fw.write(" " + String.valueOf(x));
   }

   void wr(double x) throws IOException {
      if (Math.abs(x) < 1e-9) x = 0;
      fw.write(" " + String.valueOf((float) x));
      // float instead of double to reduce the file size
   }
}