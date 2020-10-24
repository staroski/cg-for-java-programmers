package grjava2e;
// Sum.java: Demonstrating the class Input by computing the sum
//   of all numbers in the textfile example.txt (which contains
//   only numbers and whitespace characters).

// Copied from Section 5.5 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

public class Sum {
   public static void main(String[] args) {
      float x, s=0;
      Input inp = new Input("example.txt");
      for (;;) {
         x = inp.readFloat();
         if (inp.fails()) break;
         s += x;
      }
      System.out.println("The computed sum is " + s);
      if (!inp.eof())
         System.out.println("Input file missing or incorrect.");  
   }
}
