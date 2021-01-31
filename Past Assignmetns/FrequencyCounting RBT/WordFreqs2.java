
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 @author Daniel Scarbrough
  * @version Modified version of my last WordFreq with a new delimiter string as my last one was flawed (thanks rayna)
 * Takes user input from the argument string to get a file name then adds it to the tree, stating the size then allows the user
 * to input a few different commands, or not.
 *
 * *SOURCES*
 * Textbook: pages 439 - 458.
 *
 * Websites:
 * https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 * (this website seems to have the same code as our textbook for the most part, so im referencing both just to be safe)
 * https://www.geeksforgeeks.org/red-black-tree-set-1-introduction-2/
 * https://en.wikipedia.org/wiki/Red%E2%80%93black_tree
 * https://www.topcoder.com/community/competitive-programming/tutorials/an-introduction-to-binary-search-and-red-black-trees/
 * https://www.cs.auckland.ac.nz/software/AlgAnim/red_black.html
 *
 *
 * Classmates: Rayna provided assistance when I got stuck.
 *
 * @author Daniel Scarbrough
 * @version 2.0 (WordFreq2 is from last assignment but modified, and RedBlackTree was modified from my binarySearchTree from last year)
 *
 */
@SuppressWarnings("rawtypes")
public class WordFreqs2 {
    public static void main(String[] args){
        Scanner scanner = null;
        String filename2;
        RedBlackTree tree = new RedBlackTree<String, Integer>();
        String command;
        try {       //try to load users given filename through args[0]
            filename2 = args[0];
            File fileName = new File(filename2);
            scanner = new Scanner(fileName);
            scanner.useDelimiter("[ =+)(^*%&#$@!?/.,:;><{}\\-\t\r\n\"]+"); //Delimiter string courtesy of Rayna as my last one was flawed from HWK 4
            System.out.println("Loading file " + fileName + " Please wait . . .");

        } catch (FileNotFoundException error) {     //catches if file was not found as to not crash, then exits
            System.out.println("Error: File not Loaded. Please enter a valid file Name.");
            System.exit(0);
        }
        //searches for apostrophes and removes them from the string to aid in adding to the tree.
        while (scanner.hasNext()) {
            String words = scanner.next().toLowerCase();
            if (words.startsWith("''")) {
                words = words.substring(1);
            }
            if (words.startsWith("'")) {
                words = words.substring(1);
            }
            if (words.startsWith("[")) {
                words = words.substring(1);
            }
            if (words.endsWith("'")) {
                words = words.substring(0, words.length() - 1);
            }
            if (words.endsWith("]")) {
                words = words.substring(0, words.length() - 1);
            }
            if (tree.contains(words)) {
                int temp = (Integer) tree.get(words);
                tree.put(words, temp + 1);
            } else if (!words.isEmpty()) tree.put(words, 1);
        }
        scanner.close();

        System.out.println("This text contains " + tree.size() + " unique words");
        System.out.println("please enter a word to get its frequency, or hit enter to leave");
        Scanner input = new Scanner(System.in);
        do {    //various user inputed commands that the program will respond to, keywords provided from the assignment sheet.
            command = input.nextLine().toLowerCase();

            if(command.length() > 0) {
                String two = command.substring(1);
                if (command.startsWith("-")) {
                    tree.delete(command.substring(1));
                    System.out.println("\"" + command + "\" has been deleted.");
                }
                else if (command.startsWith("<") && command.substring(1).equals("")) {
                    System.out.print("The alphabetically-first word in the text is \"" + tree.getMinKey() + "\n");
                }
                else if (tree.contains(command)) {
                    System.out.println("\"" + command + "\" appears " + tree.get(command) + " times.");
                }
                else if (command.startsWith(">") && !command.substring(1).equals("")) {
                    System.out.print("The word \"" + tree.successor(two) + "\" comes after " + command.substring(1) + "\n");
                }
                else if (command.startsWith(">") && command.substring(1).equals("")) {
                    System.out.print("The alphabetically-last word in the text is \"" + tree.maxValue());
                }
                else if (command.startsWith("<") && !command.substring(1).equals("")) {
                    System.out.print("The word \"" + tree.predecessor(two) + "\" comes before " + command.substring(1) + "\n");
                }  else {
                    System.out.println("\"" + command + "\" is not in the text.");
                }
            }
        } while (!command.isEmpty());
        System.out.println("Goodbye!");
        System.exit(1);
    }
}


