public class LinkedList<T extends Comparable<T>> implements List<T> {
   public LinkedList() {
      length = 0;
      start = null;
      isSorted = true;
   }

   public boolean add(T element) {
      if (element == null)
         return false;

      if (start == null) {
         start = new Node<>(element, null);
         length = 1;
         return true;
      } else {
         Node<T> current = start;
         boolean iteration = true;

         while (iteration) {
            if (current.getNext() == null) {
               current.setNext(new Node<>(element, null));
               isSorted = false;
               iteration = false;
               length++;
            } else {
               current = current.getNext();
            }
         }
         return true;
      }
   }

   public boolean add(int index, T element) {
      if (element == null)
         return false;

      Node<T> newNode = new Node<>(element, null);

      if (start == null) { // if list is empty
         System.out.println("add(): index " + index + " out of bounds for length 0");
         return false;
      } else if (index == 0) {
         newNode.setNext(start);
         start = newNode;
         isSorted = false;
         return true;
      } else {
         isSorted = false;
         Node<T> current = start;
         boolean iteration = true;
         int count = 0;

         if (index >= 0 && index < length) {
            while (iteration) {
               if (count == index - 1) {
                  newNode.setNext(current.getNext());
                  current.setNext(newNode);
                  isSorted = false;
                  length++;
                  iteration = false;
               }
               else {
                  current = current.getNext();
                  count++;
               }
            }
            return true;
         } else {
            System.out.println("add(): index " + index + " out of bounds for length " + length);
            return false;
         }
      }
   }

   public void clear() {
      start = null;
      currentNode = null;
      length = 0;
      isSorted = true;
   }

   public T get(int index) {
      int count = 0; //A count variable to check if index is reached
      T out;
      boolean getLoop = true;
      Node<T> current = start; //Initializes current to beginning of list

      if (index >= length || index < 0) {
         System.out.println("get(): index " + index + " out of bounds for length " + length);
         return null;
      } else if (index == 0) {
         return start.getData();
      } else {
         //Loop through the list until condition is satisfied
         while (getLoop) {
            if (count == index) {
               out = current.getData();
               getLoop = false;
               return out;
            } else if (count >= length || count > index) {
               return null;
            } else {
               current = current.getNext();//Iterate currentNode
               count += 1;
            }
         }
      }

      return null;
   }

   public int indexOf(T element) {
      int index;

      if (element == null) {
         return -1;
      } else if (!isSorted) {
         Node<T> current = start;
         index = 0;

         while (current != null) {
            if (current.getData().compareTo(element) == 0) {
               return index;
            }

            current = current.getNext();
            index++;
         }
      } else {//if list is sorted this method runs faster
         Node<T> current = start;
         index = 0;

         while (current != null && current.getData().compareTo(element) <= 0) {
            if (current.getData().compareTo(element) == 0) {
               return index;
            }

            current = current.getNext();
            index++;
         }
      }

      System.out.println("indexOf(): element not found");
      return -1;
   }

   public boolean isEmpty() {
      return start == null;
   }

   public int size() {
      return length;
   }

   public void sort() { //insertion sort
      if (!isSorted) {
         int i = 1;
         while (i < length) { // with both loops, iterates j = 1, (2, 1), (3, 2, 1), (4, 3, 2, 1)...
            int j = i;
            while (j > 0 && getNode(j - 1).getData().compareTo(getNode(j).getData()) > 0) {  /*"a".compareTo("b") < 0
                                                                                                   "a".compareTo("a") == 0
                                                                                                   "b".compareTo("a") > 0 */
               Node<T> currj = getNode(j);
               Node<T> prev = getNode(j - 1);
               prev.setNext(currj.getNext()); //takes currj out of list
               currj.setNext(prev); //inserts currj before prev
               if (j >= 2) {
                   getNode(j - 2).setNext(currj);
               }
               else
                  start = currj; //if currj is being inserted at the start

               j--;
            }
            i++;
         }
      }

      isSorted = true;
   }


   public T remove(int index) {
      Node<T> current = start;
      T holdT = null;
      Node<T> holdPtr = current.getNext();
      boolean removeLoop = true;
      int count = 0;

      if (index >= length || index < 0) {
         return null;
      } else if (index == 0) {
         holdT = start.getData();
         start = start.getNext();
      } else {
         while (removeLoop) {
            if (count == index) {
               holdT = current.getData();//return value
               holdPtr = current.getNext();
               current.setNext(null);
               current = getNode(count - 1);//Sets current to index i-1
               current.setNext(holdPtr);
               removeLoop = false; //end iteration
            } else if (count > index) {
               return null;
            } else {
               current = current.getNext();
               count++;
            }
         }
      }

      length--;
      return holdT;
   }

   public void greaterThan(T element) {
      Node<T> current = start;
      int index = 0;

      if (!isSorted) {
         while (current != null) {
            if (current.getData().compareTo(element) <= 0) {//remove it if its less than or equal to
               current = current.getNext(); //if one is removed, need to iterate before or current wont be in the list anymore
               remove(index);
            }
            else {
               index++;//list moves to the left when one is removed, so no need to change index
               current = current.getNext();
            }
         }
      } else {
         while (current != null && current.getData().compareTo(element) <= 0) {//as soon as it finds one thats greater, stop iterating
            remove(0);//always removes the first because it's in ascending order

            current = current.getNext();
         }
      }
   }

   public void equalTo(T element) {
      Node<T> current = start;
      int index = 0;

      if (!isSorted) {
         while (current != null) {
            if (current.getData().compareTo(element) != 0) {//remove it if its less than or equal to
               current = current.getNext(); //if one is removed, need to iterate before or current wont be in the list anymore
               remove(index);
            }
            else {
               index++;//list moves to the left when one is removed, so no need to change index
               current = current.getNext();
            }
         }
      } else {
         while (current != null) {//has to iterate through full list
            if (current.getData().compareTo(element) != 0) {
               current = current.getNext();
               remove(index);
            }
            else {
               index++;
               current = current.getNext();
            }


         }
      }
   }

   public void lessThan(T element) {
      Node<T> current = start;
      int index = 0;

      if (!isSorted) {
         while (current != null) {
            if (current.getData().compareTo(element) >= 0) {//remove it if its less than or equal to
               current = current.getNext(); //if one is removed, need to iterate before or current wont be in the list anymore
               remove(index);
            }
            else {
               index++;//list moves to the left when one is removed, so no need to change index
               current = current.getNext();
            }
         }
      } else {
         while (current != null && current.getData().compareTo(element) <= 0) {
            current = current.getNext();
         }
         while (current != null) {//as soon as it finds one thats greater, stop iterating
            remove(0);//always removes the first because it's in ascending order

            current = current.getNext();
         }
      }
   }

   public String toString() {
      currentNode = start;
      String output = "";

      while (currentNode != null) {
         output += currentNode.getData() + "\n";
         currentNode = currentNode.getNext();
      }

      output += "isSorted = " + isSorted;
      return output;
   }

   private Node<T> getNode(int index) {
      int count = 0; //A count variable to check if index is reached
      boolean getNodeLoop = true;
      currentNode = start; //Initializes currentNode to beginning of list
      Node<T> out = currentNode; //output node

      if (index >= length || index < 0) {
         System.out.println("getNode(): index " + index + " out of bounds for length " + length);
         return null;
      }
      //Loop through the list until condition is satisfied
      while (getNodeLoop) {
         if (count == index) {
            out = currentNode;
            getNodeLoop = false;
            return out;
         } else if (count > length) {
            return null;
         } else {
            currentNode = currentNode.getNext();//Iterate currentNode
            count += 1;
         }
      }

      return null;
   }

   private void set(int index, Node<T> elem) {
      int count = 0; //A count variable to check if index is reached
      boolean setLoop = true;
      Node<T> current = start;

      if (index > length || index < 0) {
         System.out.println("set(): index " + index + " out of bounds for length " + length);
      } else if (index == 0) {
         elem.setNext(start.getNext());
         start.setNext(null);
         start = elem;
         isSorted = false;
      } else {
         //Loop through the list until condition is satisfied
         while (setLoop) {
            if (count == index) {
               this.getNode(index - 1).setNext(elem);
               elem.setNext(current.getNext());
               current.setNext(null);
               isSorted = false;
               setLoop = false;
            } else {
               current = current.getNext();//Iterate current
               count += 1;
            }
         }
      }
   }

   private boolean isSorted;
   private int length;
   private Node<T> start;
   private Node<T> currentNode; //for iteration
}

//written by halvo561 and doepn008