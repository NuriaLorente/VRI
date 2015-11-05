/*
 * vriObservatory.java
 *
 * Used in the Virtual Radio Interferometer
 *
 * 06/Jan/1998 Nuria McKay - Extracted from vri.java
 *
 */

class vriObservatory {
  public String menu_name;      // Name in observatory pop-up menu
  public String full_name;      // Proper name of obs.
  public double latitude;       // Latitude (radians)
  public double longitude;      // Longitude (radians) (not used)
  public int num_antennas;      // Number of antennas
  public int num_stations;      // Number of antennas
  public double ant_diameter;   // Diameter of antennas (metres)
  public double ant_el_limit;   // Antenna lower elevtaion limit (degrees)
  public vriConfig[] cfg;       // Set of "standard array configurations"
  public vriStation[] stn;      // Set of array stations
  public vriAntenna[] ant;      // Set of antennas
  public vriTrack[] trk;        // Set of defined road/rail tracks
  public vriLocation ref;       // Reference point of observatory.
  public int cfg_stations[][];  // Array of station numbers of configs
  public String cfg_name[];     // Array of names of configs

  public vriObservatory() {
//  Check for *.observatory files.
//  If found, put an entry into the observatory pop-up menu
  }

  public void select(String selection) {
    // When a selection is made from the observatory pop-up menu,
    // this method is called to load the values (from file or otherwise)

    if(selection.compareTo("ATCA") == 0) {
      System.out.println("Selected ATCA");
      menu_name = "ATCA";
      full_name = "Australia Telescope Compact Array";
      latitude = -0.529059644;
      ant_diameter = 22.0;
      ant_el_limit = 12.0 * Math.PI / 180.0;
      ref = new vriLocation();

      // Antennas
      num_antennas = 6;
      ant = new vriAntenna[num_antennas];
      for(int i = 0; i < ant.length; i++) ant[i] = new vriAntenna();

      // Stations
      int cs[] = {0, 2, 4, 6, 8, 10, 12, 14, 16, 32, 45, 64, 84, 98, 100, 
                  102, 109, 110, 111, 112, 113, 128, 129, 140, 147, 148, 
                  163, 168, 172, 173, 182, 189, 190, 195, 196, 388, 392};
      num_stations = cs.length;
      stn = new vriStation[num_stations];
      for(int i = 0; i < stn.length; i++) {
        stn[i] = new vriStation(cs[i],cs[34],(6000.0/392.0));   // cs[0]=Stn1, cs[34]=Stn35
      }

      // Track
      trk = new vriTrack[2];
      trk[0] = new vriTrack(stn[0],stn[34]);
      trk[1] = new vriTrack(stn[35],stn[36]);

      // Configurations
      int pad[][] = {
        { 3, 11, 16, 30, 34, 37},     // 6A     
        { 2, 12, 25, 31, 35, 37},     // 6B     
        { 1,  6, 21, 24, 31, 37},     // 6C     
        { 5, 10, 13, 28, 30, 37},     // 6D     
        {15, 18, 25, 28, 35, 37},     // 1.5A  
        {19, 21, 27, 31, 34, 37},     // 1.5B  
        {14, 22, 30, 33, 34, 37},     // 1.5C  
        {16, 17, 24, 31, 35, 37},     // 1.5D  
        {25, 27, 29, 33, 34, 37},     // 750A  
        {14, 17, 21, 24, 26, 37},     // 750B  
        {12, 13, 15, 18, 21, 37},     // 750C  
        {15, 16, 22, 24, 25, 37},     // 750D  
        { 2,  6,  8,  9, 10, 37},     // 375    
        {14, 15, 16, 17, 20, 37},     // 210   
        { 5,  6,  7,  8,  9, 37}      // 122B   
      };
      cfg_stations = pad;

      String nam[] = {
        "6A", "6B", "6C", "6D",
        "1.5A", "1.5B", "1.5C", "1.5D",
        "750A", "750B", "750C", "750D",
        "375", "210", "122B"
      };
      cfg_name = nam;

    } else if(selection.compareTo("New ATCA") == 0) {
      System.out.println("Selected modified ATCA");
      menu_name = "New ATCA";
      full_name = "Modified Australia Telescope Compact Array";
      latitude = -0.529059644;
      ant_diameter = 22.0;
      ant_el_limit = 12.0 * Math.PI / 180.0;
      ref = new vriLocation();

      // Antennas
      num_antennas = 6;
      ant = new vriAntenna[num_antennas];
      for(int i = 0; i < ant.length; i++) ant[i] = new vriAntenna();

      // Stations
      double cs[] = {0.0,   2.0,   4.0,   6.0,   8.0,  // CS01-05
                    10.0,  12.0,  14.0,  16.0,  32.0,  // CS06-10
                    45.0,  64.0,  84.0,  98.0, 100.0,  // CS11-15
                   102.0, 109.0, 110.0, 111.0, 112.0,  // CS16-20
                   113.0, 128.0, 129.0, 140.0, 147.0,  // CS21-25
                   148.0, 163.0, 168.0, 172.0, 173.0,  // CS26-30
                   182.0, 189.0, 190.0, 195.0, 196.0, 388.0, 392.0,  // -37
                   104.0, 106.0, 124.0, 125.0,         // CS38-41
                   106.0, 106.0, 106.0, 106.0,         // CS42-45
                   106.0, 106.0, 106.0, 106.0};        // CS46-49
      double cu[] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                     0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                     0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                     0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                     0.0, 0.0, 0.0, 0.0, 2.0, 5.0, 7.0, 11.0,
                     14.0, 19.0, 23.0, 24.0 };

      num_stations = cs.length;
      stn = new vriStation[num_stations];
      for(int i = 0; i < stn.length; i++) {
        stn[i] = new vriStation(cs[i], cu[i], cs[15], (6000.0/392.0)); 
        // cs[0]=Stn1, cs[34]=Stn35
      }

      // Track
      trk = new vriTrack[3];
      trk[0] = new vriTrack(stn[0],stn[34]);
      trk[1] = new vriTrack(stn[35],stn[36]);
      trk[2] = new vriTrack(stn[38],stn[48]);

      // Configurations
      int pad[][] = {
        {14, 16, 38, 17, 20, 37},     // EWuc    New East-West configs
        {16, 38, 17, 20, 41, 37},     // EWvc1
        {38, 18, 21, 40, 22, 37},     // EWvc2

        {39, 42, 43, 44, 45, 37},     // NSuc1   New North-South configs
        {39, 42, 43, 45, 46, 37},     // NSuc2
        {39, 43, 44, 47, 48, 37},     // NSvc1
        {42, 43, 45, 47, 49, 37},     // NSvc2

        {38, 39, 18, 42, 43, 37},     // H75     New Hybrid configs
        {15, 38, 19, 44, 45, 37},     // H168
        {15, 38, 19, 44, 46, 37},     // H214
        {15, 17, 40, 46, 48, 37},     // H368

        { 5,  6,  7,  8,  9, 37},     // 122B    Existing configs
        {14, 15, 16, 17, 20, 37},     // 210   
        { 2,  6,  8,  9, 10, 37},     // 375    
        {25, 27, 29, 33, 34, 37},     // 750A  
        {14, 17, 21, 24, 26, 37},     // 750B  
        {12, 13, 15, 18, 21, 37},     // 750C  
        {15, 16, 22, 24, 25, 37},     // 750D  
        {15, 18, 25, 28, 35, 37},     // 1.5A  
        {19, 21, 27, 31, 34, 37},     // 1.5B  
        {14, 22, 30, 33, 34, 37},     // 1.5C  
        {16, 17, 24, 31, 35, 37},     // 1.5D  
        { 3, 11, 16, 30, 34, 37},     // 6A     
        { 2, 12, 25, 31, 35, 37},     // 6B     
        { 1,  6, 21, 24, 31, 37},     // 6C     
        { 5, 10, 13, 28, 30, 37}      // 6D     
      };
      cfg_stations = pad;

      String nam[] = {
        "EWuc", "EWvc1", "EWvc2",
        "NSuc1", "NSuc2", "NSvc1", "NSvc2",
        "H75", "H168", "H214", "H368",
        "122B", "210", "375",
        "750A", "750B", "750C", "750D",
        "1.5A", "1.5B", "1.5C", "1.5D",
        "6A", "6B", "6C", "6D"
      };
      cfg_name = nam;

    } else if(selection.compareTo("WSRT") == 0) {
      System.out.println("Selected WSRT");
      menu_name = "WSRT";
      full_name = "Westerbork Synthesis Radio Telescope";
      latitude = +0.923574484;
      ant_diameter = 25.0;
      ant_el_limit = 0.0 * Math.PI / 180.0;
      ref = new vriLocation();

      // Antennas
      num_antennas = 14;
      ant = new vriAntenna[num_antennas];
      for(int i = 0; i < ant.length; i++) ant[i] = new vriAntenna();

      // Stations
      int cs[] = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 19, 20, 37, 38};
      num_stations = cs.length;
      stn = new vriStation[num_stations];
      for(int i = 0; i < stn.length; i++) {
        stn[i] = new vriStation(cs[i],cs[10], -72.0);   // 
      }

      // Track
      trk = new vriTrack[2];
      trk[0] = new vriTrack(stn[10],stn[11]);
      trk[1] = new vriTrack(stn[12],stn[13]);

      // Configurations
      int pad[][] = {
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}    // Default
      };
      cfg_stations = pad;

      String nam[] = {
        "default"
      };
      cfg_name = nam;

    } else if(selection.compareTo("MERLIN") == 0) {
      System.out.println("Selected MERLIN");
      menu_name = "MERLIN";
      full_name = "Multi Element Radio Linked Interferometer Network";
      latitude = +0.929160933;
      ant_diameter = 25.0;
      ant_el_limit = 0.0 * Math.PI / 180.0;
      ref = new vriLocation();

      // Antennas
      num_antennas = 6;
      ant = new vriAntenna[num_antennas];
      for(int i = 0; i < ant.length; i++) ant[i] = new vriAntenna();

      // Stations
      num_stations = 6;
      stn = new vriStation[num_stations];
      // The following lines of code are the proper X,Y,Z coordinates
      /*
      stn[0] = new vriStation(3822842.658,153800.130,5086287.220); // MkII
      stn[1] = new vriStation(3920644.000, -2563.000,5014000.000); // Camb
      stn[2] = new vriStation(3829083.786,169566.707,5081083.740); // Darn
      stn[3] = new vriStation(3860080.833,202102.955,5056572.658); // Knoc
      stn[4] = new vriStation(3817545.852,163028.964,5089898.255); // Tabl
      stn[5] = new vriStation(3923438.611,146912.201,5009757.693); // Deff
      */
      // The following lines of code are the fudged NS,EW,UD values from
      // a poorly gridded latitude/longitude/height coordinates set.

      stn[0] = new vriStation(   0.0,        0.0,        0.0 ); // Jb
      stn[1] = new vriStation( -98.663272, 180.934874,  13.0 ); // Cm
      stn[2] = new vriStation(  11.670062, -24.278806,  88.0 ); // Da
      stn[3] = new vriStation( -28.996605, -67.835530, 109.0 ); // Kn
      stn[4] = new vriStation(  26.381173, -14.973939,  77.0 ); // Ta
      stn[5] = new vriStation(-105.641049,  12.157448,  67.0 ); // De
//      stn[6] = new vriStation(   6.847840, -30.616558,  92.0 ); // Wa

      for(int i = 0; i < 6; i++) stn[i].EW -= 60000.0;  // Centre the array

      // Track
      trk = new vriTrack[5];
      trk[0] = new vriTrack(stn[0],stn[2]);
      trk[1] = new vriTrack(stn[0],stn[3]);
      trk[2] = new vriTrack(stn[0],stn[4]);
      trk[3] = new vriTrack(stn[0],stn[5]);
      trk[4] = new vriTrack(stn[5],stn[1]);

      // Configurations
      int pad[][] = {
        {1, 2, 3, 4, 5, 6}    // Default
      };
      cfg_stations = pad;

      String nam[] = {
        "default"
      };
      cfg_name = nam;

    } else {
      System.out.println("Not a valid selection... "+selection);
    }
  }

  public void selectNumAnt(int n) {
    num_antennas = n;
    vriAntenna a[] = new vriAntenna[num_antennas];
    for(int i = 0; i < a.length; i++) {
      a[i] = new vriAntenna();
      if(i < ant.length) {
        a[i].EW = ant[i].EW;
        a[i].NS = ant[i].NS;
        a[i].UD = ant[i].UD;
      }
    }
    ant = a;
  }

  public String defaultConfig() {
    for(int j = 0; j < ant.length; j++) {
      if(j < cfg_stations[0].length) {
        ant[j].EW = stn[cfg_stations[0][j]-1].EW;
        ant[j].NS = stn[cfg_stations[0][j]-1].NS;
        ant[j].UD = stn[cfg_stations[0][j]-1].UD;
      }
    }
    return cfg_name[0];
  }

  public boolean setConfig(String cfg_str) {
    for(int i = 0; i < cfg_stations.length; i++) {
      if(cfg_name[i].equals(cfg_str)) {
        for(int j = 0; j < ant.length; j++) {
          if(j < cfg_stations[i].length) {
            ant[j].EW = stn[cfg_stations[i][j]-1].EW;
            ant[j].NS = stn[cfg_stations[i][j]-1].NS;
            ant[j].UD = stn[cfg_stations[i][j]-1].UD;
          }
        }
        return true;
      } 
    }
    return false;
  }

  public void report() {

    System.out.println ("\nObservatory report... "+full_name);
    for(int i = 0; i < ant.length; i++) {
      System.out.println ("Antenna "+i+
        " NS = "+ant[i].NS+" EW = "+ant[i].EW+" UD = "+ant[i].UD );
    }
  }

}
