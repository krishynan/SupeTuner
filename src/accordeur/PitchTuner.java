/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accordeur;

/**
 *
 * @author krishynan
 */
public class PitchTuner {
    
    private double pitch;
    private double baseLa;
    private double centErrorToNearestPitchCent;
    private String notation;
    private String[] pitchLetter = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
    private String guitar ="Guitar";
    private String ukulele ="Ukulele";
    private String[] guitarNotes = {"E4","B3","G3","D3","A2","E2"};
    private String[] ukuleleNotes = {"A4","E4","C4","G4"};
    
    public PitchTuner(double baseLa){
       this.pitch = baseLa;
       this.baseLa = baseLa;
       recalculateNotation();
    }
        
    public PitchTuner(double pitch,double baseLa){
       this.pitch = pitch;
       this.baseLa = baseLa;
       
    }
    
    private void recalculateNotation(){
       double baseF = baseLa/Math.pow(2,5)/Math.pow(2,(9.0/12));; //Do-1 on the 440 scale
       long nearestPitchNote = Math.round(12*((Math.log(pitch/baseF)/Math.log(2))));
       long nearestPitchCent = 100 * nearestPitchNote;
       double pitchCent = 1200*((Math.log(pitch/baseF)/Math.log(2)));
       centErrorToNearestPitchCent = pitchCent - nearestPitchCent;
       int letter = (int)(nearestPitchNote%12);
       int octave = ((int) nearestPitchCent/1200)-1; //Because baseF is Do-1
       notation  = pitchLetter[letter] + octave;
        
    }
    public String pitchNotation() {
        return notation;
    }
    
    public double pitchCentErrorToNearest() {
        return centErrorToNearestPitchCent;
    }
    
    public double getPitch() {
        return pitch;
    }
    
    public void setPitch(double pitch) {
       this.pitch = pitch;
       recalculateNotation();
    }
    
    public double getBaseLa() {
        return baseLa;
    }
        
    public void setBaseLa(double baseLa) {
       this.baseLa = baseLa;
       recalculateNotation();
    }
    
    public String getInstrumentNotes(String instrument) {
        String notes="<html><br>";
       if (instrument.equals(guitar)) {
           for (String note: guitarNotes) {
               notes+= notation.equals(note)? "<font color='blue'>" + note + "</font><br>":note+"<br>";
           }
       }
       else if (instrument.equals(ukulele)) {
            for (String note: ukuleleNotes) {
               notes+= notation.equals(note)? "<font color='blue'>" + note + "</font><br>":note+"<br>";
           }
       }
       else return null;
       notes += "</html>";
       return notes;
    }
}
