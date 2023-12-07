# CS 245 (Fall 2023) - Assignment 3 - IRoadTrip

Can you plan the path for a road trip from one country to another?

Change the java source code, but do not change the data files. See Canvas for assignment details.

- [ ] getDistance Method:
    * The getDistance method uses Dijkstra's algorithm to find the shortest distance between two countries.
    * It initializes distances to all countries as Integer.MAX_VALUE except for the starting country, which is set to 0.
    * It maintains a priority queue (queue) to process countries with smaller distances first.
    * The algorithm continues until the priority queue is empty. In each iteration, it explores neighboring countries, updates their distances if a shorter path is found, and adds them to the priority queue.
    * The final result is the distance from the starting country to the destination country.
- [ ] findPath Method:
    * The findPath method uses Dijkstra's algorithm to find the shortest path between two countries.
    * It maintains a previous map to keep track of the preceding country in the shortest path.
    * After running Dijkstra's algorithm, it reconstructs the path from the destination country back to the starting country using the previous map.
    * The resulting path is returned as a list of countries.
- [ ] Both methods share a similar structure and leverage the principles of Dijkstra's algorithm to explore the graph and find the shortest distance and path.

- [ ] I only loaded/read capdist.csv to calculate the sum of shortest path for output instead of showing what paths lead to B country 