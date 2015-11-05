/*
 * vriGreyDisp.java
 *
 * Used in the Virtual Radio Interferometer to transform between images
 * and the U-V plane. Based on FFTTool by Mark Wieringa, 26/Dec/1996.
 *
 * 16/Jan/1997  Derek McKay
 *
 */

import java.applet.Applet;
import java.lang.Math;
import java.awt.image.*;
import java.awt.*;
//import vriDisplay;
//import vriUVcDisp;

//####################################################################//

class vriGreyDisp extends vriDisplay {
  Applet applet;
//  boolean replot = false;
  Image img;
  static float[] dat;    // Array of data for fourier transforming 
                         // (it is shared between the class instances)
  static float[] fft;    // Array of data after fourier transforming 
                         // (it is shared between the class instances)
  int[] pix;             // Array of pixels
  int imw;               // Width of the original image in pixels
  int imh;               // Height of the original image in pixels
  static int imsize;     // Size of the "squared" image
  String message = null; // Message to print on the Display
  String type = null;    // Used to select real/imag/amp/phase display

  public vriGreyDisp(int x, int y, int w, int h, Applet app) {
    super(x, y, w, h);
    applet = app;
  }

  public void paint(Graphics g) {
    Rectangle r = bounds();

    plotFocus(g);
    if(message != null) {
      g.setColor(Color.red);
      g.drawString(message, 20,20);
    } else if(img != null) {
      
      // Get current image scale
      imh = img.getHeight(this);
      imw = img.getWidth(this);
      // Determine the actual image scale after scaling, and crop the
      // displayScale to prevent it from being bigger than twice the image
      // size (an arbitrarty decision, any limit may be imposed).
      int sih = (int) ((double) imh / displayScale);
      if(sih > r.height * 2)
        displayScale = 0.5 * (double)imh / (double)r.height;
      int siw = (int) ((double) imw / displayScale);
      if(siw > r.width * 2)
        displayScale = 0.5 *  (double)imw / (double)r.width;
      // Okay, get the finalised image size.
      sih = (int) ((double) imh / displayScale);
      siw = (int) ((double) imw / displayScale);
      // Determine the image coordinates
      int x = displayCentre.x - siw/2;
      int y = displayCentre.y - sih/2;
      // Draw the image
      g.drawImage(img, x, y, siw, sih, applet);
    }
  }

  public void loadImage(String filename) {
    try {
      System.out.println("Loading "+filename);
      MediaTracker tracker = new MediaTracker(this);
      img = applet.getImage(applet.getDocumentBase(),filename);
      tracker.addImage(img, 0);

//      tracker.waitForID(0);
//      while (!tracker.checkID(0)) { Thread.sleep(500); }
//      Thread.sleep(250);
    tracker.waitForAll();

      try { Thread.sleep(5000); } catch(InterruptedException ie) {};

      if(tracker.isErrorID(0)) {
        System.err.println("Error loading image");
        message = new String("Error loading image");
      }
    } catch (Exception exc) {
      System.err.println("Error with image load");
    }
  }

/*
  public void imageComplete(int status) {
    System.out.println("Image completed with status "+status);
    switch(status) {
      case ImageConsumer.IMAGEERROR      : message = 
                                            new String("Image load error");
                                           break;
      case ImageConsumer.IMAGEABORTED    : message = 
                                             new String("Image load aborted");
                                           break;
      case ImageConsumer.SINGLEFRAMEDONE : message = null;
                                           break;
      case ImageConsumer.STATICIMAGEDONE : message = null;
                                           break;
      default                            : message = 
                                             new String("Unknown image error");
                                           break;
    }
    repaint();
  }
*/

  public void imgToPix() {
    System.out.println("Converting (imgToPix)");

    if (img != null) {
      imh = img.getHeight(this);
      imw = img.getWidth(this);
      int timeout = 100;
      while((imh <= 0 || imw <= 0) && timeout > 0) {
        imh = img.getHeight(this);
        imw = img.getWidth(this);
        timeout--;
      }
      System.out.println("Got "+this+"image: size = "+imw+"x"+imh+" "+timeout);
      pix = new int[imh*imw];

      PixelGrabber pg = new PixelGrabber(img,0,0,imw,imh,pix,0,imw);
      try {
        pg.grabPixels(100);
      } catch (InterruptedException e) {
        System.err.println("Interrupted waiting for pixels");
        return;
      }
      System.out.print("pg.status = "+pg.status()+" ...");
      if((pg.status() & ImageObserver.ABORT)      != 0) System.out.print(" ABORT");
      if((pg.status() & ImageObserver.ALLBITS)    != 0) System.out.print(" ALLBITS");
      if((pg.status() & ImageObserver.ERROR)      != 0) System.out.print(" ERROR");
      if((pg.status() & ImageObserver.FRAMEBITS)  != 0) System.out.print(" FRAMEBITS");
      if((pg.status() & ImageObserver.HEIGHT)     != 0) System.out.print(" HEIGHT");
      if((pg.status() & ImageObserver.PROPERTIES) != 0) System.out.print(" PROPERTIES");
      if((pg.status() & ImageObserver.SOMEBITS)   != 0) System.out.print(" SOMEBITS");
      if((pg.status() & ImageObserver.WIDTH)      != 0) System.out.print(" WIDTH");
      System.out.println(".");

      if ((pg.status() & ImageObserver.ABORT) != 0) {
        System.err.println("Image fetch aborted or errored");
        return;
      }
    }
  }

  public void pixToImg() {
    System.out.println("Comverting (pixToImg): "+
                       imsize+"x"+imsize);

    img = applet.createImage(
            new MemoryImageSource(imsize, imsize, pix, 0, imsize));
    repaint();
  }

  public void greyPix() {
    System.out.println("Making pix greyscale");
    if(pix != null) {
      for (int i=0; i<imh*imw; i++) {
        int red   = (pix[i] & 0x00ff0000) >> 16;
        int green = (pix[i] & 0x0000ff00 >> 8);
        int blue  = (pix[i] & 0x000000ff);
        int grey  = (red+green+blue)/3;
        pix[i] = (pix[i] & 0xff000000) |
          (grey << 16) | (grey << 8) | grey;
      }
    } else {
      System.err.println("transformToGrey: pix empty");
    }    
  }

  public void pixToDat() {
    System.out.println("Converting (pixToDat)");
    if(pix == null) {
      System.err.println("pixToDat: pix empty");
      return;
    }

    // Determine minimum sized box that image could fit into
    imsize = Math.max(imw, imh);
    int i;
    for(i=1; i<10; i++) {
      if (imsize <= Math.pow(2,i)) {
        imsize = (int) Math.pow(2,i);
        break;
      }
    }

    // Abandon exercise if it is too big
    if(i==10) { 
      System.err.println("Error: image too large (max = 1024x1024)");
      return;
    }
    System.out.println("Padding "+imw+"x"+imh+" image to "+imsize+"x"+imsize);

    // find mean value along the edge (to make the padding realisitic)
    float mean = 0;
    int count = 0;
    for(int h = 0; h < imh; h++) {
      int inc = imw-1;
      if(h == 0 || h == (imh-1)) inc = 1;
      for(int w = 0; w < imw; w += inc) {
	mean += (float)(pix[h*imw + w] & 0x000000ff);  // Assume already greyscale -
	count++;                                       // i.e. red = green = blue
      }
    }
    System.out.print("Edge sum = "+mean);
    mean /= (float)count;
    System.out.println("; edge mean = "+mean);
    // for now use full complex transform
    dat = new float[imsize*2*imsize];
    for (int h = 0; h < imh; h++) {
      for (int w = 0; w < imw; w++) {
	dat[(imsize/2+h-imh/2)*imsize*2 + (imsize/2+w-imw/2)*2] = 
	  (pix[h*imw + w] & 0x000000ff)-mean;
      }
    }
    // scale to range of one around zero
  }

  void datToPix() {
    System.out.println("Converting (datToPix)");

    // determine the scale of the data
    float[] min = new float[1];
    float[] max = new float[1];
    minmax(dat,min,max);
    System.out.println("Data minumum="+min[0]+", maximum="+max[0]);

    pix = new int[imsize * imsize];
    for (int y = 0; y < imsize; y++) {
      for  (int x = 0; x < imsize; x++) {
	int x1,y1;
        x1 = x; y1 = y;
        int grey = (int) (
                           255 * (dat[y1*imsize*2+x1*2] - min[0]) /
                           (max[0] - min[0])
                         );
	pix[y*imsize+x] = 0xff000000 | (grey << 16) | (grey << 8) | grey;
      }
    }
  }

  void fftToPix() {
    double value;        // Quantity that is plotted to a pixel
    System.out.println("Converting (datToPix)");

    // determine the scale of the data (use a forced -180:180 range for phase
    float[] min = new float[1];
    float[] max = new float[1];
    minmax(fft,min,max);
    if(type.equals("Phase")) {
      min[0] = (float)(-Math.PI / 2.0);
      max[0] = (float)( Math.PI / 2.0);
    }
    System.out.println("FFT minumum="+min[0]+", maximum="+max[0]);

    pix = new int[imsize * imsize];
    for (int y = 0; y < imsize; y++) {
      for  (int x = 0; x < imsize; x++) {
	int x1,y1;
        // shift origin to center of image
        x1 = x - imsize/2;
        y1 = y - imsize/2;
        if (x1 < 0) x1 += imsize;
        if (y1 < 0) y1 += imsize;

        // Depending on the "type" of display selected, we extract the 
        // relevant components of the fourier transform.
        if(type.equals("Real")) {
          value = fft[y1*imsize*2+x1*2];
        } else if (type.equals("Imag.")) {
          value = fft[y1*imsize*2+x1*2+1];
        } else if (type.equals("Phase")) {
          value = Math.atan2(fft[y1*imsize*2+x1*2],fft[y1*imsize*2+x1*2+1]);
        } else {
          // Used for Ampl., Colour and others that aren't handled
          value = Math.sqrt(fft[y1*imsize*2+x1*2]*fft[y1*imsize*2+x1*2] +
                            fft[y1*imsize*2+x1*2+1]*fft[y1*imsize*2+x1*2+1]);
        }

        int grey = (int) (255.0 * (value - min[0]) / (max[0] - min[0]) );
        pix[y*imsize+x] = 0xff000000 | (grey << 16) | (grey << 8) | grey;

        if(type.equals("Colour")) {
          double h = Math.atan2(fft[y1*imsize*2+x1*2],fft[y1*imsize*2+x1*2+1])
                      / Math.PI / 2.0;
          if(h < 0.0) h += 1.0;
          pix[y*imsize+x] = Color.HSBtoRGB( (float) h, (float) 1.0,
                                            (float) grey/(float)255.0);
        }  // End if(colour)
      }  // End for(x)
    }  // End for(y)
  }  // End fftToPix()


  void minmax(float arr[], float min[], float max[]) {
    // The reason for passing the values to the routine by 1x1 arrays is
    // because Java does not pass single variables by reference. As a result,
    // any manipulation of those variables is lost on leaving the scope of
    // the function. Arrays are passed by reference, and so the use of the 1x1
    // array circumvents this limitation.
    min[0] = max[0] = arr[0];
    for (int i=1; i<arr.length; i++) {
      if (arr[i] < min[0]) min[0] = arr[i];
      if (arr[i] > max[0]) max[0] = arr[i];
    }
  }


  void scaleDat() {
    float[] min = new float[1];
    float[] max = new float[1];

    minmax(dat,min,max);
    int mean=0;
    for (int i = 0; i < dat.length; i++) mean += dat[i];
    System.out.print("Limits = ["+min[0]+","+max[0]+"], sum = "+mean);
    mean /= (float) dat.length;
    System.out.print(", mean = "+mean);
    for (int i = 0; i < dat.length; i++) {
      dat[i] = (dat[i] - mean) / (max[0] - min[0]);
    }
    minmax(dat,min,max);
    System.out.println(", new limits = ["+min[0]+","+max[0]+"]");
  }
}

//####################################################################//



/*

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Label;
import java.awt.Button;
import java.awt.Event;
import java.awt.Panel;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.applet.Applet;
import java.lang.Math;
//import uvPlot;

public class vriImUV extends Panel {

//  TextField textField;
//  Label label;
//  Button FFTButton;
//  Button RevFFTButton;
//  Button ReloadButton;
//  Button ApplyUVCovButton;

  Image origImg;
  int[] pixelArr;
  int height;
  int width;
  Image img;
  float [] imArr;
  Image uvPlaneImg;
  float[] fftArr;
  int size;
  boolean newImage;
  uvPlot uvplot;
  Applet applet;

  public vriImUV(Applet app, uvPlot uvp) { 
    applet = app;
    uvplot = uvp;

    newImage=false;
    textField = new TextField(30);
    label = new Label("Enter image name");
    add(label);
    add(textField);
    FFTButton = new Button("FFT->");
    RevFFTButton = new Button("<-FFT");
    ReloadButton = new Button("Reload");
    ApplyUVCovButton = new Button("uvCoverage");
    add(FFTButton);
    add(RevFFTButton);
    add(ReloadButton);
    add(ApplyUVCovButton);
    reshape(0,700,600,300);
  }

  public boolean action(Event e, Object arg) {
    if (e.target instanceof TextField) {
      loadImage(textField.getText());
      newImage=true;
      img = null;
      repaint();
      return true;
    } else if ( e.target instanceof Button) {
      String value = (String) arg;
      if (value.equals("FFT->")) {
	if (newImage) {
	  grabPixels();
	  transformToGrey();
	  readAndScale();
	  newImage=false;
	}
	repaint();
	fft();
	repaint();
      } else if (value.equals("<-FFT")) {
	fftinv();
	repaint();
      } else if (value.equals("Reload")) {
	if (newImage) {
	  grabPixels();
	  transformToGrey();
	  newImage=false;
	}
	readAndScale();
	repaint();
      } else if (value.equals("uvCoverage")) {
	applyuvCov();
	repaint();
      }
      return true;
    }
    return false;
  }
  

  void loadImage(String name) {
    System.out.println("Loading "+name);
    origImg = applet.getImage(applet.getDocumentBase(),name);
    repaint();
  }

  void grabPixels() {
    if (origImg != null) {
      height=origImg.getHeight(this);
      width=origImg.getWidth(this);
      System.out.println("Got image "+height+"x"+width);
      pixelArr = new int[height*width];
      PixelGrabber pg = new PixelGrabber(origImg,0,0,width,height,pixelArr,0,width);
      try {
	pg.grabPixels();
      } catch (InterruptedException e) {
	System.err.println("interrupted waiting for pixels");
	return;
      }
      if ((pg.status() & ImageObserver.ABORT) != 0) {
	System.err.println("image fetch aborted or errored");
	return;
      }
    }
  }

  void transformToGrey() {
    if (pixelArr != null) {
      for (int i=0; i<height*width; i++) {
	int red= (pixelArr[i] & 0x00ff0000) >> 16;
	int green = (pixelArr[i] & 0x0000ff00 >> 8);
	int blue = (pixelArr[i] & 0x000000ff);
	int grey = (red+green+blue)/3;
	pixelArr[i]= (pixelArr[i] & 0xff000000) |
	  (grey << 16) | (grey << 8) | grey;
      }
    } else {
      System.err.println("transformToGrey: pixelArr empty");
    }
  }

  void readAndScale() {
    if (pixelArr == null) {
      System.err.println("readAndScale: pixelArr empty");
      return;
    }
    size = Math.max(width, height);
    int i;
    for (i=1; i<10; i++) {
      if (size <= Math.pow(2,i)) { size = (int) Math.pow(2,i); break;}
    }
    System.out.println("padding image to "+size+" squared");
    if (i==10) { 
      System.err.println("image too large");
      return;
    }
    // find mean edge value
    float mean=0;
    int count=0;
    for (int h=0; h<height; h++) {
      int inc=width-1;
      if (h==0 || h==(height-1)) inc=1;
      for (int w=0; w<width; w+=inc) {
	mean+=(pixelArr[h*width+w] & 0x000000ff);
	count++;
      }
    }
    mean/=count;
    // for now use full complex transform
    imArr = new float[size*2*size];
    for (int h=0; h<height; h++) {
      for (int w=0; w<width; w++) {
	imArr[(size/2+h-height/2)*size*2+(size/2+w-width/2)*2] = 
	  (pixelArr[h*width+w] & 0x000000ff)-mean;
      }
    }
    // scale to range of one around zero
    scale(imArr);
    complex2image(imArr,size,img,false);
  }

  void fft() {
    if (imArr == null ) return;
    // do the tranform
    int [] nn = new int[2]; nn[0]=size; nn[1]=size;
    System.out.println("doing forward transform...");
    fftArr = new float[imArr.length];
    for (int i=0; i<imArr.length; i++) fftArr[i]=imArr[i];
    Fourier.fourn(fftArr, nn, 2, 1);
    System.out.println("done.");
    complex2image(fftArr,size,uvPlaneImg,true);
  }

  void fftinv() {
    // do the inverse tranform
    if (fftArr == null) {
      System.err.println("fftinv : fftarr empty");
      return;
    }
    int [] nn = new int[2]; nn[0]=size; nn[1]=size;
    for (int i=0; i<imArr.length; i++) imArr[i]=fftArr[i]/size/size;
    Fourier.fourn(imArr, nn, 2, -1);
    complex2image(imArr, size, img, false);
  }

  void applyuvCov() {
    // multiply uvplane by coverage
    if (fftArr == null) {
      System.err.println("applyuvCov: fftarray emtpy");
      return;
    }
    // get square array with 1 on uv-tracks, 0 outside tracks
    float[] uvcov = uvplot.uvCoverage(size);
    for (int y=0; y<size; y++) {
      for (int x=0; x<size; x++) {
	int x1,y1;
	x1=x-size/2; y1=y-size/2;
	if (x1<0) x1+=size;
	if (y1<0) y1+=size;
	fftArr[y1*size*2+x1*2]*=uvcov[y*size+x];
	fftArr[y1*size*2+x1*2+1]*=uvcov[y*size+x];
      }
    }
    complex2image(fftArr, size, uvPlaneImg, true);
  }

  void complex2image(float[] arr, int size, Image image, boolean forward) {
    // determine the scale of the data
    float[] min=new float[1];
    float[] max=new float[1];
    minmax(arr,min,max);
    System.out.println("Image Minumum="+min[0]+", Maximum="+max[0]);
    int[] pixels = new int[size*size];
    for (int y=0; y<size; y++) {
      for  (int x=0; x<size; x++) {
	int x1,y1;
	if (forward) {
	  // shift origin to center of image
	  x1=x-size/2; y1=y-size/2;
	  if (x1<0) x1+=size;
	  if (y1<0) y1+=size;
	} else {
	  x1=x; y1=y;
	}
	int grey = (int) (255*(arr[y1*size*2+x1*2]-min[0])/(max[0]-min[0]));
	pixels[y*size+x]=0xff000000 | (grey << 16) | (grey <<8) | grey;
      }
    }
    // passing image as argument doesn't seem to work?
    if (forward) uvPlaneImg = createImage(new MemoryImageSource(size, size, pixels, 0, size));
    else img = createImage(new MemoryImageSource(size, size, pixels, 0, size));
  }

  void minmax(float arr[], float[] min, float[] max) {
    min[0] = max[0] = arr[0];
    for (int i=1; i<arr.length; i++) {
      if (arr[i]<min[0]) min[0]=arr[i];
      if (arr[i]>max[0]) max[0]=arr[i];
    }
  }

  void scale(float arr[]) {
    float[] min = new float[1];
    float[] max = new float[1];
    minmax(arr,min,max);
    int mean=0;
    for (int i=0; i<arr.length; i++) mean+=arr[i];
    mean/=arr.length;
    for (int i=0; i<arr.length; i++) {
      arr[i]=(arr[i]-mean)/(max[0]-min[0]);
    }
  }

  public void paint(Graphics g) {
    if (origImg !=null  && img == null) 
      g.drawImage(origImg,50,68,200,200,applet);
    if (img != null) g.drawImage(img,50,68,200,200,applet);
    if (uvPlaneImg != null) g.drawImage(uvPlaneImg,300,68,200,200,applet);
  }

  public static void main(String args[]) {
    ShowImage si = new ShowImage();
    si.init();
    Frame f = new Frame("ShowImage");
    f.resize(400,300);
    f.add("Center",si);
    f.show();
  }
}


*/

// EOF
