import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IRoadTrip {
    private Map<String, Map<String, Integer>> graph;

    
    public IRoadTrip(String[] args) {
        graph = new HashMap<>(); // declare HashMap
        loadGraphData("capdist.csv"); // Implementation only using capdist.csv abbervations 
    }

    // Load/read graph data from a file
    private void loadGraphData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Skip the header line containing "numa,ida,numb,idb,kmdist,midist" ---> This is why we can only use abbervations instead of full named countries
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // create string list and split each line with commas 
                String country1 = parts[1]; // First country name
                String country2 = parts[3]; // Second country name
                int distance = Integer.parseInt(parts[4]); // Kilometer Distance that correspond to country1 & country2

                // Add countries and distances to the graph
                graph.putIfAbsent(country1, new HashMap<>()); // adds in country1 to Hash Map if not there yet
                graph.putIfAbsent(country2, new HashMap<>()); // adds in country2 to Hash map if not there yet
                graph.get(country1).put(country2, distance); // step to country1 and add country2 distance from country1 to the weight
                graph.get(country2).put(country1, distance); // step to country2 and add country1 distance from country2 to the weight ---> we get and put both because they have two postions in file that has flipped country abbervations
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Error for improper data file or for improper format number conversions 
        }
    }

    // Parse distance string and remove commas
    private int parseDistance(String distanceString) {
        return Integer.parseInt(distanceString.replace(",", "")); // returns an integer or NaN
    }

    // Add an edge between two countries in the graph
    private void addEdge(String country1, String country2, int distance) {
        graph.computeIfAbsent(country1, k -> new HashMap<>()).put(country2, distance);
        graph.computeIfAbsent(country2, k -> new HashMap<>()).put(country1, distance);
    }

    // Get the distance between two countries using Dijkstra's algorithm
    public int getDistance(String country1, String country2) {
        if (!graph.containsKey(country1) || !graph.containsKey(country2)) {
            return -1; // Invalid input
        }

        Map<String, Integer> distances = new HashMap<>(); // HashMap for distances
        Set<String> visited = new HashSet<>(); // HashSet for visited vertices
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get)); // queue for distances

        // Initialize distances
        for (String country : graph.keySet()) {
            distances.put(country, Integer.MAX_VALUE);
        }
        distances.put(country1, 0);

        // First one in, first one out in
        queue.add(country1); 

        while (!queue.isEmpty()) {
            String currentCountry = queue.poll();

            if (visited.contains(currentCountry)) {
                continue;
            }

            visited.add(currentCountry);

            for (Map.Entry<String, Integer> neighbor : graph.get(currentCountry).entrySet()) {
                String nextCountry = neighbor.getKey();
                int distance = neighbor.getValue();

                if (!visited.contains(nextCountry)) {
                    int newDistance = distances.get(currentCountry) + distance;
                    if (newDistance < distances.get(nextCountry)) {
                        distances.put(nextCountry, newDistance);
                        queue.add(nextCountry);
                    }
                }
            }
        }

        return distances.get(country2) == Integer.MAX_VALUE ? -1 : distances.get(country2);
    }

    // Find the shortest path between two countries
    public List<String> findPath(String country1, String country2) {
        if (!graph.containsKey(country1) || !graph.containsKey(country2)) {
            return null; // Invalid input
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances
        for (String country : graph.keySet()) {
            distances.put(country, Integer.MAX_VALUE);
        }
        distances.put(country1, 0);

        queue.add(country1);

        while (!queue.isEmpty()) {
            String currentCountry = queue.poll();

            if (visited.contains(currentCountry)) {
                continue;
            }

            visited.add(currentCountry);

            for (Map.Entry<String, Integer> neighbor : graph.get(currentCountry).entrySet()) {
                String nextCountry = neighbor.getKey();
                int distance = neighbor.getValue();

                if (!visited.contains(nextCountry)) {
                    int newDistance = distances.get(currentCountry) + distance;
                    if (newDistance < distances.get(nextCountry)) {
                        distances.put(nextCountry, newDistance);
                        previous.put(nextCountry, currentCountry);
                        queue.add(nextCountry);
                    }
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String current = country2;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);

        return path.size() > 1 ? path : null;
    }

    // Accept user input to find distances and paths
    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String country1 = scanner.nextLine().trim();
            if (country1.equalsIgnoreCase("EXIT")) {
                break;
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String country2 = scanner.nextLine().trim();
            if (country2.equalsIgnoreCase("EXIT")) {
                break;
            }

            int distance = getDistance(country1, country2);
            if (distance != -1) { // There cannot be negative weights in Dijkstra's Algorithm
                System.out.println("Route from " + country1 + " to " + country2 + ":");

                List<String> path = findPath(country1, country2);
                if (path != null) {
                    for (int i = 0; i < path.size() - 1; i++) {
                        String currentCountry = path.get(i);
                        String nextCountry = path.get(i + 1);
                        int pathDistance = graph.get(currentCountry).get(nextCountry);

                        // Print the route in the desired format
                        System.out.println("* " + currentCountry + " --> " + nextCountry + " (" + pathDistance + " km.)");
                    }
                } else {
                    System.out.println("No path found between " + country1 + " and " + country2);
                }
            } else {
                System.out.println("Invalid country names. Please enter valid country names.");
            }
        }
    }


    // Main method to run the application
    public static void main(String[] args) {
        IRoadTrip roadTrip = new IRoadTrip(args);

        // Accept user input and find distances and paths
        roadTrip.acceptUserInput();
    }
}
