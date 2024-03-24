import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TripPoint {
    
    //variables
    private double lat;
    private double lon;
    private int time;
    private static ArrayList<TripPoint> trip = new ArrayList<TripPoint>();

    //Constructor
    public TripPoint(int time, double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;

    }

    //Getters
    public double getLat() {
        return this.lat;
    }
    public double getLon() {
        return this.lon;
    }
    public int getTime() {
        return this.time;
    }
    public static ArrayList<TripPoint> getTrip() {
        return (ArrayList<TripPoint>)trip.clone();
    }

    public static void readFile(String fileName) throws FileNotFoundException{

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // Skip the header row
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    int time = Integer.parseInt(parts[0]);
                    double latitude = Double.parseDouble(parts[1]);
                    double longitude = Double.parseDouble(parts[2]);
                    trip.add(new TripPoint(time, latitude, longitude));
                } else {
                    System.err.println("Incorrect file formating at line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error converting latitude/longitude to double: " + e.getMessage());
        }
    }

    private static double EarthRadiusKm = 6371.0; // Radius of the Earth in kilometers

    public static double DegreesToRadians(double degrees)
    {
        return degrees * Math.PI / 180.0;
    }

    public static double haversineDistance(TripPoint a, TripPoint b)
    {
        double lat1 = DegreesToRadians(a.getLat());
        double lon1 = DegreesToRadians(a.getLon());
        double lat2 = DegreesToRadians(b.getLat());
        double lon2 = DegreesToRadians(b.getLon());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double c = Math.pow(Math.sin(deltaLat / 2), 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.pow(Math.sin(deltaLon / 2), 2);

        double d = 2 * Math.atan2(Math.sqrt(c), Math.sqrt(1 - c));

        double distance = EarthRadiusKm * d;

        return distance;
    }

    public static double totalDistance(){
        double totalDist = 0.0;

        TripPoint point1;
        TripPoint point2;

        for(int i = 1; i < trip.size(); i++){
            point1 = trip.get(i - 1);
            point2 = trip.get(i);

            totalDist += haversineDistance(point1, point2);
        }

        return totalDist;
    }

    public static double totalTime(){

        return (trip.get(trip.size() - 1).getTime() / 60.0);
    }
}
