Jace Halvorson halvo561, Carsten Doepnerhove doepn008
//
Carsten wrote solveMaze()

To run declare a maze variable such as MyMaze example = new MyMaze(). Then assign that variable 
to exapmle.makeMaze(::Rows::, ::Columns::) in order to actually create a new maze. printMaze()
and solveMaze() will then both work. (Keep in mind both printMaze() and solveMaze() have variables
that must be passed)

Example:
MyMaze example = new MyMaze();
exapmle = (t.makeMaze(::Rows::, ::Columns::));

Assumptions:
The row and column variables must be positive integers. Entering negative, non-integer, or null
elements into makeMaze will likely result in a crash.

Bugs:
No known bugs.
