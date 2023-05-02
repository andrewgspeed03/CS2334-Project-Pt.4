import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

/**
 * 
 * @author good0161
 * @version 5.0.1
 * Designs and creates an executable jar file display the entire trip on a tripMap
 */

public class Driver {
	
	// Declare class data
	private static JFrame proFrame;
	private static JPanel topPanel;
	private static JButton play;
	private static JCheckBox enStop;
	private static JComboBox<String> aniTime;
	//private static GridBagConstraints layCont;
	private static JMapViewer tripMap;
	private static int speed = 15;
	private static boolean showStops = false;
	private static Timer timer;
	private static Image marker = new ImageIcon("raccoon.png").getImage();
	
    public static void main(String[] args) throws FileNotFoundException, IOException {

    	// Read file and call stop detection
    	TripPoint.readFile("triplog.csv");
    	TripPoint.h2StopDetection();
    	ArrayList<TripPoint> Trip = TripPoint.getTrip();
    	ArrayList<TripPoint> movingTrip = TripPoint.getMovingTrip();
    	
    	// Set up frame, include your name in the title
    	proFrame = new JFrame("Project 5 - Andrew Goodspeed");
        proFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        proFrame.setSize(400, 400);
        proFrame.setLayout(new BorderLayout());
        
        // Set up Panel for input selections
        topPanel = new JPanel();
    	
        // Play Button
        play = new JButton("Play");
    	
        // CheckBox to enable/disable stops
        enStop = new JCheckBox("Include Stops");

        // ComboBox to pick animation time
        String[] Times = {"Animation Time","15", "30", "60", "90"};
        aniTime = new JComboBox<String>(Times);
        aniTime.setSelectedItem(0);
        
    	aniTime.setEditable(false);
    	
        // Add all to top panel
        topPanel.add(play);
        topPanel.add(enStop);
        topPanel.add(aniTime);
        proFrame.add(topPanel, BorderLayout.NORTH);
        
        // Set up tripMap
        tripMap = new JMapViewer();
        tripMap.setTileSource(new OsmTileSource.TransportMap());
        proFrame.add(tripMap, BorderLayout.CENTER);
        
        // Add listeners for GUI components
        play.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(timer != null) 
        			timer.stop();
        		Play(Trip);
        	}
        });
        enStop.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		showStops = !showStops;
        	}
        });
        aniTime.addItemListener(new ItemListener() {
        	@Override
        	public void itemStateChanged(ItemEvent e) {
        		if(e.getStateChange() == ItemEvent.SELECTED) {
        			String sel = (String) aniTime.getSelectedItem();
        			if(aniTime.getSelectedIndex() != 0) {
        				speed = Integer.parseInt(sel);
        			}        			
        		}
        	}
        });
        

        // Set the tripMap center and zoom level
        proFrame.setVisible(true);
    }
    // Animate the trip based on selections from the GUI components


    private static void Play(ArrayList<TripPoint> Trip){
    	
    	tripMap.removeAllMapPolygons();
    	List<MapMarkerDot> points = new ArrayList<MapMarkerDot>();
    	
    	for(TripPoint x: Trip) {
    		points.add(new MapMarkerDot(x.getLat(),x.getLon()));	
    	}
    	int duration = (Trip.size() / speed); 
    	final int[] current = {0};
    	
    	
    	timer = new Timer(duration, new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				if(current[0] != points.size()-1) {
	    				MapMarkerDot from = points.get(current[0]);
	    				MapMarkerDot to = points.get(current[0] + 1);
	    		    	MapPolygon line = new MapPolygonImpl(from,to,to);
	    		    
	    		    	tripMap.addMapPolygon(line);
    				}
    				else
    					timer.stop();
    				current[0]++;
    			}
    	});
    	timer.start();
    }
    
    
    
 
}