package grjava2e;
// RGBtoHSL.java: Conversion from the RGB to the HSL color system.
public class RGBtoHSL {
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
         double sum = max + min, diff = max - min;
         double lum = sum / 2, sat;
         int lumInt = (int)(lum * 100 + 0.5);
         int satInt = 0, hueInt = 0;
         if (min < max) {
            if (lum <= 0.5)
               sat = diff/sum;
            else 
               sat = diff/(2 - sum);
            satInt = (int)(sat * 100 + 0.5);
         }
         double hue;
         if (r == max)
            hue = (g - b)/diff;
         else
         if (g == max)
            hue = 2 + (b - r)/diff;
         else // b == max
            hue = 4 + (r - g)/diff;
         hueInt = (int)(hue * 60 + 360 + 0.5) % 360;
         System.out.println("R="+rInt+" G="+gInt+" B="+bInt+
               "     H="+hueInt+" S="+satInt+ " L="+lumInt);
      }
   }
}
