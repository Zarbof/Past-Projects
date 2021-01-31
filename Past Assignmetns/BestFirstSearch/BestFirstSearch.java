/*
Daniel Scarbrough
CS 361
Adam Smith
HW3 - Best First Search
9/28/20
V 1.0

******* Sources *********
People: I worked with the following students in this class to conceptualize and understand the assignment via multiple zoom meetings and
text conversations.
   Chloe, Rayna, Lilly, Anna VB, Eric, Danielle, and Jack.

Online: I referred to many posts on Stackoverflow and other code learning websites to review documentation, and sample code to help myself
understand the priority queue and file management systems in java.
Notable links:
    https://www.w3schools.com/java/java_files_read.asp
    https://www.geeksforgeeks.org/best-first-search-informed-search/
    https://www.freecodecamp.org/news/priority-queue-implementation-in-java/
    https://stackoverflow.com/questions/13855466/split-string-and-store-in-array-using-java/13855518
    https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html

Textbook: I also used this classes textbook (Algorithms 4th edition) to help write the PriorityQueue class.
    pages: 308 - 327
************************


 */


import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*; //various utility methods will be used, notably scanner.

public class BestFirstSearch {

    public static <E> void main(String[] args){
        String str = "";
        String filename2;
        int score = -1;         //placeholder value for score (cant be 0 or pos due to all those num's being actual possibilities)
        HashSet<E> exploredNodes = new HashSet<E>();        //a hashset to store nodes that have already been explored (closed list)
        HashMap<E, Integer> elemScore = new HashMap<E, Integer>();  //a hashset to store relationship between Object and its value(score)
        HashMap<E, ArrayList> connected = new HashMap<E, ArrayList>();//a hashset to store a node and its connected nodes

        String adjacentNode;    //adjacent string to hold raw names of connected nodes to origin
        E name = null;      //this E object will hold the name of
        PriorityQueue<E> PrioQ = new PriorityQueue(name, 10);     //instantiate our custom priority queue with placeholder values
        E originNode = null;
        Scanner scanner = null; //instantiate scanner

        try {       //try to load users given filename through args[0]
            filename2 = args[0];
            File fileName = new File(filename2);
            scanner = new Scanner(fileName);
            System.out.println("Loading file " + fileName);
        }
        catch(FileNotFoundException error){     //catches if file was inputted incorrectly, tells user, then exits program
            System.out.println("Error: File not Loaded. Please enter a valid file Name.");
            System.exit(0);
        }
            originNode = (E)scanner.nextLine();     //holds first node

            while(scanner.hasNextLine()){
                str = scanner.nextLine();           //stores current line in a string
                String inputs[] = str.split("\t");
                name = (E) inputs[0];

                if(inputs.length > 1 && !elemScore.containsValue(name)){
                    score = Integer.parseInt(inputs[1]);
                    elemScore.put(name, score);         //puts name of node and its score into table
                }
                if(inputs.length > 2 && !connected.containsValue(name)){
                    adjacentNode = inputs[2];
                    String list[] = adjacentNode.split(",");
                    ArrayList<E> listTemp = new ArrayList<>();  //makes arraylist to hold connected nodes
                    for(int i = 0; i < list.length; i++){
                        listTemp.add((E) list[i]);
                    }
                    connected.put(name,listTemp);       //puts name of node and its connected nodes into table
                }
            }



        PrioQ.insert(originNode, elemScore.get(originNode));        //inserts nodes into priority queue
        boolean passed = false;

        while(!passed && !PrioQ.isEmpty()){
            E removed = PrioQ.remove();     //stores removed value into variable, to be added to exploredNode data structure
            if(!exploredNodes.contains(removed)){
                String print = ("Exploring node " + removed);
                if(elemScore.get(removed) == 0){
                    System.out.println(print + ".... GOAL!" );
                    String print_exit = "Upon Termination, there were " + PrioQ.getSize() + " nodes ready to explore ";
                    while(!PrioQ.isEmpty()) {
                        print_exit += PrioQ.remove();
                        if (PrioQ.getSize() > 0) {
                            print_exit += ",";
                        }
                    }
                        System.out.println(print_exit);
                        passed = true;          //condition to exit while loop

                    }
                else{       //triggers if there is still work to be done
                    if(connected.get(removed) != null){
                        print += " .... adding nodes ";
                        for(int i = 0; i < connected.get(removed).size(); i++){
                            E value = (E)connected.get(removed).get(i);
                            print += value;
                            if(i != connected.get(removed).size() -1){
                               print += ",";
                            }
                            if(!exploredNodes.contains(value)){
                                PrioQ.insert(value, elemScore.get(value));
                            }
                        }
                        System.out.println(print);
                    }
                    else{
                        System.out.println(print + "... no connecting nodes to add");
                    }
                }

            }
            exploredNodes.add(removed);
        }
        if(passed == false){        //condition for if the goal node (0) cannot be reached from origin node
            System.out.println("Goal could not be reached. ");
        }
        scanner.close();
    }
}
