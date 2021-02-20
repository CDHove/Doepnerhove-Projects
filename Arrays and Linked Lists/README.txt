Jace Halvorson halvo561, Carsten Doepnerhove doepn008
Jace wrote most of LinkedList and most of the analysis
Carsten wrote ArrayList, some of LinkedList, and some of the analysis
To run LinkedList or ArrayList, make a declaration of a variable such as:

LinkedList newList = new LinkedList();
and any of the 13 specified methods, along with set() and getNode() can be run on newList.
void set(int index, Node<T> elem): changes list[index] to elem, not affecting the rest of the list
Node getNode(int index): returns the node at index
OR
ArrayList newList = new ArrayList();
and any of the 13 specified methods, along with resize() and stripEmpty() can be run on newList.
void resize(): makes a new array double the size of the current one and copies over every element,
effectively doubling the size of the list
void stripEmpty(): removes all null elements in between values. For example, {0, null, 2, null, 4}
would end up as {0, 2, 4}, and {0, 1, 2, null, null} would be unchanged

Assumptions:
Paramaters can't be null, if they are the method will cancel, an error will print, and the method will return null or -1.
Paramaters representing indexes can not be negative or greater than the size of the list. If they are, the method will cancel, an error will print stating the method name, index paramater, size length, and return null or -1.

Bugs:
No known bugs
