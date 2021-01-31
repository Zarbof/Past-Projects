/**
 * Daniel Scarbrough
 * V 1.0
 * WordFreqs HW4
 * See HashTable.java for sources
 *
 * A program that takes a single commandline argument and displays stats based on the text file
 */


import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*; //various utility methods will be used, notably scanner.

public class WordFreqs {
    public static void main(String[] args) {
        String filename;
        Scanner scanner = null;
        String filename2;
        HashTable<String, Integer> hash = new HashTable();
        String command;
        try {       //try to load users given filename through args[0]
            filename2 = args[0];
            File fileName = new File(filename2);
            scanner = new Scanner(fileName);
            scanner.useDelimiter("[ +=()^%*&$#!>@.,/;:{}<>\\-\t\r\n\"]+");
            System.out.println("Loading file " + fileName + " Please wait . . .");

        }
        catch(FileNotFoundException error){     //catches if file was inputted incorrectly, tells user, then exits program
            System.out.println("Error: File not Loaded. Please enter a valid file Name.");
            System.exit(0);
        }
        while(scanner.hasNext()){
            String currentLine = scanner.next().toLowerCase();
            if(currentLine.startsWith("'")){
                currentLine = currentLine.substring(1);
            }
            if(currentLine.endsWith("'")){
                currentLine = currentLine.substring(0, currentLine.length()-1);
            }
            if(hash.contains(currentLine)){
                int temp = hash.get(currentLine);
                hash.put(currentLine, temp+1);
            }
            else if(!currentLine.isEmpty()){
                hash.put(currentLine, 1);
            }
        }
        scanner.close();

        Scanner input = new Scanner(System.in);
        System.out.println("This text contains " +hash.getSize() + " unique words");
        System.out.println("please enter a word to get its frequency, or hit enter to leave");
        do{
            command = input.nextLine();
            if(command.startsWith("-")){
                hash.delete(command.substring(1));
                String temp = command.substring(1);
                System.out.println(temp + " has been deleted");

            }
            else if(hash.contains(command)){
                System.out.println( command + " appears " + hash.get(command) + " times in the file");
            }
            else{
                if(command.isEmpty()){
                    break;
                }
                System.out.println(command + " does not appear in this dataset");
            }
        }
        while(!command.isEmpty());
        System.out.println("Goodbye!");
        System.exit(1);
    }
}
