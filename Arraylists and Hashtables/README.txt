Jace Halvorson halvo561, Carsten Doepnerhove doepn008
Carsten modified TextScan from the example provided on canvas, wrote hash3(), specificHash(), and contributed to add()
Jace wrote display(), hash1(), hash2(), and contributed to add()

To use the hash table, use the code in main
/*
HashTable<String> test2 = new HashTable();
TextScan test = new TextScan();
String[] holdTest = test.convert("keywords.txt");
for (int i=0; i<holdTest.length; i++) {
     test2.add(holdTest[i], 4);
}
System.out.println(test2.display());
*/
This block of code creates a instances of HashTable and TextScan. The convert() function from TextScan (returns a String[])
is then run and a valuable is assigned to its return value. This variable, holdTest, contains every word from the file in
the form of an array. The array is then iterated through and every element is added to the instance of HashTable using
add(T element, int hash). The 'hash' paramater (1-4) indicates which hash function to use, 'keywords.txt' should be hashed
using hash 4, the specific hash function. The display() method of this hash table is then printed.

When considering how certain elements changed how hash functions work, prime table lengths were found to have resulted in
better hashes. Also, implementing if statements in the hash functions to seperate close items from eachother was useful
expecially when adding to total in one case and subtracting from total in the other.

Assumptions: The file is in the project folder, but not inside the src. Any element being added to the hash table must be
strings or be able to be converted to a string with its toString() function. The 'hash' paramater in add() must be an integer.
HashTable uses NGen and TextScan, which uses a modified version of ArrayList and List. All of these files must be present in 
the src folder in order to use HashTable.

No known bugs or defects in the program
