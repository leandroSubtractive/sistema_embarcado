package com.leandromendes.audioequalizer;

public class EqualizerProfile {
    private String name;
    private float bass_eq_value;
    private float mid_eq_value;
    private float hi_eq_value;

     public EqualizerProfile()
     {
          // Constructor set to default values
          this.name = "Default";
          this.bass_eq_value = 0;
          this.mid_eq_value = 0;
          this.hi_eq_value = 0;
     }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBass_eq_value() {
        return bass_eq_value;
    }

    public void setBass_eq_value(float bass_eq_value) {
        this.bass_eq_value = bass_eq_value;
    }

    public float getMid_eq_value() {
        return mid_eq_value;
    }

    public void setMid_eq_value(float mid_eq_value) {
        this.mid_eq_value = mid_eq_value;
    }

    public float getHi_eq_value() {
        return hi_eq_value;
    }

    public void setHi_eq_value(float hi_eq_value) {
        this.hi_eq_value = hi_eq_value;
    }
}
