/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accordeur;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.example.InputPanel;
import be.tarsos.dsp.example.PitchDetectionPanel;
import be.tarsos.dsp.example.Shared;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author krishynan
 */
public class MainForm extends javax.swing.JFrame implements PitchDetectionHandler {

    private AudioDispatcher dispatcher;
    private Mixer currentMixer;
    private JPanel inputPanel;
    private JPanel pitchDetectionPanel;
    private double baseLa=440;
    private PitchTuner pitchTuner;
            
    private PitchProcessor.PitchEstimationAlgorithm algo;
    private ActionListener algoChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            String name = e.getActionCommand();
            PitchProcessor.PitchEstimationAlgorithm newAlgo = PitchProcessor.PitchEstimationAlgorithm.valueOf(name);
            algo = newAlgo;
            try {
                setNewMixer(currentMixer);
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            }
        }
    };

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        pitchTuner = new PitchTuner(baseLa);
        initComponents();
        currentMixer = AudioSystem.getMixer(Shared.getMixerInfo(false, true).get(0)); //get first mixer avaiable
        inputPanel = new InputPanel(currentMixer); //creates mixer panel menu
        inputPanel.addPropertyChangeListener("mixer",
                new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                try {
                    setNewMixer((Mixer) arg0.getNewValue());
                } catch (LineUnavailableException | UnsupportedAudioFileException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // TODO Auto-generated catch block
                
            }
        });
        algo = PitchProcessor.PitchEstimationAlgorithm.MPM; //default algorithm
        pitchDetectionPanel = new PitchDetectionPanel(algoChangeListener); //algorithm panel

        try {
            setNewMixer(currentMixer);
        } catch (LineUnavailableException | UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated catch block
        
    }

    private void setNewMixer(Mixer mixer) throws LineUnavailableException,
            UnsupportedAudioFileException {

        if (dispatcher != null) {
            dispatcher.stop();
        }
        currentMixer = mixer;

        float sampleRate = 44100; //default sample rate
        int bufferSize = 8192; //around 5 times by second
        int overlap = 4096; // half buffer overlap for smoothing measures

        soundCardLabel.setText("Sound Adapter: " + Shared.toLocalString(mixer.getMixerInfo().getName()));

        final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
                true);
        final DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, format);
        TargetDataLine line;
        line = (TargetDataLine) mixer.getLine(dataLineInfo);
        final int numberOfSamples = bufferSize;
        line.open(format, numberOfSamples);
        line.start();
        final AudioInputStream stream = new AudioInputStream(line);

        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
        // create a new dispatcher
        dispatcher = new AudioDispatcher(audioStream, bufferSize,
                overlap);

        // add a processor
        dispatcher.addAudioProcessor(new PitchProcessor(algo, sampleRate, bufferSize, this));
        //Thread to run in parallel
        new Thread(dispatcher, "Audio dispatching").start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pitchLabel = new javax.swing.JLabel();
        centLabel = new javax.swing.JLabel();
        baseLaLabel = new javax.swing.JLabel();
        baseLaSpinner = new javax.swing.JSpinner();
        HertzLabel = new javax.swing.JLabel();
        javax.swing.UIManager.put("Slider.paintValue", true);
        centSlider = new javax.swing.JSlider();
        soundCardLabel = new javax.swing.JLabel();
        instrumentsComboBox = new javax.swing.JComboBox<>();
        instrumentNotesLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        algorithmMenu = new javax.swing.JMenu();
        inputMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SupeTuner");
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(70, 70, 70));
        jPanel1.setAutoscrolls(true);
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 260));

        pitchLabel.setFont(new java.awt.Font("Droid Sans", 0, 48)); // NOI18N
        pitchLabel.setForeground(java.awt.Color.green);
        pitchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pitchLabel.setText("A4");
        pitchLabel.setFocusable(false);
        pitchLabel.setPreferredSize(new java.awt.Dimension(55, 75));

        centLabel.setText("100*Cents");
        centLabel.setFocusable(false);

        baseLaLabel.setText("La4 =");

        baseLaSpinner.setModel(new javax.swing.SpinnerNumberModel(440, 416, 466, 1));
        baseLaSpinner.setRequestFocusEnabled(false);
        baseLaSpinner.setVerifyInputWhenFocusTarget(false);
        baseLaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                baseLaSpinnerStateChanged(evt);
            }
        });

        HertzLabel.setText("Hz");

        centSlider.setMajorTickSpacing(1000);
        centSlider.setMaximum(5000);
        centSlider.setMinimum(-5000);
        centSlider.setMinorTickSpacing(1000);
        centSlider.setPaintTicks(true);
        centSlider.setValue(0);
        centSlider.setEnabled(false);
        centSlider.setExtent(50);
        centSlider.setFocusable(false);
        centSlider.setPreferredSize(new java.awt.Dimension(200, 55));

        soundCardLabel.setEnabled(false);

        instrumentsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Instrument", "Guitar", "Ukulele" }));

        instrumentNotesLabel.setBackground(new java.awt.Color(70, 70, 70));
        instrumentNotesLabel.setForeground(java.awt.Color.gray);
        instrumentNotesLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        instrumentNotesLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        instrumentNotesLabel.setMaximumSize(new java.awt.Dimension(25, 5));
        instrumentNotesLabel.setMinimumSize(new java.awt.Dimension(25, 5));
        instrumentNotesLabel.setOpaque(true);
        instrumentNotesLabel.setPreferredSize(new java.awt.Dimension(25, 10));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(baseLaLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(baseLaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HertzLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 52, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(pitchLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(centSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(soundCardLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(instrumentsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(centLabel)
                            .addComponent(instrumentNotesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(instrumentsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(soundCardLabel))
                        .addGap(26, 26, 26)
                        .addComponent(pitchLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(centSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(instrumentNotesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(11, 11, 11)
                        .addComponent(centLabel)))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseLaLabel)
                    .addComponent(baseLaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HertzLabel))
                .addGap(21, 21, 21))
        );

        algorithmMenu.setText("Algorithms");
        algorithmMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                algorithmMenuMenuSelected(evt);
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });
        jMenuBar1.add(algorithmMenu);

        inputMenu.setText("Inputs");
        inputMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                inputMenuMenuSelected(evt);
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });
        jMenuBar1.add(inputMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void algorithmMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_algorithmMenuMenuSelected
        JFrame Menu1 = new JFrame(); // if clicked opens menu
        Menu1.add(pitchDetectionPanel);
        Menu1.pack();
        Menu1.setVisible(true);
    }//GEN-LAST:event_algorithmMenuMenuSelected

    private void inputMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_inputMenuMenuSelected
        JFrame Menu2 = new JFrame(); // if clicked opens menu
        Menu2.add(inputPanel);
        Menu2.pack();
        Menu2.setVisible(true);
    }//GEN-LAST:event_inputMenuMenuSelected

    private void baseLaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_baseLaSpinnerStateChanged
        this.baseLa =  (Integer) baseLaSpinner.getValue(); //detects changes in baseLa
        pitchTuner.setBaseLa(this.baseLa);
    }//GEN-LAST:event_baseLaSpinnerStateChanged
    
    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() != -1) { //if detected
            pitchTuner.setPitch(pitchDetectionResult.getPitch()); //get pitch frequency
            centSlider.setValue((int) Math.round(100*pitchTuner.pitchCentErrorToNearest())); //update slider multiplied because of decimals
            pitchLabel.setText(pitchTuner.pitchNotation()); //update text
            instrumentNotesLabel.setText(pitchTuner.getInstrumentNotes(instrumentsComboBox.getSelectedItem().toString())); //print instrument notes if selected
            if (Math.abs(pitchTuner.pitchCentErrorToNearest())<1){
                pitchLabel.setForeground(java.awt.Color.GREEN); //tuned color
            }
            else {
                pitchLabel.setForeground(java.awt.Color.RED); //out of tune color
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() { //create runnable for mainform
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HertzLabel;
    private javax.swing.JMenu algorithmMenu;
    private javax.swing.JLabel baseLaLabel;
    private javax.swing.JSpinner baseLaSpinner;
    private javax.swing.JLabel centLabel;
    private javax.swing.JSlider centSlider;
    private javax.swing.JMenu inputMenu;
    private javax.swing.JLabel instrumentNotesLabel;
    private javax.swing.JComboBox<String> instrumentsComboBox;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel pitchLabel;
    private javax.swing.JLabel soundCardLabel;
    // End of variables declaration//GEN-END:variables
}
