import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
    	JComboBox aniTime = null;
    	GridBagConstraints layCont = null;

    	// Read file and call stop detection
    	TripPoint.readFile("triplog.csv");
    	TripPoint.h2StopDetection();
    	
    	// Set up frame, include your name in the title
    	proFrame = new JFrame("Project 5 - Andrew Goodspeed");
        
        // Set up Panel for input selections
        topPanel = new JPanel();
    	
        // Play Button
        play = new JButton("Play");
    	
        // CheckBox to enable/disable stops
        enStop = new JCheckBox("Include Stops");
    	
        // ComboBox to pick animation time
        Integer[] Times = {15, 30, 60, 90};
        aniTime = new JComboBox(Times);
    	
        // Add all to top panel
        
        
        // Set up mapViewer
        
        
        // Add listeners for GUI components
        play.addActionListener(play);

        // Set the map center and zoom level
        
    }
    @Override
    public void actionPerformed(ActionEvent event) {
    	
    }
    
    // Animate the trip based on selections from the GUI components
    
}