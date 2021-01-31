import java.util.*;
/**
 * A hashtable originally used for an assignment in 261 called animalpedia, but has been tweaked for frequency counting
 * of words in certain texts.
 * @Author Daniel Scarbrough
 * @Version 2.0;
 * ------------------
 * SOURCES :
 * My own two classes from CS 261 HashTable
 *
 * Classmates:
 * Classmate Rayna provided help with modifying our old code from other class to help with this assignment to minimize
 * creation of duplicate code, as a result of this their may be some unecesary fluff from the old assignment that doesnt
 * pertain to this assignment.
 *
 * Websites:
 * https://www.baeldung.com/java-hashcode
 * https://www.geeksforgeeks.org/hashing-in-java/
 * https://www.tutorialspoint.com/java_generics/java_generics_multiple_type.htm
 * https://www.geeksforgeeks.org/difference-between-hashmap-and-hashset/
 * https://www.daniweb.com/programming/software-development/threads/439579/generic-hash-table
 * https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html
 * https://numbermonk.com/hexadecimal/2147483647/en
 *
 */
public class HashTable<K,V> {
    private ArrayList<SequentialSearchSymbolTable<K,V>> arrayList;
    private int tableSize = 0;
    private int totalhashes = 0;
    private static final int[] primes = {11, 19, 41, 79, 163, 317, 641, 1279, 2557, 5119, 10243, 20479, 40961, 81919, 163841, 327673};
    int size = primes[totalhashes];

    /**
     * Creeates a new hashTable of size Tablesize
     * @param  - size of the arraylist supporting the HashTable
     */
    public HashTable(){
        arrayList = new ArrayList<>();
        for(int i = 0; i < size; i++){
            arrayList.add(i, new SequentialSearchSymbolTable<>());
            this.tableSize++;
        }

    }

    /**
     * clears out the whole hashTable
     */
    public void clear(){
        arrayList.clear();
    }

    /**
     *Deletes an item from the hashtable when user types -(word)
     * @param key key of item to be deleted
     * @return true if deleted succesfully, or false if item wasnt found in list
     */
    public boolean delete(K key){
        int hashCode = createHash(key);
        if(arrayList.get(hashCode) == null){
            return false;
        }
        else{
            return arrayList.get(hashCode).delete(key);
        }
    }

    /**
     * rehashes the table when load factor exceeds 0.75 by using provided arraylist of prime numbers
     * assisted by rayna
     */
    public void rehash() {
        ArrayList<SequentialSearchSymbolTable<K, V>> table = arrayList;
        arrayList = new ArrayList<>();
        totalhashes++;
        size = primes[totalhashes];
        for (int i = 0; i < size; i++) {
            arrayList.add(i, new SequentialSearchSymbolTable<>());
        }
        for (SequentialSearchSymbolTable<K, V> table2: table) {
            if (table2 != null) {
                for (K key : table2.keys) {
                    if (key != null) {
                        put(key, table2.get(key));
                    }
                }
            }
        }
    }

    /**
     * a getter for the value of a given key
     * @param key - key that matches to a value want to find
     * @return the value matched with the key
     */
    public V get(K key){
        V foundValue;
        int hashCode = createHash(key);
        if(arrayList.get(hashCode) == null){
            return null;
        }
        else if(arrayList.get(hashCode).get(key) != null){
            return arrayList.get(hashCode).get(key);
        }
        return null;
    }

    /**
     *a getter that returns the size of the hashtable
     * @return the size of hashtable
     */
    public int getSize(){
        int size = 0;
        for(int i = 0; i < arrayList.size(); i++){
            size += arrayList.get(i).getSize();
        }
        return size;
    }

    /**
     *adds an item to the hashtable by hashing it, and then matching it to its value in a HashTable
     * @param key - Key to be matched to value
     * @param value - value thats matched to key
     */
    public void put(K key, V value){
        int hashCode = createHash(key);
        SequentialSearchSymbolTable<K,V> table = arrayList.get(hashCode);
        arrayList.set(hashCode, table);
        table.put(key, value);
        if(getSize() == 0){
            arrayList.add(null);
        }
        if(getSize() / size > 0.75){
            rehash();
        }
        int location = java.lang.Math.abs(hashCode%(this.tableSize));
    }

    /**
     * Check to see if a certain key exists in the table.
     * @param key to search table for
     * @return true if it exists in table, false if not.
     */public boolean contains(K key) {
        int location = createHash(key);
        if(arrayList.get(location).keys.contains(key) == true){
            return true;
        }
        else return false;

    }

    /**
     * a dedicated function to hash the key given depending on size of table
     * @param key
     * @return
     */
    private int createHash(K key) {
        int index;
        if(arrayList.size()==0){
            index=0;
            return index;
        }
        else
            index = (key.hashCode() & 0x7fffffff% size);
        return index;
    }

    private class SequentialSearchSymbolTable<K,V> {
        ArrayList<K> keys;
        ArrayList<V> values;

        /**
         * initialises the two arrays used for the keys and values
         */
        public SequentialSearchSymbolTable(){
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }

        /**
         * clears out both arrays of keys and values
         */
        public void clear(){
            keys.clear();
            values.clear();
        }

        /**
         * deletes given key and its corresponding value
         * @param key key to be deleted
         * @return true if succesful, false if key wasnt in the table
         */
        public boolean delete(K key){
            if(!keys.contains(key)){
                return false;
            }
            else{
                int location = keys.indexOf(key);
                keys.remove(key);
                values.remove(location);
                return true;
            }
        }

        /**
         * returns the value of the given key
         * @param key - key to find corresponding value of
         * @return that corresponding value, or null if false
         */
        public V get(K key){
            if(!keys.contains(key)){
                return null;
            }
            else{
                int location = keys.indexOf(key);
                return values.get(location);
            }
        }

        /**
         * getter method for the size of table
         * @return size of the table
         */
        public int getSize(){
            return keys.size();
        }

        /**
         * puts a key value pair into the table, if already exists, will replace old value
         * @param key - key to be added
         * @param value - value to be added to that key
         */
        public void put(K key, V value){
            if(keys.contains(key)){
                int location = keys.indexOf(key);
                keys.set(location, key);
                values.set(location, value);
            }
            else{
                keys.add(key);
                values.add(value);
            }
        }

        /**
         * A toString method that returns key and values
         * @return a string with key and values
         */
        @Override
        public String toString(){
            String str = "";
            for(int i = 0; i < keys.size(); i++) {

                str+= "Key: " + keys.get(i) + " Value: " +values.get(i);

            }
            return str;
        }

    }

}
