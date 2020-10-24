package grjava2e;
// RGBtoHSL.java: Conversion from the RGB to the HSL color system.
public class RGBtoHSVHSL {
   public static void main(String[] args) {
      if (args.length != 3){
         System.out.println("Supply r, g and b (0-255) as program arguments.");
      }
      else {
         
         int rInt = Integer.valueOf(args[0]).intValue(),
             gInt = Integer.valueOf(args[1]).intValue(),
             bInt = Integer.valueOf(args[2]).intValue();
         double r = rInt / 255.0, g = gInt / 255.0, b = bInt / 255.0;
         double min = Math.min(r, Math.min(g,  b)),
                max = Math.max(r,  Math.max(g,  b));
         double diff = max - min;
      
         double h, s, v, l, hDeg;
         if (diff == 0)
            h = 0;
         else {
            if (max == r)
               h = (g - b)/diff;
            else
            if (max == g)
               h = (b - r)/diff;
            else // max == b
               h = (g - b)/diff;
         }      
         if (h >= 0)
            hDeg = h * 60;
         else
            hDeg = (h + 6) * 60;
         System.out.println("Given: R="+rInt+" G="+gInt+" B="+bInt);
         
         // HSV:
         if (max == 0)
            s = 0;
         else
            s = diff/max;
         v = max;
         System.out.println("HSV: H="+hDeg+" S="+s+" V="+v);
         
         // HSL:
         l = (min + max)/2;
         if (l == 0 || l == 1)
            s = 0;
         else
         if (l <= 0.5)
            s = diff/(2 * l);
         else
            s = diff/(2 - 2 * l);
         System.out.println("HSL: H="+hDeg+" S="+s+" L="+l);
      }
   }
}
