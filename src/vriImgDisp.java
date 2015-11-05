/*
 * vriImgDisp.java
 *
 * Used in the Virtual Radio Interferometer
 *
 * 05/Jan/1998 Nuria McKay - Moved to separate file from vriGreyDisp.java
 *
 */

import java.applet.Applet;

class vriImgDisp extends vriGreyDisp {
  public vriImgDisp(int x, int y, int w, int h, Applet app) {
    super(x, y, w, h, app);
    message = new String("No current image");
  }

  public void load(String str) {
    System.out.println("--- begin operation ---");
//    message = new String("Loading new image");
//    repaint();
    loadImage(str);
    imgToPix();
    greyPix();
    pixToDat();
    scaleDat();
    datToPix();
    pixToImg();
    System.out.println("---- end operation ----");
    message = null;
    repaint();
  }

  public void invfft() {
    if(fft == null) return;
    message = new String("Inverse fourier transforming...");
    repaint();
    int[] nn = new int[2];
    nn[0] = imsize;
    nn[1] = imsize;
    System.out.print("Doing inverse transform... ");
    dat = new float[fft.length];
    for(int i = 0; i < dat.length; i++) dat[i] = fft[i]/imsize/imsize;
    Fourier.fourn(dat, nn, 2, -1);
    System.out.println("done.");
    datToPix();
    pixToImg();
    message = null;
    repaint();
  }
}
