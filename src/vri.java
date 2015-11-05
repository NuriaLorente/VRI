/**
 * @(#)vri.java V.R.I.
 *
 * The Virtual Radio Interferometer
 *
 * This is a java applet (also runnable as a standalone java program) which
 * allows the simulation of various aspects of a radio interferometer.
 *
 * uvTest.java
 *
 * v1.0  05/Dec/1996  Derek McKay
 * v1.1  12/Dec/1996  Derek McKay & Nuria McKay
 * v1.2  13/Dec/1996  Derek McKay & Nuria McKay
 * v1.3  26/Dec/1996  Derek McKay, Mark Wieringa & Nuria McKay
 *
 * vri.java
 *
 * v2.0 06/Jan/1997   Derek McKay
 * v2.1 17/Mar/1997   Derek McKay
 * v2.2 07/Apr/1997   Derek McKay & Nuria McKay
 * v2.3 29/Jul/1997   Derek McKay & Nuria McKay
 * v2.4 10/Sep/1997   Nuria McKay (removed grid for ADASS)
 * v2.5 11/Sep/1997   Derek McKay (added new ATCA stations for JLC)
 *
 */

import java.lang.*;
import java.lang.Math;
import java.awt.*;
import java.awt.image.*;
import java.applet.Applet;
import java.util.Date;
//import vriImUV;
//import vriGreyDisp;
//import vriImgDisp;

/**
 * "vri" is the main class for the VRI applet. It has the capability
 * to allow the program to be run standalone or as an applet. When it
 * is being run as an applet, it should be called with:
 * <PRE>
 * &ltAPPLET codebase="vri" code="vri.class" width=612 height=692&gt&lt/APPLET&gt
 * </PRE>
 *
 * It also has methods for returning information about the applet, such as
 * the overloading of the getAppletInfo class.
 *
 * @author Derek J. McKay
 */
public class vri extends Applet {
  public vriArrDisp ArrDisp;
  public vriImgDisp ImgDisp;
  public vriUVcDisp UVcDisp;
  public vriUVpDisp UVpDisp;
  public vriArr2UVc Arr2UVc;
  public vriImg2UVp Img2UVp;
  public vriArrEdit ArrEdit;
  public vriUVcEdit UVcEdit;
  public vriImgEdit ImgEdit;
  public vriUVpEdit UVpEdit;
  public vriAuxEdit AuxEdit;
  public vriUVc2UVp UVc2UVp;
  public vriObsEdit ObsEdit;
  public vriObservatory obs;
  public vriAuxiliary aux;
  public vriDisplayCtrl ArrCtrl;
  public vriDisplayCtrl UVcCtrl;
  public vriDisplayCtrl ImgCtrl;
  public vriDisplayCtrl UVpCtrl;

  /**
   * This is a replacement for the standard method for applets.
   *
   * @return A string containing the Applet title, version and authors
   * @author Derek J. McKay
   */
  public String getAppletInfo() {
    return "VRI v2.3, by Derek & Nuria McKay";
  }


  /**
   * This starts the applet, if being run standalone (i.e. outside a browser)
   *
   * @param Command line arguments (not used)
   * @author Derek J. McKay
   */
  public static void main(String args[]) {
    System.out.println("Standalone java program");

    Frame f = new Frame("VRI");
    vri vriTest = new vri();
    vriTest.init();
    vriTest.start();
    f.add("Center", vriTest);
//    f.resize(612,692);
    f.setSize(612, 692);
//    f.show();
    f.setVisible(true);
  }

  /**
   * Main routine to setup the screen
   *
   * @param (none)
   * @return void
   * @exception none
   * @author Derek J. McKay
   */
  public void init() {

    System.out.println("VRI, Virtual Radio Interferometer");

    setLayout(null);
    setBackground(Color.lightGray);

    obs = new vriObservatory();
    obs.select("MERLIN");
//    obs.report();

    aux = new vriAuxiliary();

    int w0 = 612;
    int w1 = w0/2;
    int w2 = 256;
    int w3 = w1-w2;
    int w4 = w1-w3/2;
    int h0 = 34;       // Top "observatory" box
    int h1 = 32;       // Top control panel
    int h2 = w2;       // Panels are square
    int h3 = h2*2;     // 
    int h4 = 54;
    int h5 = 60;
    int x1 = w3;
    int x2 = w1;
    int x3 = w0-w3;
    int y1 = h0;
    int y2 = y1+h1;
    int y3 = y2+h2;
    int y4 = y2+h3;
    int y5 = y4+h4;

    ObsEdit = new vriObsEdit(       0,  0, w0-1, h0-1 );
    ImgEdit = new vriImgEdit(       0, y1, w1-1, h1-1 );
    ArrEdit = new vriArrEdit(      x2, y1, w1-1, h1-1, obs );
    ArrDisp = new vriArrDisp(      x2, y2, w2-1, h2-1, this, obs, ArrEdit );
    ImgDisp = new vriImgDisp(      x1, y2, w2-1, h2-1, this );
    UVcDisp = new vriUVcDisp(      x2, y3, w2-1, h2-1, obs, aux );
    UVpDisp = new vriUVpDisp(      x1, y3, w2-1, h2-1, this );
    Img2UVp = new vriImg2UVp(       0, y3-w3, w3-1, w3*2-1 );
    UVpEdit = new vriUVpEdit(       0, y4, w4-1, h4-1 );
    UVcEdit = new vriUVcEdit( x2+w3/2, y4, w4-1, h4-1 );
    UVc2UVp = new vriUVc2UVp( x2-w3/2, y4, w3-1, w3-1 );
    Arr2UVc = new vriArr2UVc(      x3, y3-w3/2, w3-1, w3-1, UVcDisp );
    AuxEdit = new vriAuxEdit(       0, y5, w0-1, h5-1, aux, obs );
    ArrCtrl = new vriDisplayCtrl(  x3, y2, w3-1, w3*4-1, "Station\nlock", ArrDisp);
    UVcCtrl = new vriDisplayCtrl(  x3, y4-w3*4, w3-1, w3*4-1, "?", UVcDisp);
    ImgCtrl = new vriDisplayCtrl(   0, y2, w3-1, w3*4-1, "?", ImgDisp);
    UVpCtrl = new vriDisplayCtrl(   0, y4-w3*4, w3-1, w3*4-1, "?", UVpDisp);

    // Place the components on the screen
    add(UVcCtrl);
    add(ArrCtrl);
    add(UVpCtrl);
    add(ImgCtrl);
    add(ArrDisp);
    add(ImgDisp);
    add(UVcDisp);
    add(UVpDisp);
    add(Arr2UVc);
    add(ArrEdit);
    add(UVcEdit);
    add(ImgEdit);
    add(UVpEdit);
    add(AuxEdit);
    add(UVc2UVp);
    add(ObsEdit);
    add(Img2UVp);

    AuxEdit.setDecRange();
    UVpEdit.type.select("Ampl.");
    UVpDisp.type = new String("Ampl.");
    UVcDisp.setColour(Color.blue);
    String c = obs.defaultConfig();
    ArrEdit.config.select(c);
    ArrDisp.repaint();

//    System.out.println("Total applet = "+w0+"x"+(y5+h5));

    // Size the display and show the components
    reshape(0,0,w0,y5+h5);
    show();
  }

  /**
   * This is the method that does the event processing and delegation
   *
   * @param e Java event
   * @param arg Java event argument
   * @return Whether the event was handled or not
   * @exception none
   * @author Derek J. McKay
   */
  public boolean action(Event e, Object arg) {
    // We need this to handle the events that affect things more globally

    if(e.target instanceof Button) {
      if(e.target == UVcEdit.add) {
        UVcDisp.addTracks();
      } else if(e.target == UVcEdit.clear) {
        UVcDisp.clearTracks();
      } else if(e.target == Img2UVp.fft) {
        UVpDisp.fft();
      } else if(e.target == Img2UVp.invfft) {
        ImgDisp.invfft();
      } else if(e.target == UVc2UVp.apply) {
        UVpDisp.applyUVc(UVcDisp);
      } else if(e.target == ArrEdit.stn_lock) {
        ArrDisp.stationLock();
      } else {
        return false;
      }
      return true;
    } else if(e.target == UVpEdit.type) {
      String s = (String) arg;
      if(s.equals("Real")) UVpDisp.type = new String("Real");
      if(s.equals("Imag.")) UVpDisp.type = new String("Imag.");
      if(s.equals("Ampl.")) UVpDisp.type = new String("Ampl.");
      if(s.equals("Phase")) UVpDisp.type = new String("Phase");
      if(s.equals("Colour")) UVpDisp.type = new String("Colour");
      UVpDisp.fftToImg();
    } else if(e.target == UVcEdit.colour) {
      String s = (String) arg;
      if(s.equals("Blue")) UVcDisp.setColour(Color.blue);
      if(s.equals("Red"))  UVcDisp.setColour(Color.red);
      if(s.equals("Hide")) UVcDisp.setColour(Color.black);
    } else if(e.target == ArrEdit.config) {
      String s = (String) arg;
      if(obs.setConfig(s)) {
        ArrDisp.repaint();
        UVcDisp.repaint();
      }
      return true;
    } else if(e.target == ImgEdit.src_choice) {
      String s = (String) arg;
//      // The following calls fail (from eclipse) due to the vri/ prefix... (npfl 5-Nov-2015)
//      if(s.equals("Point")) ImgDisp.load("vri/point.gif");
//      if(s.equals("Offset point")) ImgDisp.load("vri/offset_point.gif");
//      if(s.equals("Wide double")) ImgDisp.load("vri/wide_double.gif");
//      if(s.equals("Narrow double")) ImgDisp.load("vri/narrow_double.gif");
//      if(s.equals("Wide gaussian")) ImgDisp.load("vri/wide_gauss.gif");
//      if(s.equals("Narrow gaussian")) ImgDisp.load("vri/narrow_gauss.gif");
//      if(s.equals("Disc")) ImgDisp.load("vri/disc.gif");
//      if(s.equals("Crux")) ImgDisp.load("vri/crux.gif");
//      if(s.equals("Radio galaxy")) ImgDisp.load("vri/radio_galaxy.gif");

      if(s.equals("Point")) ImgDisp.load("point.gif");
      if(s.equals("Offset point")) ImgDisp.load("offset_point.gif");
      if(s.equals("Wide double")) ImgDisp.load("wide_double.gif");
      if(s.equals("Narrow double")) ImgDisp.load("narrow_double.gif");
      if(s.equals("Wide gaussian")) ImgDisp.load("wide_gauss.gif");
      if(s.equals("Narrow gaussian")) ImgDisp.load("narrow_gauss.gif");
      if(s.equals("Disc")) ImgDisp.load("disc.gif");
      if(s.equals("Crux")) ImgDisp.load("crux.gif");
      if(s.equals("Radio galaxy")) ImgDisp.load("radio_galaxy.gif");
      if(s.equals("file:")) {
        ImgEdit.filename.setEditable(true);
      } else {
        ImgEdit.filename.setEditable(false);
      }
      return true;
    } else if(e.target == ImgEdit.filename) {
      String s = (String) arg;
      ImgDisp.load(s);
      ImgEdit.src_choice.select("file:");
      return true;
    } else if(e.target == ObsEdit.site_choice) {
      String s = (String) arg;
      obs.select(s);
      ArrEdit.setupConfigMenu(obs);
      String c = obs.defaultConfig();
      ArrEdit.config.select(c);
      ArrDisp.repaint();
      AuxEdit.setDecRange();
      ObsEdit.setFields(obs);
      return true;
    } else if(e.target == ObsEdit.el_field) {
      Double d = new Double((String) arg);
      obs.ant_el_limit = d.doubleValue() * Math.PI / 180.0;
      ObsEdit.site_choice.select("custom");
      AuxEdit.setDecRange();
      UVcDisp.repaint();
      return true;
    } else if(e.target == ObsEdit.dia_field) {
      Double d = new Double((String) arg);
      obs.ant_diameter = d.doubleValue();
      UVcDisp.repaint();
      ObsEdit.site_choice.select("custom");
      return true;
    } else if(e.target == ObsEdit.lat_field) {
      Double d = new Double((String) arg);
      obs.latitude = d.doubleValue() * Math.PI / 180.0;
      AuxEdit.setDecRange();
      UVcDisp.repaint();
      return true;
    } else if(e.target == ObsEdit.ant_field) {
      Integer n = Integer.valueOf((String) arg);
      obs.selectNumAnt(n.intValue());
      ObsEdit.site_choice.select("custom");
      UVcDisp.repaint();
      ArrDisp.repaint();
      return true;
    }
    return false;
  }

  /**
   * A method to set the array configuration to "custom"
   *
   * @return void
   * @author Derek J. McKay
   */
  public void setCustomConfig() {
    ArrEdit.config.select("custom");
  }
}


class vriDisplayCtrl extends Panel {
  vriDisplay disp;
  public Button function;
  public Button zoomIn;
  public Button zoomOut;
  public Button zoomReset;

  public vriDisplayCtrl(int x, int y, int w, int h, String s, vriDisplay d) {
    disp = d;
    setLayout(new GridLayout(0, 1));
    add(new Label());
    add(new Label("Zoom",Label.CENTER));
    add(zoomIn = new Button("In"));
    add(zoomOut = new Button("Out"));
    add(zoomReset = new Button("Reset"));
    add(new Label());
    reshape(x,y,w,h);
//    System.out.println(this);
  }

  public boolean action(Event e, Object arg) {
    if(e.target instanceof Button) {
      String label = (String) arg;
      if(label.equals("In")) {
        disp.zoomIn();
      } else if(label.equals("Out")) {
        disp.zoomOut();
      } else if(label.equals("Reset")) {
        disp.zoomReset();
      } else {
        return false;
      }        
      return true;
    }
    return false;
  }
}



class vriArr2UVc extends Panel {
  vriUVcDisp disp;

  public vriArr2UVc(int x, int y, int w, int h, vriUVcDisp d) {
    setLayout(new GridLayout(1, 1));
    add(new Button("Plot"));
    disp = d;
    reshape(x,y,w,h);
//    System.out.println(this);
  }

  public boolean action(Event e, Object arg) {
    if(e.target instanceof Button) {
      String label = (String) arg;
      if(label.equals("Plot")) {
        disp.repaint();
        return true;
      }        
    }
    return false;
  }
}

class vriImg2UVp extends Panel {
  public Button fft;
  public Button invfft;

  public vriImg2UVp(int x, int y, int w, int h) {
    setLayout(new GridLayout(2, 1));
    reshape(x,y,w,h);
    add(fft = new Button("FFT"));
    add(invfft = new Button("FFT-1"));
//    System.out.println(this);
  }
}


class vriArrEdit extends Panel {
  public Choice config;
  public Button stn_lock;

  public vriArrEdit(int x, int y, int w, int h, vriObservatory obs) {
    setLayout(new FlowLayout());

    setupConfigMenu(obs);
    reshape(x, y, w, h);
//    System.out.println(this);
  }

  public void setupConfigMenu(vriObservatory obs) {
    removeAll();
    setLayout(new FlowLayout());
    add(new Label("Configuration:",Label.RIGHT));
    add(config = new Choice());
    for(int i = 0; i < obs.cfg_name.length; i++)
        config.addItem(obs.cfg_name[i]);
    config.select(obs.cfg_name[0]);
    add(stn_lock = new Button("Station lock"));
    Graphics gc = getGraphics();
    paintAll(gc);
//    paintComponents(gc);
//    show();
//    reshape(x, y, w, h);
//    System.out.println(this);
  }
}


class vriUVcEdit extends Panel {
  public Button add;
  public Button clear;
  public Choice colour;

  public vriUVcEdit(int x, int y, int w, int h) {
    setLayout(new GridLayout(0, 2));

    add(add = new Button("Add"));
    add(new Label("Accumulate",Label.LEFT));
    add(clear = new Button("Clear"));
    add(colour = new Choice());
    colour.addItem("Blue");
    colour.addItem("Red");
    colour.addItem("Hide");
    colour.select("Blue");

    reshape(x, y, w, h);
//    System.out.println(this);
  }
}


class vriImgEdit extends Panel {
  public Choice src_choice;
  public Label src_label;
  public TextField filename;

  public vriImgEdit(int x, int y, int w, int h) {
//    setLayout(null);
    setLayout(new FlowLayout());

//    int x1 = 55;
//    int x2 = x1+90;

    add(src_label = new Label("Source:",Label.RIGHT));
    add(src_choice = new Choice());
    src_choice.addItem("Point");
    src_choice.addItem("Offset point");
    src_choice.addItem("Wide double");
    src_choice.addItem("Narrow double");
    src_choice.addItem("Wide gaussian");
    src_choice.addItem("Narrow gaussian");
    src_choice.addItem("Disc");
    src_choice.addItem("Crux");
    src_choice.addItem("Radio galaxy");
    src_choice.addItem("file:");
    src_choice.select("file:");
    add(filename = new TextField("",10));

//    Dimension s1 = src_label.size();
//    Dimension s2 = src_choice.size();

//     src_label.reshape( 0, 0   , x1-1, h);
//    src_choice.reshape(x1, 0, x2-x1-1, h);
//      filename.reshape(x2, 0,  w-x2-1, h);

    reshape(x, y, w, h);
//    System.out.println(this);
  }
}

class vriUVpEdit extends Panel {
  public Choice type;

  public vriUVpEdit(int x, int y, int w, int h) {
    setLayout(new FlowLayout());

//    add(new Label("Resolution (256): ",Label.RIGHT));
//    add(new Scrollbar(Scrollbar.HORIZONTAL, 8,1,1,8));

    add(new Label("Display:",Label.RIGHT));
    add(type = new Choice());
    type.addItem("Real");
    type.addItem("Imag.");
    type.addItem("Ampl.");
    type.addItem("Phase");
    type.addItem("Colour");

    reshape(x, y, w, h);
//    System.out.println(this);
  }
}


class vriUVc2UVp extends Panel {
  public Button apply;

  public vriUVc2UVp(int x, int y, int w, int h) {
    setLayout(new GridLayout(1, 1));
    add(apply = new Button("Apply"));
    reshape(x, y, w, h);
//    System.out.println(this);
  }
}


class vriAuxEdit extends Panel {
  public Label fr_label;
  public Label bw_label;
  public Label fr_units;
  public Label bw_units;
  public TextField fr_field;
  public TextField bw_field;
  public Scrollbar ha1;
  public Label ha1_label;
  public Scrollbar ha2;
  public Label ha2_label;
  public Scrollbar dec;
  public Label dec_label;
  vriAuxiliary aux;
  vriObservatory obs;
  double ha_limit1;     // The Hour angle limit imposed by the Ant.El.limt
  double ha_limit2;     // and the choice of declination.

  public vriAuxEdit(int x, int y, int w, int h,
                    vriAuxiliary a, vriObservatory o) {

    aux = a;
    obs = o;
    setLayout(null);

    add(fr_label = new Label("Frequency:",Label.RIGHT));
    add(bw_label = new Label("Bandwidth:",Label.RIGHT));
    add(fr_units = new Label("MHz",Label.LEFT));
    add(bw_units = new Label("MHz",Label.LEFT));
    add(fr_field = new TextField("4800.0",20));
    add(bw_field = new TextField("100.0",20));

    add(ha1_label = new Label("Hour Angle (+6.0h):",Label.RIGHT));
    add(ha1 = new Scrollbar(Scrollbar.HORIZONTAL, -60, 10, -120, 120));
    add(ha2_label = new Label("Hour Angle (-6.0h):",Label.RIGHT));
    add(ha2 = new Scrollbar(Scrollbar.HORIZONTAL,  60, 10, -120, 120));
    add(dec_label = new Label("Declination ("+(90)+"�):",Label.RIGHT));
    add(dec = new Scrollbar(Scrollbar.HORIZONTAL, 90, 10, -90, +90));

    setHAlabel(ha1_label, -60);
    setHAlabel(ha2_label,  60);

    int x3 = w/2-100;
    int x1 = 80;
    int x2 = x3-50;
    int x4 = x3+120;
    int y1 = h/2;
    int y2 = h/3;
    int y3 = 2*h/3;

     fr_label.reshape( 0,  0,    x1-1, y1-1);
     bw_label.reshape( 0, y1,    x1-1, y1-1);
     fr_field.reshape(x1,  0, x2-x1-1, y1-1);
     bw_field.reshape(x1, y1, x2-x1-1, y1-1);
     fr_units.reshape(x2,  0, x3-x2-1, y1-1);
     bw_units.reshape(x2, y1, x3-x2-1, y1-1);
     ha1_label.reshape(x3,  0, x4-x3-1, y2-1);
     ha2_label.reshape(x3, y2, x4-x3-1, y2-1);
    dec_label.reshape(x3, y3, x4-x3-1, y2-1);
           ha1.reshape(x4,  0,  w-x4-1, y2-1);
           ha2.reshape(x4, y2,  w-x4-1, y2-1);
          dec.reshape(x4, y3,  w-x4-1, y2-1);

    limitHArange();
    bw_field.setEditable(false);

    reshape(x, y, w, h);
//    System.out.println(this);
  }

  public boolean handleEvent(Event e) {
    if(e.target == fr_field && e.id == Event.KEY_PRESS) {
      if(e.key == 10) {
        Double d = new Double(fr_field.getText());
        aux.freq = d.doubleValue();
      } else {
        return false;
      }
    } else if(e.target == fr_field && e.id == Event.LOST_FOCUS) {
      Double d = new Double(fr_field.getText());
      aux.freq = d.doubleValue();
    } else if(e.target == dec) {
      dec_label.setText(
        "Declination (" + dec.getValue() + "�):"
      );
      aux.dec = (double)dec.getValue() * Math.PI / 180.0;
      limitHArange();
    } else if(e.target == ha1) {
      int h = ha1.getValue();
      aux.ha1 = 1.5 * (double)h * Math.PI / 180.0;
      aux.ha1 = Math.max(aux.ha1, ha_limit1);
      aux.ha1 = Math.min(aux.ha1, ha_limit2);
      h = getHAint(aux.ha1);
      setHAlabel(ha1_label, h);
      if(aux.ha1 > aux.ha2) {
        aux.ha2 = aux.ha1;
        ha2.setValue(h);
        setHAlabel(ha2_label, h);
      }
    } else if(e.target == ha2) {
      int h = ha2.getValue();
      aux.ha2 = 1.5 * (double)h * Math.PI / 180.0;
      aux.ha2 = Math.max(aux.ha2, ha_limit1);
      aux.ha2 = Math.min(aux.ha2, ha_limit2);
      h = getHAint(aux.ha2);
      setHAlabel(ha2_label, h);
      if(aux.ha2 < aux.ha1) {
        aux.ha1 = aux.ha2;
        ha1.setValue(h);
        setHAlabel(ha1_label, h);
      }
    } else {
      return false;   // Nothing we can do here - pass the buck to the parent!
    }
    return true;
  }

  void limitHArange() {
    double ha = 
        ( Math.sin(obs.ant_el_limit) - 
           Math.sin(obs.latitude) * Math.sin(aux.dec) ) /
        ( Math.cos(obs.latitude) * Math.cos(aux.dec) );
    if(ha >= 1.0) {
      ha_limit1 = ha_limit2 = 0.0;
    } else if(ha <= -1.0) {
      ha_limit1 = -12.0;
      ha_limit2 = 12.0;
    } else {
      ha = Math.abs(Math.acos(ha));
      ha_limit1 = -ha;
      ha_limit2 = ha;
    }
    if(aux.ha1 < ha_limit1) {
      aux.ha1 = quantizeHA(ha_limit1);
      int h = getHAint(aux.ha1);
      setHAlabel(ha1_label, h);
      ha1.setValue(h);
    }
    if(aux.ha2 > ha_limit2) {
      aux.ha2 = quantizeHA(ha_limit2);
      int h = getHAint(aux.ha2);
      setHAlabel(ha2_label, h);
      ha2.setValue(h);
    }
  }

  public void setDecRange() {
    int el = (int) Math.round(obs.ant_el_limit * 180.0 / Math.PI);
    int lat = (int) Math.round(obs.latitude * 180.0 / Math.PI);

    int dec_max = Math.min( 90 + lat - el,  90);    
    int dec_min = Math.max(-90 + lat + el, -90);
    int dec_val = Math.min(dec.getValue(), dec_max);
        dec_val = Math.max(dec.getValue(), dec_min);

    dec.setValues(dec_val, 10, dec_min, dec_max);
  };

  double quantizeHA(double ha) {
    ha *= 12.0 / Math.PI;             // Convert to hours
    ha = Math.rint(ha*10.0) / 10.0;   // Round to 1 decimal place
    ha *= Math.PI / 12.0;             // Convert back to radians
    return ha;
  }

  int getHAint(double ha) {
    int h;
    ha *= 12.0 / Math.PI;             // Convert to hours
    h = (int) Math.round(ha*10.0);    // Round to 1 decimal place * 10.0
    return h;
  }

  void setHAlabel(Label label, int h) {
    String s = new String("h):");

    // Fix an additional .0 for the integer Hour Angle values (keeps it neat)
    if((double)(h/10) == (double)h/10.0) s = new String(".0h):");

    // Force a "+" sign if necessary and set the label text
    if(h > 0) {
      label.setText("Hour Angle (+" + (double)h/10.0 + s);
    } else {
      label.setText("Hour Angle (" + (double)h/10.0 + s);
    }
  }
}


class vriObsEdit extends Panel {
  public Choice site_choice;
  public TextField lat_field;
  public TextField ant_field;
  public TextField dia_field;
  public TextField el_field;

  public vriObsEdit(int x, int y, int w, int h) {
    setLayout(new FlowLayout(FlowLayout.LEFT));

    add(new Label("Obs:",Label.RIGHT));
    add(site_choice = new Choice());
    site_choice.addItem("MERLIN");
    site_choice.addItem("ATCA");
    site_choice.addItem("WSRT");
    site_choice.addItem("New ATCA");
    site_choice.addItem("custom");
    site_choice.select("MERLIN");

    add(new Label("Lat:",Label.RIGHT));
    add(lat_field = new TextField("+53.0517",8));
    add(new Label("Ants:",Label.RIGHT));
    add(ant_field = new TextField("6",2));
    add(new Label("Dia:",Label.RIGHT));
    add(dia_field = new TextField("25.0",4));
    add(new Label("El lim:",Label.RIGHT));
    add(el_field = new TextField("5.0",4));

    reshape(x, y, w, h);
//    System.out.println(this);
  }

  public void setFields(vriObservatory obs) {
    lat_field.setText(Double.toString(obs.latitude * 180.0 / Math.PI));
    ant_field.setText(Integer.toString(obs.num_antennas));
    dia_field.setText(Double.toString(obs.ant_diameter));
    el_field.setText(Double.toString(obs.ant_el_limit * 180.0 / Math.PI));
  }
}

//####################################################################//

class vriArrDisp extends vriDisplay {
  Color bg;      // Background "grass" colour - could be obs. dependent :-)
  Applet applet;     // Applet identity (for image loading)
  int imgw = -1;    // Width of antenna images
  int imgh = -1;    // Height of antenna images
  double im_scale = 1.00;    // 0.64 is nicer
  Image image;       // Image of antenna for ArrDisp
  int scaled_imgw = -1;   // Width of image after scaling
  int scaled_imgh = -1;   // Height of image after scaling
                          // We need this because the antenna is plotted wrt
                          // the top-left (not centre) of the image
  int xoff = 0;      // Image X-offset due to top-left (not centre) positioning
  int yoff = 0;      // Image Y-offset due to top-left (not centre) positioning
  static vriObservatory obs;  // Observatory being used
  vriAntenna pick;    // Antenna selected by the mouse
  vriArrEdit edit;    // Edit panel associated with this instance

  public vriArrDisp(int x, int y, int w, int h,
                    Applet app, vriObservatory o,
                    vriArrEdit e) {
    super(x, y, w, h);
    applet = app;
    obs = o;
    edit = e;
    obs.ref.x = w/2;    // Set obs. reference point
    obs.ref.y = h/2;
//    obs.report();
    bg = new Color(174,255,81);

    // Now load in the antenna images/icons for display on the site map
    // Note that we wait for them to finish loading before proceding
    try {
      MediaTracker tracker = new MediaTracker(this);
//      image = applet.getImage(applet.getDocumentBase(),"vri/antenna.gif");
      image = applet.getImage(applet.getDocumentBase(),"antenna.gif");
      tracker.addImage(image, 0);
      tracker.waitForID(0);
      if(tracker.isErrorID(0)) {
        System.err.println("Error loading antenna icon");
      }
    } catch (Exception exc) {
      System.err.println("Error with antenna icon load");
    }

    displayScale = 300000.0;
    defaultScale = 300000.0;
    repaint();
  }

  public void grow() {
    im_scale *= 1.25;
    if(im_scale > 2.0) im_scale = 2.0;
    scaled_imgh = scaled_imgw = -1;
    repaint();
  }

  public void shrink() {
    im_scale /= 1.25;
    if(im_scale < 0.1) im_scale = 0.1;
    scaled_imgh = scaled_imgw = -1;
    repaint();
  }

  public void randomize() {
    Rectangle r = bounds();
    for(int i = 0; i < obs.ant.length; i++) {
      obs.ant[i].x = (int)(10.0 + (r.width-20)*Math.random());
      obs.ant[i].y = (int)(10.0 + (r.height-20)*Math.random());
      obs.ant[i].xyz2NEU(obs.ref,displayScale,displaySize);
    }
    repaint();
  }

  public void stationLock() {
    int s = 0;
    double dist;

    for(int i = 0; i < obs.ant.length; i++) {
      vriAntenna a = obs.ant[i];
      double bestdist = Double.MAX_VALUE;
      for(int j = 0; j < obs.stn.length; j++) {
        dist = Math.pow((a.NS - obs.stn[j].NS),2.0) + 
               Math.pow((a.EW - obs.stn[j].EW),2.0);
        if(dist < bestdist) {
          s = j;
          bestdist = dist;
        }
      }
      obs.ant[i].NS = obs.stn[s].NS;
      obs.ant[i].EW = obs.stn[s].EW;
    }
    repaint();
  }

  public void update(Graphics g) {
    paint(g);
  }

  public void paint(Graphics g) {
    Rectangle r = bounds();

    // Do background grass and plot the focus border
    g.setColor(bg);
    g.fillRect(0, 0, r.width-1, r.height-1);
    plotFocus(g);

    // Determine observatory reference point (for plotting)
    obs.ref.x = displayCentre.x;
    obs.ref.y = displayCentre.y;
    obs.ref.xyz2NEU(obs.ref,displayScale,displaySize);

    // Plot road/rail tracks
    g.setColor(Color.black);
    for(int j = 0; j < obs.trk.length; j++) {
      obs.trk[j].start.NEU2xyz(obs.ref,displayScale,displaySize);
      obs.trk[j].end.NEU2xyz(obs.ref,displayScale,displaySize);
      g.drawLine(obs.trk[j].start.x, obs.trk[j].start.y,
                 obs.trk[j].end.x, obs.trk[j].end.y);
    }

    // Plot antenna stations
    for(int j = 0; j < obs.stn.length; j++) {
      obs.stn[j].NEU2xyz(obs.ref,displayScale,displaySize);
      g.drawOval(obs.stn[j].x-2, obs.stn[j].y-2, 4, 4);
    }

    // Plot observatory centre
    g.drawLine(obs.ref.x, obs.ref.y+2, obs.ref.x, obs.ref.y-2);
    g.drawLine(obs.ref.x+2, obs.ref.y, obs.ref.x-2, obs.ref.y);

    // Calculate antenna image sizes
    Image curimage = image;   // Allows us to scale curimage later. We only 
                              // use one image; all antennas look the same.
    if (imgw < 0) {
      imgw = curimage.getWidth(this);
      imgh = curimage.getHeight(this);
      if (imgw < 0 || imgh < 0) {
        return;
      }
    }

    // Determine scale based on zoom, etc.
    if (scaled_imgw < 0) {
      scaled_imgw = (int)(imgw*im_scale);
      scaled_imgh = (int)(imgh*im_scale);
      xoff = (scaled_imgw) / 2;
      yoff = (scaled_imgh) / 2;
    }

    // Plot antennas
    for(int i = 0; i < obs.ant.length; i++) {
      obs.ant[i].NEU2xyz(obs.ref,displayScale,displaySize);
      if (imgw != scaled_imgw || imgh != scaled_imgh) {
        g.drawImage(curimage, obs.ant[i].x-xoff, obs.ant[i].y-yoff,
                    scaled_imgw, scaled_imgh, this);
      } else {
        g.drawImage(curimage, obs.ant[i].x-xoff, obs.ant[i].y-yoff, this);
      }
    }

    // Draw a scale
    double l = displayScale * (displaySize - 20.0) / displaySize;
    l = Math.log(l)/Math.log(10.0);
    l = Math.pow(10.0, Math.floor(l));
    int m = (int) Math.round(l * displaySize / displayScale);
    g.drawLine(10, r.height-10, 10+m, r.height-10);
    String s = new String();
    if(l >= 1000.0) {
      s = Double.toString(l/1000.0) + "km";
    } else {
      s = Double.toString(l) + "m";
    }
    g.drawString(s, 10,r.height-12);

  }

  public boolean mouseDown(Event e, int x, int y) {
    double bestdist = Double.MAX_VALUE;
    for(int i = 0; i < obs.ant.length; i++) {
      vriAntenna a = obs.ant[i];
      double dist = (a.x - x) * (a.x - x) + (a.y - y) * (a.y - y);
      if( dist < bestdist) {
        pick = a;
        bestdist = dist;
      }
    }
    pick.x = x;
    pick.y = y;
    pick.xyz2NEU(obs.ref,displayScale,displaySize);
    repaint();
    edit.config.select("custom");
//    applet.setCustomConfig();
    return true;
  }

  public boolean mouseDrag(Event e, int x, int y) {
    pick.x = x;
    pick.y = y;
    pick.xyz2NEU(obs.ref,displayScale,displaySize);
    repaint();
    return true;
  }

  public boolean mouseUp(Event e, int x, int y) {
    pick.x = x;
    pick.y = y;
    pick.xyz2NEU(obs.ref,displayScale,displaySize);
    repaint();
    return true;
  }

}


// EOF
