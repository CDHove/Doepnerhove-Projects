// SOLUTION: main function for a linked list manager

#include <stdio.h>
#include <string.h>
#include "hashset.h"

int main(int argc, char *argv[]){
  int echo = 1;                                // controls echoing, 0: echo off, 1: echo on
  //if(argc > 1 && strcmp("-echo",argv[1])==0) { // turn echoing on via -echo command line option
  //  echo=1;
  //}

  printf("Hashset Application\n");
  printf("Commands:\n");
  printf("  hashcode <item>  : prints out the numeric hash code for the given key (does not change the hash set)\n");
  printf("  contains <item>  : prints the value associated with the given item or NOT PRESENT\n");
  printf("  add <item>       : inserts the given item into the hash set, reports existing items\n");
  printf("  print            : prints all items in the hash set in the order they were addded\n");
  printf("  structure        : prints detailed structure of the hash set\n");
  printf("  clear            : reinitializes hash set to be empty with default size\n");
  printf("  save <file>      : writes the contents of the hash set to the given file\n");
  printf("  load <file>      : clears the current hash set and loads the one in the given file\n");
  printf("  next_prime <int> : if <int> is prime, prints it, otherwise finds the next prime and prints it\n");
  printf("  expand           : expands memory size of hash set to reduce its load factor\n");
  printf("  bye              : exit the program\n");
  
  char cmd[128];
  hashset_t hs;
  int success;
  hashset_init(&hs, 5);

  while(1){
    printf("HS|> ");                 // print prompt
    success = fscanf(stdin,"%s",cmd); // read a command
    if(success==EOF){                 // check for end of input
      printf("\n");                   // found end of input
      break;                          // break from loop
    }


    //---------------------Bye---------------------
    if( strcmp("bye", cmd)==0 ){     // check for exit command
      if(echo){
        printf("bye\n");
      }
      break;                          // break from loop
    }
    //---------------------Bye---------------------


    //---------------------Hashcode---------------------
    else if( strcmp("hashcode", cmd)==0 ){
      fscanf(stdin,"%s",cmd);            // read string to insert
      if(echo){
        printf("hashcode %s\n",cmd);
      }

      long out = hashcode(cmd);                    
      printf("%ld\n", out);
    }
    //---------------------Hashcode---------------------


    //---------------------Contains---------------------
    else if( strcmp("contains", cmd)==0 ){
      fscanf(stdin,"%s",cmd);            // read string to insert
      if(echo){
        printf("contains %s\n",cmd);
      }

      int out = hashset_contains(&hs, cmd);
      if (out==1) {
        printf("FOUND: %s\n", cmd);
      } else {
        printf("NOT PRESENT\n");
      }
    }
    //---------------------Contains---------------------


    //---------------------Add---------------------
    else if( strcmp("add", cmd)==0 ){
      fscanf(stdin,"%s",cmd);            // read string to insert
      if(echo){
        printf("add %s\n",cmd);
      }

      int out = hashset_add(&hs, cmd);
      if (out==0) {
        printf("Item already present, no changes made\n");
      }
    }
    //---------------------Add---------------------


    //---------------------Print---------------------
    else if( strcmp("print", cmd)==0 ){
      if(echo){
        printf("%s\n",cmd);
      }

      hashset_write_items_ordered(&hs, stdout);
    }
    //---------------------Print---------------------


    //---------------------Structure---------------------
    else if( strcmp("structure", cmd)==0 ){
      if(echo){
        printf("%s\n",cmd);
      }

      hashset_show_structure(&hs);
    }
    //---------------------Structure---------------------


    //---------------------Clear---------------------
    else if( strcmp("clear", cmd)==0 ){
      if(echo){
        printf("%s\n",cmd);
      }

      hashset_free_fields(&hs);
      hashset_init(&hs, 5);
    }
    //---------------------Clear---------------------


    //---------------------Save---------------------
    else if( strcmp("save", cmd)==0 ){
      fscanf(stdin,"%s",cmd);            // read file name for save
      if(echo){
        printf("save %s\n",cmd);
      }

      FILE *fn = fopen(cmd, "w");
      if (fn == NULL) {
        printf("ERROR: could not open file '%s'\n", cmd);
      } else {
        hashset_save(&hs, cmd);
        fclose(fn);
      }
    }
    //---------------------Save---------------------


    //---------------------Load---------------------
    else if( strcmp("load", cmd)==0 ){
      fscanf(stdin,"%s",cmd);            // read file name for load
      if(echo){
        printf("load %s\n",cmd);
      }
      int out;
      out = hashset_load(&hs, cmd);
      if (out == 0) {
        printf("load failed\n");
      }
    }
    //---------------------Load---------------------


    //---------------------Next Prime---------------------
    else if( strcmp("next_prime", cmd)==0 ){
      int num;
      fscanf(stdin,"%d",&num);            // read int to insert
      if(echo){
        printf("next_prime %d\n",num);
      }

      int o_num = next_prime(num);
      printf("%d\n", o_num);
    }
    //---------------------Next Prime---------------------


    //---------------------Expand---------------------
    else if( strcmp("expand", cmd)==0 ){
      if(echo){
        printf("%s\n",cmd);
      }

      hashset_expand(&hs);
    }
    //---------------------Expand---------------------

    else{                                 // unknown command
      if(echo){
        printf("%s\n",cmd);
      }
      printf("unknown command %s\n",cmd);
    }
  }  

  // end main while loop
  hashset_free_fields(&hs);                      // clean up the list
  return 0;
}
