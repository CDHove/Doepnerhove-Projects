//HashTable uses NGen and TextScan, which uses ArrayList and List. All of these files must be present in the src folder
//in order to use HashTable

public class HashTable<T> {
   NGen<T>[] table;


   //Constructor
   public HashTable() {
      table = new NGen[141];//using prime table lengths tends to result in better hashes
   }


   //-------------------------------------------Add-------------------------------------------
   public void add(T item, int hash) {
      int total = 0;
      switch (hash) {//parameter hash indicates which hash function to use, for testing only
         case 1:
            total = hash1(item);
            break;
         case 3:
            total = hash3(item);
            break;
         case 4://This should only be entered if the file you are hashing is "keywords.txt"
            total = specificHash(item);
            break;
         default://if 2 is entered or if hash isn't 1, 2, 3, or 4, use hash2 by default
            total = hash2(item);
      }

      if (table[total] == null) {
         table[total] = new NGen<T>(null, null);
         table[total].setData(item);
      }
      else {
         boolean dontAdd = false;
         NGen<T> iteration = table[total];
         if (iteration.getData().equals(item)) {
            dontAdd = true;
         }
         while (iteration.getNext() != null) {//gets to the end of the current index if there are links
            if (iteration.getData().equals(item)) {
               dontAdd = true;
            }
            iteration = iteration.getNext();
         }
         if (iteration.getData().equals(item))
            dontAdd = true;
         if (!dontAdd)
            iteration.setNext(new NGen<T>(item, null));

      }
   }
   //-------------------------------------------Add-------------------------------------------




   //--------------------------------------Hash Functions--------------------------------------
   public int hash1(T item) {
      int total = 0;

      for (int i = 0;i<=5 && i<item.toString().length();i++) {
         total += item.toString().charAt(i);
      }
      total %= table.length;
      //hash function - Take up to first 5 characters, sum them and modulo them by table length

      return total;
   }


   public int hash2(T item) {
      int total = 0;

      for (int i = 0;i<=20 && i<item.toString().length();i++) {
         total += item.toString().charAt(i);
      }
      total %= table.length;
      //hash function - Take up to first 20 characters, sum them and modulo them by table length

      return total;
   }


   public int hash3(T item) {
      int total = 0;

      total += item.toString().charAt(0);
      char endChar = item.toString().charAt(item.toString().length() - 1);
      if (endChar > total) {
         total += endChar / 2;
      } else {
         total -= endChar;
      }
      int midInd = item.toString().length() / 2;
      total += item.toString().length();
      total += item.toString().charAt(midInd);
      total %= table.length;
      //hash function - First adds first char, then if end char is larger add the end char/2
      //    or if the end char is <= the first char subtract the end char. Then add the length
      //    of the string and the mid char.

      return total;
   }


   public int specificHash(T item) {//Hash function for keywords.txt
      int total = 0;
      char endChar = item.toString().charAt(item.toString().length() - 1);
      char firstChar = item.toString().charAt(0);

      for (int i = 0;i<=20 && i<item.toString().length();i++) {
         if (item.toString().charAt(i) < total && i%2 == 1) {
            total -= item.toString().charAt(i);
         } else if (firstChar > endChar && i%2 == 0){
            total += item.toString().charAt(i);
            total -= firstChar/2;
         } else {
            total += item.toString().charAt(i)/2;
            total += (firstChar*5)/(i+2);
         }
      }
      total %= table.length;
      //hash function - Iterates through each char in the item.toString(), and then has different
      //    cases to add or subtract various amounts from total in order to achieve a more even distribution
      return total;
   }
   //--------------------------------------Hash Functions--------------------------------------




   //------------------------------------------Display------------------------------------------
   public String display() {
      String output = "";
      int longestChain = 0;
      double sumLengths = 0;//used for average collision length
      for (int i=0;i<table.length;i++) {
         int linksDeep = 0;//used for average collision length
         output += i + ": ";
         if (table[i] != null)
            output += table[i].getData();
         linksDeep = 1;


         NGen<T> iteration = table[i];
         while (iteration != null && iteration.getNext() != null) {
            output += " -> " + iteration.getNext().getData(); //links
            iteration = iteration.getNext();
            linksDeep++;
            if (linksDeep > longestChain)
               longestChain = linksDeep;
         }

         sumLengths += linksDeep;
         output += "\n";
      }
      output += "\naverage collision length: " + sumLengths/table.length;
      output += "\nlongest chain: " + longestChain;
      return output;
   }
   //------------------------------------------Display------------------------------------------




   public static void main(String[] args) {//Main is used for testing
      HashTable<String> test2 = new HashTable();
      TextScan test = new TextScan();
      String[] holdTest = test.convert("keywords.txt");
      for (int i=0; i<holdTest.length; i++) {
         test2.add(holdTest[i], 4);
      }
      System.out.println(test2.display());

      HashTable<String> gettTest = new HashTable();
      TextScan gettTest2 = new TextScan();
      String[] holdTest2 = gettTest2.convert("gettysburg.txt");
      for (int i=0; i<holdTest2.length; i++) {
         gettTest.add(holdTest2[i], 2);
      }
      System.out.println(gettTest.display());
   }
}

//written by halvo561 and doepn008