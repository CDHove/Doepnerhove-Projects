public class ArrayList<T extends Comparable<T>> implements List<T> {

   ArrayList() {//Constructor
      isSorted = true;
      list = (T[]) new Comparable[2];
      nextOpen = 0;
      size = 0;
   }


   //---------------------------------------Add Methods---------------------------------------
   public boolean add(T element) {
      if (element == null) {
         return false;
      }
      if (nextOpen == list.length) {
         list = resize(list);
         System.out.println("resized list to " + list.length);
      }
      list[nextOpen] = element;
      size += 1;
      nextOpen += 1;
      isSorted = false;
      return true;
   }


   public boolean add(int index, T element) {
      T hold1;
      T hold2;

      //------------------------Index Check------------------------
      if (element == null) { return false; }//Checks if element is null
      if (index < size && index >= 0) {//Checks if index is within the size of list
         hold1 = list[index];
      } else {
         System.out.println("add(): Index " + index + " out of bounds for length " + size);
         return false;
      }//----------------------Index Check------------------------


      if (index == list.length-1) {//Case where index points to end of list
         list = resize(list);
         list[index] = element;
         list[index+1] = hold1;
      } else {
         hold2 = list[index + 1];
         list[index] = element;//sets the element in correct index in preparation for "bump"

         for (int i = index + 1; i < list.length; i++) {//bump
            list[i] = hold1;
            hold1 = hold2;
            try {//Tries list[i+1] and catches it if it goes out of bounds
               hold2 = list[i+1];
            } catch (ArrayIndexOutOfBoundsException e) {
               if (hold1 == null && hold2 == null) {//If both are null method should end
                  size += 1;
                  nextOpen = size;
                  isSorted = false;
                  return true;
               } else {//If one has a value there is still more elements to assign
                  list = resize(list);//  so list is resized to account for that
                  hold2 = list[i+1];
               }
            }
         }
      }
      size += 1;
      nextOpen = size;
      isSorted = false;
      return true;
   }
   //-----------------------------------------------------------------------------------------




   //---------------------------------------Get Methods---------------------------------------
   public T get(int index) {
      if (0 <= index && index < size) {
         return list[index];
      } else {
         return null;
      }
   }


   public int indexOf(T element) {
      if (isSorted) {//If list is sorted, implements a bisecting search
         int i = size/2;//Sets i up for a bisecting search
         for (int j=0; j<size; j++) {
            if (list[i].equals(element)) {
               return i;
            } else {
               if (list[i].compareTo(element) > 0) {
                  i = i/2;
               } else if (list[i].compareTo(element) < 0) {
                  i = i + (i/2);
               }
            }
         }
      } else {
         for (int i=0; i<size; i++) {
            if (list[i].equals(element)) {
               return i;
            }
         }
      }
      System.out.println("element not found");
      return -1;//Returns -1 if the element was not in the list
   }
   //-----------------------------------------------------------------------------------------




   //---------------------------------------Misc Methods---------------------------------------
   public boolean isEmpty() {
      if (list[0] != null) {
         return false;
      } else {
         return true;
      }
   }

   public void clear() {
      list = (T[]) new Comparable[2];
      size = 0;
      nextOpen = 0;
   }


   public int size() {
      return size;
   }


   public void sort() {
      int i, j;
      T n;
      list = stripEmpty(list);
      for (i = 0; i < list.length; i++) {
         if (list[i] != null) {
            n = list[i];
            for (j = i - 1; j >= 0 && n.compareTo(list[j]) < 0; j--) {
               list[j + 1] = list[j];
            }
            list[j + 1] = n;
         }
      }
      isSorted = true;
   }


   public T remove(int index) {
      if (0 <= index && index < size) {//Checks if index is in bounds
         T out = list[index];
         list[index] = null;
         list = stripEmpty(list);//Cleans up list
         return out;
      } else {
         return null;
      }
   }


   public String toString() {
      String outStr = "";
      for (int i=0; i<size; i++) {
         outStr += list[i]+"\n";
      }
      return outStr;
   }
   //------------------------------------------------------------------------------------------





   //---------------------------------------Compare-Remove Methods---------------------------------------
   public void greaterThan(T element) {//removes all n <= element
      if (isSorted) {
         for (int i=indexOf(element); i>=0; i--) {//Starts on element if sorted to speed up runtime
            if (list[i].compareTo(element) <= 0) {//Sets all <= elements in list to null
               list[i] = null;
            }
         }
      } else {
         for (int i=0; i<size; i++) {//Sets all <= elements in list to null
            if (list[i].compareTo(element) <= 0) {
               list[i] = null;
            }
         }
      }
      list = stripEmpty(list);//Reformats list to account for lost indices
   }


   public void lessThan(T element) {
      if (isSorted) {
         for (int i=indexOf(element); i<size; i++) {//Starts on element if sorted to speed up runtime
            if (list[i].compareTo(element) >= 0) {//Sets all >= elements in list to null
               list[i] = null;
            }
         }
      } else {
         for (int i=0; i<size; i++) {//Sets all >= elements in list to null
            if (list[i].compareTo(element) >= 0) {
               list[i] = null;
            }
         }
      }
      list = stripEmpty(list);//Reformats list to account for lost indices
   }


   public void equalTo(T element) {
      //Calls stripEmpty and reformats list
      for (int i=0; i<size; i++) {//Sets all != elements in list to null
         if (list[i].compareTo(element) != 0) {
            list[i] = null;
         }
      }
      list = stripEmpty(list);//Reformats list to account for lost indices
   }
   //----------------------------------------------------------------------------------------------------



   //---------------------------------------Strip and Resize---------------------------------------
   public T[] resize(T[] original) {
      T[] temp = (T[]) new Comparable[2*original.length];//Initializes a larger list
      for (int i = 0; i < original.length; i++) {
         temp[i] = original[i];//Copies over all elements into the new list
      }
      return temp;
   }


   public T[] stripEmpty(T[] original) {//Strips all null indices from a list.
      T[] temp = (T[]) new Comparable[original.length];
      int holdSize = 0;//Hold variable for length
      int next = 0;//This is a local version of nextOpen that will give the next valid index
      for (int i = 0; i < original.length; i++) {
         if (original[i] != null) {
            temp[next] = original[i];
            next += 1;//Iterates next to the next open spot in temp
            holdSize += 1;
         }
      }
      size = holdSize;//Re-assigns size to make sure if elements were deleted, the true size is maintained
      return temp;
   }
   //----------------------------------------------------------------------------------------------



   private boolean isSorted;
   private T[] list;
   private int nextOpen;
   private int size;


}
