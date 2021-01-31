import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.*;
import java.util.*;
import java.lang.Object;

/**
 * /*
 * Daniel Scarbrough
 * CS 361
 * Adam Smith
 * HW6 - Best Navigator
 * 12/1/20
 * V 1.4
 *
 * ******* Sources *********
 * People: Rayna helped me understand the search algorithm itself as well as showed me that I had to use a comparator class to help me with my openList comparisons.
 *
 * Online: I referred to many posts on Stackoverflow and other code learning websites like youtube to review documentation, and sample code to help myself
 * understand the A* algorithm  and file management systems in java.
 * Notable links:
 *     http://robotics.caltech.edu/wiki/images/e/e0/Astar.pdf
 *     https://www.baeldung.com/java-a-star-pathfinding
 *     https://www.youtube.com/watch?v=vP5TkF0xJgI
 *     https://www.youtube.com/watch?v=6TsL96NAZCo&t=638s
 *     https://www.youtube.com/watch?v=ySN5Wnu88nE&t=307s
 *     https://www.programiz.com/java-programming/linkedlist
 *     https://stackoverflow.com/questions/49638556/how-to-read-half-or-one-third-file-in-java
 *     https://stackoverflow.com/questions/62483329/storing-coordinates-in-hashset
 *     http://cs.pugetsound.edu/~aasmith/cs361/secrets/lecs/graphs.html
 *     http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#:~:text=A*'s%20Use%20of%20the,control%20A*'s%20behavior.&text=If%20h(n)%20is%20always,*%20expands%2C%20making%20it%20slower.
 *     + a few referencing articles for small refreshers like arrayList functions and manipulation etc.
 *
 *
 *
 * Textbook: I also used this classes textbook (Algorithms 4th edition) to help write the class.
 *     pages: 655
 * ************************
 *
 *
 *  */


public class Navigator{
    public static final int RADIUS = 6371;      //holds radius of earth
    public static final String FILENAME = "US-capitals.geo";    //holds filename
    public List<String> coords;
    public LinkedList<Node> tree = new LinkedList<>();  //holds all nodes
    public LinkedList<Node> closedList = new LinkedList<>();    //closed list of visited values not to be searched again
    public HashMap<String, Integer> edges;
    public static Comparator<Node> comparator = new comparator();   //used to compare nodes in open list
    public static PriorityQueue<Node> priority = new PriorityQueue<Node>(20, comparator);   //open list
    public static String firstFound = null; //global string to be printed out during special cases

    /**
     *Constructor (empty)
     */
    public Navigator(){ }

    /**
     * This calculates our heuristic for us based off coordinates provided in the file.
     * @param cityA first city
     * @param cityB second city to be calculated
     * @return the heuristic value h to be used in calculating which node to go to
     */
    public static double calcDistance(Node cityA, Node cityB) {
        double latA = cityA.latitude;
        double latB = cityB.latitude;
        double lonA = cityA.longitude;
        double lonB = cityB.longitude;
        return Math.toRadians(Math.acos(Math.sin(latA) * Math.sin(latB) + Math.cos(latA) * Math.cos(latB) * Math.cos(lonA - lonB))* RADIUS);
    }

    //finds the edge value

    /**
     * Preamble to the a star algorithm, mostly used to process nodes whcih are already adjacent to one another
     * @param city1 city1 to be tested
     * @param city2 city2 as in goal node
     * @param graph the file we started with
     * @return either returns a string into the firstFound field or returns whatever astar finds.
     */
    public String find(String city1, String city2, Scanner graph){
        //this will check to see if they are neighboring cities, if not will go into the advanced search.
        graph.useDelimiter("\t");
        int val;
        while(graph.hasNext()){
            String temp = graph.nextLine().toLowerCase(); //converts line to lowercase justincase user makes a spelling mismatch
            String[] array2 = temp.split("\t");
            if(temp.contains(city1) && temp.contains(city2)){
                val = Integer.parseInt(array2[2]);
                firstFound = "Path Found: " + city1 + " - " +city2 + " (" + Integer.toString(val) + " KM)"; //loads string into field
                return firstFound;
            }

        }
        return aStar(city1, city2);

    }

    /**
     * A star, main search algorithm which uses lots of helper classes to find the shortest/most optimal path to a node from a origin node
     * @param originCity Origin Node in string form
     * @param goal goal node in string form
     * @return
     */
    public String aStar(String originCity, String goal){
//        double f = 0;   //total so far
//        double g = 0;   //how far weve come
//        double h = 0;   //value of heuristic
        Node[] cities = findNodeFromName(originCity, goal); //takes the string nodes and converts them to their node form

        Node originNode = cities[0];
        Node goalNode = cities[1];

        priority.add(originNode);   //adds origin node to priorityqueue aka open list
        while(!priority.isEmpty()){
            Node current = priority.poll(); //adds the top of priority to the current node to be tested
            closedList.add(current);        //adds current node to closed list as weve visited it already

            if(current.cityName.equals(goalNode.cityName)){ //this is the case in whcih we have reached our goal properly and thus exits
                String str = buildPrint(goalNode);  //builds the response string then returns it
                return str;
            }
            for(int i = 0; i < current.adjacent.size(); i++){
                Node child = current.adjacent.get(i);
                double costSoFar = calcG(current, child);
                double g = current.g + costSoFar;
                child.h = calcDistance(child, goalNode);    //calculates heuristic via function call
                double tempF = calcF(child, goalNode, g);   //holds temporary f value via function call

                if(closedList.contains(child) && (tempF >= child.f)){
                    continue;
                }
                else if((!priority.contains(child) || (tempF < child.f))){
                    child.parent = current;
                    child.g = g;
                    child.f = tempF;
                    if(priority.contains(child)){
                        priority.remove(child);
                    }
                    priority.add(child);
                }
            }

        }
        return "No path was found";     //just incase all checks missed an edge case this will return a blanket no path found statement
    }

    /**
     * Builds the string which is printed at the end.
     * @param finale
     * @return
     */
    public String buildPrint(Node finale){
        ArrayList<String> stringList = new ArrayList<>();
        String str = "Path Found: ";
        stringList.add(finale.cityName);
        double tally = finale.g;
        Node current = finale.parent;
        while(current!=null){
            stringList.add(current.cityName);
            current = current.parent;
        }
        for(int i = stringList.size()-1; i > 0; i--){
            str += stringList.get(i) + " - ";
        }
        str += finale.cityName;
        return str += " ( " + tally + " KM )";

    }

    /**
     * takes a node, g value, and goal node and finds the overall F value in equation F = g + h
     * @param currentNode current node
     * @param goal goal node
     * @param g g value calculated from calcG
     * @return the f value
     */
    private double calcF(Node currentNode, Node goal, double g){
        double f;
        double h;
        h = calcDistance(currentNode, goal);
        f = g + h;

        return f;
    }

    /**
     * calculates the G value for the equation F = g + h
     * @param currentNode current node to test distance to
     * @param possibleNode  possible node to be teested for distance
     * @returns the value of G
     */
    private double calcG(Node currentNode, Node possibleNode){
        String currentCity = currentNode.cityName;
        String possibleCity = possibleNode.cityName;
        String concat = currentCity + "-" + possibleCity;

        if(edges.get(concat) != null) {
            int val = (edges.get(concat));
            Double value = (double) val;
            return value;
        }
        else{
            concat = possibleCity + "-" + currentCity;

            int val = (edges.get(concat));
            Double value = (double) val;
            return value;
        }
    }

    /**
     * nodeFromName allows us to input city names and get their respective nodes
     * @param origin origin cityname in string form
     * @param goal goal cityname in string form
     * @return a list of nodes with node[0] being origin and node[1] being goal
     */
    private Node[] findNodeFromName(String origin, String goal){
        Node[] cities = new Node[2];
        for(int i = 0; i < tree.size(); i++){
            if(tree.get(i).cityName.equalsIgnoreCase(origin)){
                cities[0] = tree.get(i); //adds origin city to first spot (0) in cities array

            }
            if(tree.get(i).cityName.equalsIgnoreCase(goal)){
                cities[1] = tree.get(i);        //adds goal city to second spot in cities array

            }
        }
        return cities;
    }

    /**
     * different NodeFromName that is usede for membership checking
     * @param city
     * @return
     */
    private boolean findNodeFromName(String city){
        Node[] cities = new Node[1];
        boolean result = false;
        for(int i = 0; i < tree.size(); i++){
            if(tree.get(i).cityName.equalsIgnoreCase(city)){
                result = true;

            }
        }
        return result;
    }

    /**
     *
     * @param scanner
     */
    public void findCords(Scanner scanner){
        //this splits the file into just the coordinates

        ArrayList<String> test = new ArrayList<>();
        while(scanner.hasNextLine()){
            test.add(scanner.nextLine());
        }
        coords = test.subList(0, 50);

    }



    /**
     *
     * @param scanner
     */
    public void findAdjacent(Scanner scanner) {
        String[] list = new String[3];
        String city1;
        String city2;
        int distance;
        int location = 0;
        edges = new HashMap<>();
        while(scanner.hasNextLine()){
            list = scanner.nextLine().split("\t");
            city1 = list[0];
            city2 = list[1];
            distance = Integer.parseInt(list[2]);

            for(int i = 0; i < tree.size(); i++){
                for(int k = 0; k <= tree.get(i).adjacent.size(); k++){
                    if(tree.get(i).cityName.equalsIgnoreCase(city1)) {
                        for(int j = 0; j < tree.size(); j++){
                            if(tree.get(j).cityName.equalsIgnoreCase(city2)){
                                location = j;
                            }
                        }
                        String concat = city1 + "-" +city2;
                        edges.put(concat, distance);
                        tree.get(i).adjacent.add(tree.get(location));//adds the second citys node to the adjacent list for city 1
                        tree.get(location).adjacent.add(tree.get(i)); //adds first city node to the adjacent list for city 2
                        break;
                    }
                    break;
                }

            }

        }
        /*for(int m = 0; m < tree.size(); m++) {
            System.out.println(tree.get(m).adjacent.size());
        }*/
        /*System.out.println(tree.get(5).adjacent.get(0).cityName);
        System.out.println(tree.get(5).adjacent.get(1).cityName);
        System.out.println(tree.get(5).adjacent.get(2).cityName);*/
    }

    /**
     *
     * @param scanner
     */
    public void createLinkedList(Scanner scanner){
        int i = 0;
        //scanner.useDelimiter("\t");
        while(scanner.hasNextLine() && i < 50){
            String line = scanner.nextLine();
            String[] data = new String[3];
            data = line.split("\t");
            Node newNode = new Node(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]));
            tree.add(newNode); //adds this new node to the tree
            i++;    //iterates as to not go past the coordinates part of the file
        }
    }


    public static void main(String[] args){
        Navigator test = new Navigator();
        String command;
        String filename = FILENAME;
        try {
            File file = new File(filename); //load file
            System.out.println("File " + filename + " has been loaded");
            System.out.println("Please enter your query separated by a dash (no spaces)");

            Scanner input = new Scanner(System.in);
            do {    //various user inputed commands that the program will respond to, keywords provided from the assignment sheet.
                command = input.nextLine().toLowerCase();   //converts to lowercase for consistant searching
                Scanner scanner = new Scanner(file);        //holds the geo file
                test.findCords(scanner);
                scanner = new Scanner(file); //resets the nextLine position of the scanner after being used.
                test.createLinkedList(scanner);     //creates tree list which is used for scanning membership as well as edge forming



                //this moves the scanner down so it starts at the edges part of the file for the next call.
                scanner.nextLine();

                test.findAdjacent(scanner); //fills the nodes adjacent field with a list of its adjacent nodes to be used in search


                if(command.length() > 0) {
                    if(command.contains("-") && !command.startsWith("-") && !command.endsWith("-")){
                        String[] temp = command.split("-");
                        if(!test.isMember(temp)){
                            System.out.println("One or more of your cities does not exist. Please check your spelling and try again in the proper format (city1-city2");
                        }
                        else if(temp[0].equalsIgnoreCase(temp[1])){
                            System.out.println("No Path Needed. Origin city is destination (0 KM)");
                        }
                        else if(command.contains("juneau") || command.contains("honolulu")){
                            System.out.println("Sorry, there is no path to or from this city from outside its state.");
                        }

                        else {
                            scanner = new Scanner(file); //resets the nextLine position of the scanner after being used.
                            String distance = test.find(temp[0], temp[1], scanner); //holds response to query
                            if(firstFound != null){ //this prints if the goal and origin node are adjacent
                                System.out.println(firstFound);
                                firstFound = null;
                                continue;
                            }
                            System.out.println(distance);   //prints query

                        }

                    }
                    else{
                        System.out.println("Unrecognized Command, Please follow format of (city1-city2)");
                    }

                }
            } while (!command.isEmpty());
            System.out.println("Goodbye!");
            System.exit(1);


        }
        catch (FileNotFoundException error) {     //catches if file was not found as to not crash, then exits
            System.out.println("Error: File not Loaded. Please enter a valid file Name.");
            System.exit(0);
        }


    }

    /**
     * Different isMember to be used to check if user input is in the tree.
     * @param city
     * @return true or false depending on result
     */
    public boolean isMember(String[] city){
        String city1 = city[0];
        String city2 = city[1];
        boolean temp = findNodeFromName(city1);
        boolean temp2 = findNodeFromName(city2);
        if(temp && temp2){
            return true;
        }
        return false;
    }

    /**
     * Node class that holds important stats about each city
     */
    private class Node {
        double longitude;
        double latitude;
        String cityName;
        ArrayList<Node> adjacent = new ArrayList<>();
        Node parent;
        double f;
        double g;       //total distance traveled so far
        double h; //heuristic  score


        public Node(String city, double lati, double longi){
            this.cityName = city;
            this.longitude = longi;
            this.latitude = lati;

        }



        /**
         * @param node node whos data want printed
         * @return returns data in ()
         */
        private String toString(Node node) {
            return (cityName + "Longitude = " + longitude + ", Latitude = " + latitude);
        }
        private void addAdj(Node node){
            if(!adjacent.contains(node)){
                adjacent.add(node);
            }
        }
    }

    /**
     * comparator class allows for the priorityqueue to work properly.
     */
    private static class comparator implements Comparator<Node> {
        @Override
        public int compare(Node cityA, Node cityB) {
            if (cityA.g + cityA.h < cityB.g + cityB.h) {
                return -1;
            }
            if (cityA.g + cityA.h > cityB.g + cityB.h) {
                return 1;
            }
            return 0;
        }
    }

}


