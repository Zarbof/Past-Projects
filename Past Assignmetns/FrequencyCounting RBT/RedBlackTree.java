import java.util.*;
import java.io.*;

/**
 * A program that creates a searchable red black tree. Use WordFreqs2 to operate.
 * Daniel Scarbrough
 * Version 2.0 (Original code from BinarySearchTree from 261
 * *SOURCES*
 *  * Textbook: pages 439 - 458.
 *  *
 *  * Websites:
 *  * https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 *  * (this website seems to have the same code as our textbook for the most part, so im referencing both just to be safe)
 *  * https://www.geeksforgeeks.org/red-black-tree-set-1-introduction-2/
 *  * https://en.wikipedia.org/wiki/Red%E2%80%93black_tree
 *  * https://www.topcoder.com/community/competitive-programming/tutorials/an-introduction-to-binary-search-and-red-black-trees/
 *  * https://www.cs.auckland.ac.nz/software/AlgAnim/red_black.html
 *  *
 *  *
 *  * Classmates: Rayna provided assistance when I got stuck.
 */
public class RedBlackTree<E extends Comparable<E>, V> {
    private Node root = null;
    private static final boolean red = true;
    private static final boolean black = false;

    public RedBlackTree() {
    }

    /**
     * Remakes the tree after being called by delete as it is no longer balanced
     * @param node
     * @return new remade node
     */
    private Node remake(Node node) {
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rRight(node);
        }
        if (isRed(node.right)) {
            node = rLeft(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flip(node);
        }
        node.size = getSize(node.left) + getSize(node.right) + 1;
        return node;
    }

    /**
     * private function that min calls
     * @param x
     * @return the minimum node.
     */
    private Node min(Node x) {
        if (x.left == null) {
            return x;
        } else {
            return min(x.left);
        }
    }

    /**
     * simple function that flips the colors of the nodes to whatever is the opposite of them
     * @param node
     */
    private void flip(Node node) {
        node.color = !node.color;
        node.right.color = !node.right.color;
        node.left.color = !node.left.color;


    }
    /**
     * rotates the nodes in the tree at location node to the left
     * @param node
     * @return the new node
     */
    private Node rLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = x.left.color;
        x.left.color = red;
        x.size = node.size;
        node.size = getSize(node.left) + getSize(node.right) + 1;
        return x;
    }

    /**
     * rotates the nodes in the tree at location node to the right
     * @param node
     * @return the new node
     */
    private Node rRight(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = temp.right.color;
        temp.right.color = red;
        temp.size = node.size;
        node.size = getSize(node.left) + getSize(node.right) + 1;
        return temp;
    }






    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(E key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        if (!isRed(root.left) && !isRed(root.right)){
            root.color = red;
        }
        root = delete(root, key);
        if (!isEmpty()) root.color = black;
    }

    /**
     * Delete function that removes a function and rebalanced (remakes) tree.
     * @param node node to be removed
     * @param key key to be removed
     * @return new rebalanced nodes
     *
     * **SOURCES**
     * Class Textbook had example code that was helpful
     */
    private Node delete(Node node, E key) {

        if (key.compareTo(node.data) < 0)  {
            if (!isRed(node.left) && !isRed(node.left.left))
                node = shiftLeft(node);
            node.left = delete(node.left, key);
        }
        else {
            if (isRed(node.left))
                node = rRight(node);
            if (key.compareTo(node.data) == 0 && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left))
                node = shiftRight(node);
            if (key.compareTo(node.data) == 0) {
                Node tempNode = min(node.right);
                node.data = tempNode.data;
                node.value = tempNode.value;
                node.right = deleteMinVal(node.right);
            }
            else node.right = delete(node.right, key);
        }
        return remake(node);
    }

    /**
     * shfits the red value to the right side of the tree
     * @param node
     * @return the new node
     */
    private Node shiftRight(Node node) {
        flip(node);
        if (isRed(node.left.left)) {
            node = rRight(node);
            flip(node);
        }
        return node;
    }

    /**
     * Shifts the red value to the left side of the tree
     * @param node
     * @return the new node
     */
    private Node shiftLeft(Node node) {
        flip(node);
        if (isRed(node.right.left)) {
            node.right = rRight(node.right);
            node = rLeft(node);
            flip(node);
        }
        return node;
    }

    /**
     * Deletes the minimum value in the node
     * @param node
     * @return the remade node
     */
    private Node deleteMinVal(Node node) {
        if (node.left == null)
            return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = shiftLeft(node);

        node.left = deleteMinVal(node.left);
        return remake(node);
    }


    /*public  void insert(E object) {
        counter++;
        Node newNode = new Node(object);
        if (root == null) {
            root = newNode;
            return;
        }
        Node current = root;
        Node parent = null;
        while (true) {  //goes until return statement
            parent = current;
            if (object.compareTo(current.data) < 0) {
                current = current.left;
                if (current == null) {
                    parent.left = newNode;
                    return;
                }
            } else {
                current = current.right;
                if (current == null) {
                    parent.right = newNode;
                    return;
                }
            }
        }
    }*/

    /**
     * checks the tree to see if it contains the value
     *
     * @return true if value is in tree, false if not
     */
    /*public boolean has (E value){
        Node current = root;
        while (current!=null){
            if(current.data.compareTo(value)>0){
                current=current.left;
            }else if (current.data.compareTo(value)<0){
                current=current.right;
            }
            else if (current.data.equals(value)) return true;
        }
        return false;
    }*/

    /**
     * Returns true if a value is in the red black tree
     * @param query takes value to check if it is in tree
     * @return true or false
     */
    public boolean contains(E query) {
        if (get(query) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Private helper function that takes node and a data value to match to a node.
     * @param node
     * @param data
     * @return what is stored in node
     */
    private V get(Node node, E data) {
        while (node != null) {
            if (node.data.compareTo(data) > 0) {
                node = node.left;
            } else if (node.data.compareTo(data) < 0) {
                node = node.right;
            } else if (node.data.equals(data)) {
                return node.value;
            }
        }
        return null;
    }

    /**
     * returns whatever data is stored at the node
     * @param data
     * @return whatever the node is storing
     */
    public V get(E data) {
        return get(root, data);
    }

    /**
     * getMinKey finds the minimum
     * @return the minimum node.
     */
    public E getMinKey() {
        Node min = getMinKey(root);
        return min.data;
    }

    /**
     * Private helper
     * @param node node to find min of
     * @return node which is minimum
     */
    private Node getMinKey(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * returns true if the size of the root is 0 which would mean the tree is empty
     *
     */
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        } else return false;
    }

    /**
     * private helper function that returns size of the node.
     * @param node
     * @return size of node
     */
    private int size(Node node) {
        if (node == null) {
            return 0;
        } else return node.size;
    }

    /**
     * takes size of the tree by looking at the root
     * @return
     */
    public int size() {
        return size(root);
    }

    /**
     * MaxValue Methods used for a user command in word freq.
     * @return
     */
    public E maxValue() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return maxValue(root).data;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node maxValue(Node x) {
        // assert x != null;
        if (x.right == null) return x;
        else return maxValue(x.right);
    }

    /**
     * get the size of the list
     *
     * @return the size of the list
     */
    public int getSize(Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

   /* *
     * private method to help test tree
     * @return a string with root, rootleft and rootright
    private String printTest(){
        String str = " ";
        str+= "Root: "  +root.data;
        str += ":rootL: " +root.left.data;
        str += ":rootR: " +root.right.data;
        return str;
    }*/


    /*public E findSuccessor(E value) {       //mostly identical to findpredecessor, but uses right child
        Node current = root;
        if (root == null)
            return null;
        else if (current.data.equals(value) || has(value) == false)
            return null;
        else {
            if (current.right != null) {
                current = current.right;
            } else
                return null;
            if (current != null) {
                while (current.left != null) {
                    current = current.left;
                }
            }
        }
        return current.data;
    }*/
    public E successor(E data) {
        Node successor = new Node(data);
        Node node;
        Node lastNode = root;
        if (contains(data) == false) {//was size==1
            return null;
        }

        while (lastNode != data) {
            if (lastNode.data.compareTo(data) > 0) {
                successor = lastNode;
                lastNode = lastNode.left;
            } else if (lastNode.data.compareTo(data) < 0) {
                lastNode = lastNode.right;
            } else break;
        }

        if (lastNode.right != null) {
            successor = lastNode.right;
            node = lastNode.right;
            while (node.left != null) {
                node = node.left;
                successor = node;
            }
            return successor.data;
        }
        else if(lastNode.right == null) {
            return successor.data;
        }

        return null;
    }


    /*public E findPredecessor(E value) {
        Node current = root;
        if (root == null)
            return null;
        else if (current.data.equals(value) || has(value) == false)
            return null;
        else {
            if (current.left != null) {
                current = current.left;
            } else
                return null;
            if (current != null) {
                while (current.right != null) {
                    current = current.right;
                }
            }
        }
        return current.data;
    }*/

    public E predecessor(E data) {
        Node predecessor = new Node(data);
        Node node;
        Node lastNode = root;
        //If the tree does not contain the value entered, if the value is the same as the root, or the size of the tree is 1, return null.
        if (contains(data) == false || data == root) {// || predecessor.size()==1){
            return null;
        }
        //traverse the tree from the root comparing node to value until you locate the correct node, keeping track
        //of the predecessor.
        while (lastNode != data) {
            if (lastNode.data.compareTo(data) > 0) {
                lastNode = lastNode.left;
            } else if (lastNode.data.compareTo(data) < 0) {
                predecessor = lastNode;
                lastNode = lastNode.right;
            } else break;
        }
        //When the node containing the value is located, if there is a left child, predecessor is left child. Then, while there are right children
        //predecessor becomes the right child. if there are no left children, the predecessor is from the last time we traversed
        //down the left. Print the predecessor.
        if (lastNode.left != null) {
            predecessor = lastNode.left;
            node = lastNode.left;
            while (node.right != null) {
                node = node.right;
                predecessor = node;
            }

            return predecessor.data;
        } else if (lastNode.left == null) {
            return predecessor.data;
        }
        return null;

    }
    /**
     * simple function that returns true or false depending on if node is red or not.
     * @param node
     * @return true if node is red, false if not
     */
    private boolean isRed(Node node){

        if(node==null) {    //check to make sure node isnt null
            return false;
        }
        return node.color==red;
    }



    /**
     * public put function that calls private put function
     * @param data data to be added
     * @param value value to be stored
     */
    public void put(E data, V value) {
        root = put(root, data, value);
        root.color = black;
    }

    /**
     * Put method that adds to the red black tree using the private node class
     * *SOURCES*
     * -Page 439 in the course textbook is what I used to figure out how to
     * make this method.
     *
     * @param node  node that will be added to
     * @param key   the key
     * @param value value to be added to node
     * @return the node is reutrned
     */
    private Node put(Node node, E key, V value) {
        if (node == null) return new Node(key, value, 1, red);
        int cmp = key.compareTo(node.data);

        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else node.value = value;

        //not sure about these. the book sais isRed not node.isRed but can't figureoit howto do it.
        if (isRed(node.right) && !isRed(node.left)) {
            node = rLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flip(node);
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    /**
     * private Node class used to make each node object
     */
    private class Node {
        E data;         //holds the nodes Data
        V value;
        Node left;      //holds Nodes pointer to left node (less)
        Node right;     //holds Nodes pointer to right node (bigger)
        Node parent;
        int size;
        boolean color;

        /**
         * @param data data that the node will hold
         */
        public Node(E data, V value, int size, boolean color) {     //constructor to set node data
            this.data = data;
            this.value = value;
            this.size = size;
            this.color = color;
        }

        public Node(E data) {
            this.data = data;
        }


        /**
         * @param node node whos data want printed
         * @return returns data in ()
         */
        private String toString(Node node) {
            return ("(" + node.data + ")");
        }
    }

}







