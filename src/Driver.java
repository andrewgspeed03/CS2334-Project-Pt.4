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
import javax.swing.JPanel;
import javax.swing.Timer;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
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
	private static final Image marker = new ImageIcon("raccoon.png").getImage();
	private static int numStops;

	
    public static void main(String[] args) throws FileNotFoundException, IOException {

    	// Read file and call stop detection
    	TripPoint.readFile("triplog.csv");
    	numStops = TripPoint.h2StopDetection();
    	ArrayList<TripPoint> trip = TripPoint.getTrip();
    	ArrayList<TripPoint> stops = new ArrayList<>(trip);
    	stops.removeAll(TripPoint.getMovingTrip());
    	
    	// Set up frame, include your name in the title
    	proFrame = new JFrame("Project 5 - Andrew Goodspeed");
        proFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        proFrame.setSize(1920, 1080);
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
        		try {
					Play(trip,stops);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		
        	}
        });
        enStop.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(enStop.isSelected())
        			showStops = true;
        		else
        			showStops = false;
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
        tripMap.setDisplayPosition(new Coordinate(35,-110), 6);
        proFrame.setVisible(true);
    }
    // Animate the trip based on selections from the GUI components


    private static void Play(ArrayList<TripPoint> Trip, ArrayList<TripPoint> Stops) throws FileNotFoundException, IOException{
    	
    	tripMap.removeAllMapMarkers();
    	tripMap.removeAllMapPolygons();
    	
    	List<Coordinate> line = new ArrayList<Coordinate>();
    	List<Coordinate> dot = new ArrayList<Coordinate>();
    	

    	Graphics g = (Graphics2D) proFrame.getGraphics();
    	g.setColor(Color.RED);
    	int period = 1000;
    	
    	for(TripPoint x: Trip) 
    		line.add(new Coordinate(x.getLat(),x.getLon()));	
    	for(TripPoint y: Stops)
    		dot.add(new Coordinate(y.getLat(),y.getLon()));
    	
    	final int[] current = {0};
    	final int[] stopCount = {0};
    	final MapMarker[] prev = new MapMarker[1];
    	period =  (speed * 1000) / line.size();

    	timer = new Timer(period, new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				
    				if(current[0] < line.size()-1) {
    					Coordinate from = line.get(current[0]);
    					Coordinate to = line.get(current[0] + 1);
	    				
	    		    	MapPolygonImpl line = new MapPolygonImpl(from,from ,to);
	    		    	MapMarker rac = new IconMarker(to , marker);
	    		    	
	    		    	line.setColor(Color.RED);
	    		    	tripMap.removeMapMarker(prev[0]);
	    		    	tripMap.addMapMarker(rac);
	    		    	tripMap.addMapPolygon(line);
	    		    	prev[0] = rac;
	    		    	if(showStops && stopCount[0] < numStops) { 
	    		    		Coordinate stop = dot.get(stopCount[0]);
	    		    		if(to.equals(stop)){
	    		    			MapMarkerDot stopMark = new MapMarkerDot(stop);
	    		    			stopMark.setBackColor(Color.RED);
	    		    			stopMark.setColor(Color.RED);
	    		    			
	    		    			tripMap.addMapMarker(stopMark);
	    		    			stopCount[0]++;
	    		    		}
	    		    	}
	    		    	
    				}
    				
    				current[0]++;
    				
    			}
    	});
    	timer.setInitialDelay(0);
    	timer.start();
    }
   
   
}