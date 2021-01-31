/*
Daniel Scarbrough
CS 361
Adam Smith
HW3 - Priority Queue
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

import java.util.*;

public class PriorityQueue<E>  {             //custom priority queue class with generics
    private ArrayList<Integer> score;       //arraylist to hold the scores of the elements
    private ArrayList<E> element;       //arraylist to hold elements themselves
    private int newScore = 0;
    private E object;


    /**
     * Constructor class for Priority queue
     * @param object generic, used to receive the generic object that will be put into queue
     * @param value  used to match given object with a score/value to be sorted by
     */
    public PriorityQueue(E object, int value){
        score = new ArrayList<Integer>();
        element = new ArrayList<E>();
        this.newScore = value;
        this.object = object;
    }

    /**
     * Insert method that will insert the object into its array, and its score into a different array at same point
     * @param object object to be added
     * @param value score of object
     */
    public  void insert(E object, int value){
        score.add(value);
        element.add(object);
        swim(score.size() -1);
    }

    /**
     * Returns true or false based on if dataset is empty or not, will be used by BestFirstSearch.java
     * @return true or false based on if dataset is empty or not
     */
    public boolean isEmpty(){
        return getSize() == 0;
    }

    /**
     * remove function that will remove the correct value
     * @return removed value
     */
    public E remove(){
        int head = 0;
        int tail = (score.size() -1);
        E max = element.get(head);
        Collections.swap(score, head,tail);
        Collections.swap(element, head,tail);
        score.remove(tail);
        element.remove(tail);
        sink(0);
        return max;
    }

    /**
     * simple getter method that returns size of data
     * @return current size of both datasets
     */
    public int getSize(){
        return element.size();
    }

    /**
     * clears both arrays
     */
    public void clear(){
            element.clear();
            score.clear();
    }

    /**
     * sinks the elements so that they can be rearranged in correct order, comparisons are done to array with scores
     * then same changes are done to other array sequentially.
     * @param index index at which to sink at
     */
    private void  sink(int index){
        while(2*index +1 <= element.size()){
            int temp_left = (index * 2) +1 ;
            if(temp_left +1  < (score.size()) && (score.get(temp_left)> score.get(temp_left +1))){
                temp_left ++ ;

            }
            if((score.get(index) <= score.get(temp_left-1))){
                break;
            }
            Collections.swap(score, index, temp_left);
            Collections.swap(element, index, temp_left);
            index = temp_left;
        }
    }

    /**
     * swims element up to move it into correct spot, compared using score array, then element array is subsequently moved.
     * @param index index at which to start at
     */
    private void swim(int index){
        while(index >= 0 && index < score.size() && score.get((index-1)/2) > score.get(index)){
            Collections.swap(score, (index-1)/2, index);
            Collections.swap(element,(index-1)/2, index);
            index = (index-1)/2;
        }
    }

}
