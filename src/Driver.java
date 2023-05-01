import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
/**
 * 
 * @author good0161
 * @version 5.0.1
 * Designs and creates an executable jar file display the entire trip on a map
 */

public class Driver {
	
	// Declare class data

    public static void main(String[] args) throws FileNotFoundException, IOException {
    	JFrame proFrame = null;
    	JPanel topPanel = null;
    	JButton play = null;
    	JCheckBox enStop = null;
    	JComboBox<String> aniTime = null;
    	GridBagConstraints layCont = null;

    	// Read file and call stop detection
    	TripPoint.readFile("triplog.csv");
    	TripPoint.h2StopDetection();
    	ArrayList<TripPoint> Trip = TripPoint.getTrip();
    	
    	// Set up frame, include your name in the title
    	proFrame = new JFrame("Project 5 - Andrew Goodspeed");
        proFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set up Panel for input selections
        topPanel = new JPanel();
    	
        // Play Button
        play = new JButton("Play");
    	
        // CheckBox to enable/disable stops
        enStop = new JCheckBox("Include Stops");

        // ComboBox to pick animation time
        String[] Times = {"15", "30", "60", "90"};
        aniTime = new JComboBox<String>(Times);
        aniTime.insertItemAt("Animation Time", 0);
        aniTime.setSelectedItem(0);
        
    	aniTime.setEditable(false);
    	
        // Add all to top panel
        topPanel.add(play);
        topPanel.add(enStop);
        topPanel.add(aniTime);
        
        // Set up mapViewer
        
        
        // Add listeners for GUI components
        play.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		
        	}
        });
        enStop.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		
        	}
        });
        aniTime.addItemListener(new ItemListener() {
        	@Override
        	public void itemStateChanged(ItemEvent e) {
        		
        	}
        });

        // Set the map center and zoom level
        
    }
    
    // Animate the trip based on selections from the GUI components
    
}