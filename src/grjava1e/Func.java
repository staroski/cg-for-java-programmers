package grjava1e;
// Func.java: A function of two variables x and y.

// Copied from Appendix E of
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

public class Func extends Frame {
   public static void main(String[] args) {new Func();}
   Func() {new FuncDialog(this);}
}

class FuncDialog extends Dialog {
TextField tfFun = new TextField(50), tfX = new TextField(10),
         tfY = new TextField(10), tfFileName = new TextField(15);
   Button buttonWriteFile = new Button("Write file"),
         buttonExit = new Button(" Exit ");

   FuncDialog(Frame fr) {
      super(fr, "Function of two variables", true);

      addWindowListener(new WindowAdapter() {
         @Override
        public void windowClosing(WindowEvent e) {
            dispose();
            System.exit(0);
         }
      });

      Panel p1 = new Panel(), p2 = new Panel(), p3 = new Panel();
      p1.add(new Label("z = f(x, y) = "));
      p1.add(tfFun);
      p2.add(new Label("xMin (deltaX) xMax"));
      p2.add(tfX);
      p2.add(new Label("        yMin (deltaY) yMax"));
      p2.add(tfY);
      p3.add(new Label("Output file name: "));
      p3.add(tfFileName);
      p3.add(buttonWriteFile);
      p3.add(buttonExit);
      setLayout(new BorderLayout());
      add("North", p1);
      add("Center", p2);
      add("South", p3);

      buttonWriteFile.addActionListener(new ActionListener() {
         @Override
        public void actionPerformed(ActionEvent ae) {
            float xa = 0, dx = 0, xb = 0, 
                  ya = 0, dy = 0, yb = 0;
            String sX = tfX.getText();

            xa = (new xyExpression(sX)).factor();
            sX = sX.substring(sX.indexOf('(') + 1);
            dx = (new xyExpression(sX)).factor();
            sX = sX.substring(sX.indexOf(')') + 1);
            xb = (new xyExpression(sX)).factor();

            String sY = tfY.getText();
            ya = (new xyExpression(sY)).factor();
            sY = sY.substring(sY.indexOf('(') + 1);
            dy = (new xyExpression(sY)).factor();
            sY = sY.substring(sY.indexOf(')') + 1);
            yb = (new xyExpression(sY)).factor();

            if (xa + dx > xb || dx <= 0 || ya + dy > yb || dy <= 0) {
               Toolkit.getDefaultToolkit().beep();
               return;
            }
            String s = tfFun.getText(), fileName = tfFileName.getText();

            xyExpression xyE = new xyExpression(s);
            try {
               xyE.generate(xa, dx, xb, ya, dy, yb, fileName);
            } catch (IOException ioe) {
            }
         }
      });
      buttonExit.addActionListener(new ActionListener() {
         @Override
        public void actionPerformed(ActionEvent ae) {
            dispose(); 
            System.exit(0);
         }
      });

      Dimension dim = getToolkit().getScreenSize();
      setSize(6 * dim.width / 10, dim.height / 4);
      setLocation(dim.width / 5, dim.height / 2);
      setVisible(true);
   }
}

class xyExpression {
   String buf;
   float x, y, lastNum;
   char lastChar;
   int pos;
   boolean OK;

   xyExpression(String s) {buf = s; OK = true;}

   void generate(float xa, float dx, float xb, 
                 float ya, float dy, float yb, 
                 String fileName) throws IOException {
      FileWriter fw = new FileWriter(fileName);
      int nx = Math.round((xb - xa) / dx), 
          ny = Math.round((yb - ya) / dy), nr = 0;
      float za = 1e30F, zb = -1e30F;
      outer: 
         for (int j = 0; j <= ny; j++) {
         float y = ya + j * dy;
         for (int i = 0; i <= nx; i++) {
            float x = xa + i * dx;
            nr = j * (nx + 1) + i + 1;
            float z = eval(x, y);
            if (!OK) {
               Toolkit.getDefaultToolkit().beep();
               break outer;
            }
            if (z < za) za = z;
            if (z > zb) zb = z;
            fw.write(nr + " " + x + " " + y + " " + z + "\r\n");
         }
      }
      // x, y and z axes:
      float dz = (zb - za) / 10, 
         xa1 = Math.min(xa - 2 * dx, 0), xb1 = Math.max(xb + 2 * dx, 0),
         ya1 = Math.min(ya - 2 * dy, 0), yb1 = Math.max(yb + 2 * dy, 0),
         za1 = Math.min(za - 2 * dz, 0), zb1 = Math.max(zb + 2 * dz, 0);
      fw.write(++nr + " " + xa1 + " 0 0\r\n");
      fw.write(++nr + " " + xb1 + " 0 0\r\n");
      fw.write(++nr + " 0 " + ya1 + " 0\r\n");
      fw.write(++nr + " 0 " + yb1 + " 0\r\n");
      fw.write(++nr + " 0 0 " + za1 + "\r\n");
      fw.write(++nr + " 0 0 " + zb1 + "\r\n");
      fw.write("Faces:\r\n");
      for (int i = 0; i < nx; i++) {
         for (int j = 0; j < ny; j++) {
            int k = j * (nx + 1) + i + 1, 
                m = k + nx + 1, k1 = k + 1, m1 = m + 1;
            fw.write(k + " " + -m1 + " " + k1 + ".\r\n");
            fw.write(k1 + " " + m1 + " " + -k + ".\r\n");
            fw.write(k + " " + -m1 + " " + m + ".\r\n");
            fw.write(m + " " + m1 + " " + -k + ".\r\n");
         }
      }
      int k = (nx + 1) * (ny + 1);
      fw.write(++k + " " + ++k + ".\r\n"); // x-axis
      fw.write(++k + " " + ++k + ".\r\n"); // y-axis
      fw.write(++k + " " + ++k + ".\r\n"); // z-axis
      fw.close();
      System.out.println("Ready!");
   }

   boolean readChar() {
      char ch;
      do {
         if (pos == buf.length()) return false;
         ch = buf.charAt(pos++);
      } while (ch == ' ');
      lastChar = ch;
      return true;
   }

   boolean nextIs(char ch) {
      char ch0 = lastChar;
      if (readChar()) {
         if (ch == lastChar) return true;
         pos--;
      }
      lastChar = ch0;
      return false;
   }

   float eval(float x, float y) {
      this.x = x; this.y = y;
      pos = 0;
      OK = true;
      return expression();
   }

   float expression() {
      float x = term();
      for (;;) {
         if (nextIs('+')) x += term(); else 
         if (nextIs('-')) x -= term(); else break;
      }
      return x;
   }

   float term() {
      float x = factor();
      for (;;) {
         if (nextIs('*')) x *= factor(); else 
         if (nextIs('/')) x /= factor(); else
            break;
      }
      return x;
   }

   float factor() {
      float v = 0;
      if (!readChar()) return 0;
      if (lastChar == 'x') return x;
      if (lastChar == 'y') return y;
      if (lastChar == '(') {
         v = expression();
         if (!nextIs(')')) {OK = false; return 0;}
         return v;
      }
      char ch = lastChar;
      if (ch == 'c'    // cos(expression)
       || ch == 's'    // sin(expression)
       || ch == 'p') { // pow(expression, expression)
         while ((OK = readChar()) && lastChar != '(')
            ;
         if (!OK) return 0;
         float arg = expression();
         if (ch == 'p') {
            if (!nextIs(',')) {OK = false; return 0;}
            double exponent = expression();
            v = (float) Math.pow(arg, exponent);
         } else
            v = (float) (ch == 'c' ? Math.cos(arg) : Math.sin(arg));
         if (!nextIs(')')) {OK = false; return 0;}
         return v;
      }
      pos--;
      if (number()) return lastNum;
      OK = false;
      return 0;
   }

   boolean number() {
      float x = 0;
      int nDec = -1;
      boolean neg = false;
      do {
         if (!readChar()) return false;
         if (lastChar == '-') {
            neg = true;
            if (!readChar()) return false;
            break;
         }
      } while (Character.isWhitespace(lastChar));
      if (lastChar == '.') {
         if (!readChar()) return false;
         nDec = 0;
      }
      if (!Character.isDigit(lastChar)) {OK = false; return false;}
      for (;;) {
         if (lastChar == '.' && nDec < 0) nDec = 0; else 
         if (Character.isDigit(lastChar)) {
            x = 10 * x + (lastChar - '0');
            if (nDec >= 0) nDec++;
         } 
         else {
            pos--;
            break;
         }
         if (!readChar()) break;
      }
      while (nDec > 0) {x *= 0.1; nDec--;}
      lastNum = (neg ? -x : x);
      return true;
   }
}