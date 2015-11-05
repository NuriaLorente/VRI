/*
 * vriUVpDisp.java
 *
 * Used in the Virtual Radio Interferometer.
 *
 * 06/Jan/1998 Nuria McKay - Extracted from vriGreyDisp.java
 *
 */

import java.applet.Applet;

class vriUVpDisp extends vriGreyDisp
{
   public vriUVpDisp(int x, int y, int w, int h, Applet app)
   {
      super(x, y, w, h, app);
      message = new String("No current transform");
   }

   public void fft()
   {
      if(dat == null)
         return;
      message = new String("Fourier transforming...");
      repaint();
      int[] nn = new int[2];
      nn[0] = imsize;
      nn[1] = imsize;
      System.out.print("Doing forward transform... ");
      fft = new float[dat.length];
      for(int i = 0; i < dat.length; i++)
         fft[i] = dat[i];
      Fourier.fourn(fft, nn, 2, 1);
      System.out.println("done.");
      fftToImg();
   }

   public void fftToImg()
   {
      fftToPix();
      pixToImg();
      message = null;
      repaint();
   }

   public void applyUVc(vriUVcDisp uv)
   {
      // Applies the UV coverage (from the UVcDisp class) to the FFT
      // (the fft[] array).

      System.out.println("applyUVc");
      if(fft == null)
      {
         System.err.println("applyUVc: fft[] array empty");
         return;
      }
      message = new String("Applying UV coverage...");
      repaint();

      // Get square array of floating point numbers with the taper
      // function on pixels with UV coverage and 0 on those without
      float[] cov = uv.uvCoverage(imsize);

      for(int y = 0; y < imsize; y++)
      {
         for(int x = 0; x < imsize; x++)
         {
            int x1, y1;
            x1 = x - imsize/2; y1 = y - imsize/2;
            if (x1 < 0) x1 += imsize;
            if (y1 < 0) y1 += imsize;
            fft[y1*imsize*2 + x1*2]     *= cov[y*imsize+x];  // Real
            fft[y1*imsize*2 + x1*2 + 1] *= cov[y*imsize+x];  // Imaginary
         }
      }
      fftToPix();
      pixToImg();
      message = null;
      repaint();
   }
}

//####################################################################//

