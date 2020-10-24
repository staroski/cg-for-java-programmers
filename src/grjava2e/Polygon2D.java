package grjava2e;
// Polygon2D: A class to triangulate polygons.
public class Polygon2D {
   Point2D[] vertices;
   int[] nrs;
   int n; 

   Polygon2D(Point2D[] p) {
      vertices = p;
      n = p.length;
      nrs = new int[n];
      for (int i=0; i<n; i++)
         nrs[i] = i;
   }
   
   Polygon2D(Point2D[] p, int[] index) { // Used for 3D applications
      vertices = p;
         n = index.length;
         nrs = new int[n];
         for (int i=0; i<n; i++)
            nrs[i] = Math.abs(index[i]); // Possibly negative to omit line segments to drawn
   }
  
   float angle(Point2D a, Point2D b, Point2D c){
      // Return angle ABC, using dot product u . v = |u||v|cos angle
      double xBA = a.x - b.x, yBA = a.y - b.y,
            xBC = c.x - b.x, yBC = c.y - b.y,
            dotproduct = xBA * xBC + yBA * yBC,
            lenBA = Math.sqrt(xBA * xBA + yBA * yBA),
            lenBC = Math.sqrt(xBC * xBC + yBC * yBC),
            cosB = dotproduct / (lenBA * lenBC);
      return (float)Math.acos(cosB);
   }
   
   boolean flippingDesirable(int iP, int iQ, int iR, int iS) {
      // Currently, diagonal PR divides quadrangle PQRS into two
      // triangles. Is the alternative diagonal QS a better choice?
      Point2D vP = vertices[iP], vQ = vertices[iQ], 
              vR = vertices[iR], vS = vertices[iS];
      // Compute the angles at the opposite vertices Q and S.
      // Flipping is desirable if (angle Q) + (angle S) > pi.
      return angle(vP, vQ, vR) + angle(vR, vS, vP) > Math.PI + 0.0001;
   }
     
   boolean anyFlipping(Tria[] trias) {
      if (trias.length < 2)
         return false;
      for (int i=0; i<trias.length; i++){
         int[] t = {trias[i].iA, trias[i].iB, trias[i].iC};
         for (int j=i+1; j<trias.length; j++){
            int[] u = {trias[j].iA, trias[j].iB, trias[j].iC};
            // Look for a common edge of triangles t and u
            for (int h=0; h<3; h++){
               for (int k=0; k<3; k++){
                  if (t[h] == u[k] && t[(h+1)%3] == u[(k+2)%3]){
                     int iP = Math.abs(t[(h+1)%3]), iQ = Math.abs(t[(h+2)%3]), 
                         iR = Math.abs(t[h]), iS = Math.abs(u[(k+1)%3]);
                     if (flippingDesirable(iP, iQ, iR, iS)){
                        trias[i] = new Tria(iP, iQ, iS); 
                        trias[j] = new Tria(iS, iQ, iR);
                        return true;
                     }
                  }
               }
            }
         }
      }
      return false; 
   }  
   
   Tria[] triangulate(){
      Tria[] tr = new Tria[n - 2];
      if (n <= 4){ // This is faster for important special cases.
         tr[0] = new Tria(nrs[0], nrs[1], nrs[2]);
         if (n == 4) {
            tr[1] = new Tria(nrs[2], nrs[3], nrs[0]);
            anyFlipping(tr);
         }
         return tr;
      } 
      int[] next = new int[n];
      for (int i = 0; i < n; i++) 
         next[i] = (i + 1) % n;
      
      for (int k = 0; k < n - 2; k++) {
         // Find a suitable triangle, consisting of two edges
         // and an internal diagonal:
         Point2D a, b, c;
         boolean triaFound = false;
         int iA = 0; 
         
         int count = 0;
         while (!triaFound && ++count < n) {
            int iB = next[iA], iC = next[iB];
           // System.out.println(" iA="+iA+" iB="+iB+" iC="+iC);
            int nA = Math.abs(nrs[iA]), 
                nB = Math.abs(nrs[iB]), 
                nC = Math.abs(nrs[iC]); 
            a = vertices[nA]; b = vertices[nB]; c = vertices[nC];
            if (Tools2D.area2(a, b, c) >= 0) {
               // Edges AB and BC; diagonal AC.
               // Test to see if no other polygon vertex
               // lies within triangle ABC:
               int j = next[iC];
               int nj = Math.abs(nrs[j]);
               int loopcount = 0; // to prevent infinite loop in special cases
               while (loopcount++ <= n && j != iA && 
                    (nj == nA || nj == nB || nj == nC) || 
                     !Tools2D.insideTriangle(a, b, c, vertices[nj])){
                 // System.out.println(" j="+j+" next[j]="+next[j]+" iA="+iA+" iB="+iB+" iC="+iC+" nrs[j]="+nrs[j]);
                  j = next[j];
                  nj = Math.abs(nrs[j]);
               }
               if (j == iA) { // Triangle ABC contains no other vertex:
                  tr[k] = new Tria(nA, nB, nC);
                  next[iA] = iC;
                  triaFound = true;
               }
            }
            iA = next[iA];
         }
         if (count == n) {
            // Degenerated case: vertices on the same line. //     Tria[] tr0 = new Tria[0];
            return new Tria[0]; // array of length 0.      tr0; // System.exit(1);
         }       
      }
    
      while (anyFlipping(tr))   
         ; // Keep flipping diagonals as long as this is desirable
      return tr;
   }   
}
