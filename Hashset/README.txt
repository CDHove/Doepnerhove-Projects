hashset_funcs.c and hashset_main.c almost entirely written by Carsten Doepnerhove
hashset.h and prompts for hashset_funcs.c and main.c were provided
by the instructor.

This is a program that prints out a user interface for the user and
allows them to create a hashset using the functions provided. The program
should be run by compileing hashset.h, hashset_funcs.c, and hashset_main.c.

All of the functions have a discription next to them in the UI. Functions must
be entered in all lower case and in cases where additional user input is required
calls should follow the format below with no added spaces or charecters:

<function_call> <user input>

Bugs: Stress testing has found a memory leak in the add function when dealing
with very large data sets.