package grjava3e;
// ColorCirles.java: draw three circles with primary colors Red, 
// Green and Blue and their intersections with additive colors.
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class ColorCircles extends Frame {
   public static void main(String[] args){new ColorCircles();}
   ColorCircles() {
   super("Additive Color System");
   addWindowListener(new WindowAdapter(){
      @Override
    public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(400, 400); 
      add("Center", new CvColorCircles());
      setVisible(true);
   }
}

class CvColorCircles extends Canvas {
   @Override
public void paint(Graphics g) {
      super.paint(g);
      final int RED = 0xFF0000, GREEN = 0x00FF00, BLUE = 0x0000FF;
      Shape circleTop = new Ellipse2D.Double(100, 43, 200, 200); 
      Shape circleLeft = new Ellipse2D.Double(50, 130, 200, 200); 
      Shape circleRight = new Ellipse2D.Double(150, 130, 200, 200); 
      Area top = new Area(circleTop);
      Area left = new Area(circleLeft);
      Area right = new Area(circleRight);
      Area intersectTopLeft = new Area(top);    
      intersectTopLeft.intersect(left);
      Area intersectLeftRight = new Area(left);
      intersectLeftRight.intersect(right);
      Area intersectTopRight = new Area(top);
      intersectTopRight.intersect(right);
      Area intersectCenter = new Area(intersectTopLeft);
      intersectCenter.intersect(right);
      setBackground(Color.black);
      Graphics2D g2 = (Graphics2D)g;
      g2.setColor(new Color(RED));     
      g2.fill(top);
      g2.setColor(new Color(GREEN));
      g2.fill(left);
      g2.setColor(new Color(BLUE));
      g2.fill(right);
      g2.setColor(new Color(RED + GREEN)); // Yellow
      g2.fill(intersectTopLeft);
      g2.setColor(new Color(GREEN + BLUE)); // Cyan
      g2.fill(intersectLeftRight);
      g2.setColor(new Color(RED + BLUE)); // Magenta
      g2.fill(intersectTopRight);
      g2.setColor(new Color(RED + GREEN + BLUE)); // White
      g2.fill(intersectCenter);
      g2.setColor(Color.black);
      g2.draw(circleTop);
      g2.draw(circleLeft);
      g2.draw(circleRight);
   }
}

/*
Here is the version for the Subtractive Color System (not for the book).
Note the use of minus signs.

class CvColorCircles extends Canvas {
	   public void paint(Graphics g) {
	      super.paint(g);
	      final int WHITE = 0xFFFFFF, MAGENTA = 0xFF00FF, YELLOW = 0xFFFF00, CYAN = 0x00FFFF;
	      Shape circleTop = new Ellipse2D.Double(100, 43, 200, 200); 
	      Shape circleLeft = new Ellipse2D.Double(50, 130, 200, 200); 
	      Shape circleRight = new Ellipse2D.Double(150, 130, 200, 200); 
	      Area top = new Area(circleTop);
	      Area left = new Area(circleLeft);
	      Area right = new Area(circleRight);
	      Area intersectTopLeft = new Area(top);    
	      intersectTopLeft.intersect(left);
	      Area intersectLeftRight = new Area(left);
	      intersectLeftRight.intersect(right);
	      Area intersectTopRight = new Area(top);
	      intersectTopRight.intersect(right);
	      Area intersectCenter = new Area(intersectTopLeft);
	      intersectCenter.intersect(right);
	      setBackground(new Color(WHITE));
	      Graphics2D g2 = (Graphics2D)g;

	      g2.setColor(new Color(MAGENTA));     
	      g2.fill(top);
	      g2.setColor(new Color(YELLOW));
	      g2.fill(left);
	      g2.setColor(new Color(CYAN));
	      g2.fill(right);
	      
	      int red = WHITE - (MAGENTA ^ YELLOW);
	      //      = 0xFFFFFF - (0xFF00FF ^ 0xFFFF00)
	      //      = 0xFFFFFF - 0x00FFFF
	      //      = 0xFF0000
	      // Recall that ^ is bitwise exclusive OR: 0 ^ 0 = 0, 0 ^ 1 = 1, 1 ^ 0 = 1, 1 ^ 1 = 0
	      g2.setColor(new Color(red)); 
	      g2.fill(intersectTopLeft);
	      
	      int blue = WHITE - (MAGENTA ^ CYAN);	      
	      g2.setColor(new Color(blue)); 
	      g2.fill(intersectTopRight);

	      int green = WHITE - (YELLOW ^ CYAN);
	      g2.setColor(new Color(green)); 
	      g2.fill(intersectLeftRight);
	      
	      int black = WHITE - red - green - blue;
	      g2.setColor(new Color(black)); 
	      g2.fill(intersectCenter);
	      
	      g2.setColor(Color.black);
	      g2.draw(circleTop);
	      g2.draw(circleLeft);
	      g2.draw(circleRight);
	   }
	}
*/
