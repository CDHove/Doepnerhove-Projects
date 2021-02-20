// linked list functions, student version

#include <stdlib.h>
#include <stdio.h>
#include <string.h> // for strcpy() and strcmp()
#include "hashset.h"

// hashset_funcs.c: utility functions for operating on hash sets. Most
// functions are used in the hashset_main.c which provides an
// application to work with the functions.

// PROVIDED: Compute a simple hash code for the given character
// string. The code is "computed" by casting the first 8 characters of
// the string to a long and returning it. The empty string has hash
// code 0. If the string is a single character, this will be the ASCII
// code for the character (e.g. "A" hashes to 65).  Longer strings
// will have numbers which are the integer interpretation of up to
// their first 8 bytes.  ADVANTAGE: constant time to compute the hash
// code. DISADVANTAGE: poor distribution for long strings; all strings
// with same first 8 chars hash to the same location.
long hashcode(char key[]){
  union {
    char str[8];
    long num;
  } strnum;
  strnum.num = 0;

  for(int i=0; i<8; i++){
    if(key[i] == '\0'){
      break;
    }
    strnum.str[i] = key[i];
  }
  return strnum.num;
}


// Initialize the hash set 'hs' to have given size and item_count
// 0. Ensures that the 'table' field is initialized to an array of
// size 'table_size' and is filled with NULLs. Also ensures that the
// first/last pointers are initialized to NULL
void hashset_init(hashset_t *hs, int table_size){
    hs->item_count = 0;
    hs->table_size = table_size;
    hs->order_first = NULL;
    hs->order_last = NULL;
    hashnode_t **s = malloc(sizeof(hashnode_t) * hs->table_size);
    hs->table = s;
    for (int i=0; i<table_size; i++) {
        hs->table[i] = NULL;
    }
}


// Returns 1 if the parameter `item` is in the hash set and 0
// otherwise. Uses hashcode() and field `table_size` to determine
// which index in table to search.  Iterates through the list at that
// table index using strcmp() to check for `item`. NOTE: The
// `hashcode()` function may return positive or negative
// values. Negative values are negated to make them positive. The
// "bucket" (index in hs->table) for `item` is determined by with
// 'hashcode(key) modulo table_size'.
int hashset_contains(hashset_t *hs, char item[]){
    long it_code = hashcode(item);
    if (it_code<0) {it_code = -it_code;} //simple check if negative and switch to pos
    it_code = it_code % hs->table_size;

    hashnode_t *ptr = hs->table[it_code];
    while (ptr != NULL) {                //while loop to find item
        if (strcmp(item, ptr->item)==0) {
            return 1;
        }
        ptr = ptr->table_next;
    }
    return 0;
}


// If the item is already present in the hash set, makes no changes to
// the hash set and returns 0. hashset_contains() may be used for
// this. Otherwise determines the bucket to add `item` at via the same
// process as in hashset_contains() and adds it to the FRONT of the
// list at that table index. Adjusts the `hs->order_last` pointer to
// append the new item to the ordered list of items. If this is the
// first item added, also adjsuts the `hs->first` pointer. Updates the
// `item_count` field and returns 1 to indicate a successful addition.
//
// NOTE: Adding items at the front of each bucket list allows much
// simplified logic that does not need any looping/iteration.
int hashset_add(hashset_t *hs, char item[]){
    hashnode_t *h_ptr = hs->order_first;
    hashnode_t *n_node = malloc(sizeof(hashnode_t));

    n_node->order_next = NULL;
    n_node->table_next = NULL;
    for (int i=0; i<128; i++) {
        n_node->item[i] = '\0';
    }

    while (h_ptr!=NULL) { //check to see if item already present
        if (strcmp(item, h_ptr->item)==0) {
            return 0;
        }
        h_ptr = h_ptr->order_next;
    }

    //Find hashcode of item
    long it_code = hashcode(item);
    if (it_code<0) {it_code = -it_code;} //simple check if negative and switch to pos
    it_code = it_code % hs->table_size;

    //Copies item to the new node
    for (int i=0; i<strlen(item); i++){
        n_node->item[i] = item[i];
    }

    //checks if there is a value in the row and points n_node->table_next to it if needed
    h_ptr = hs->table[it_code];
    if (h_ptr==NULL) {
        hs->table[it_code] = n_node;
    } else {
        hs->table[it_code] = n_node;
        n_node->table_next = h_ptr;
    }
    
    //checks on item count and if order first needs to be assigned
    if (hs->item_count==0) {
        hs->order_first = n_node;
        hs->order_last = n_node;
    } else {
        h_ptr = hs->order_last;
        hs->order_last = n_node;
        h_ptr->order_next = n_node;
    }

    hs->item_count++;
    return 1;
}


// De-allocates nodes/table for `hs`. Iterates through the ordered
// list of the hash set starting at the `order_first` field and
// de-allocates all nodes in the list. Also free's the `table`
// field. Sets all relevant fields to 0 or NULL as appropriate to
// indicate that the hash set has no more usable space. Does NOT
// attempt to de-allocate the `hs` itself as it may not be
// heap-allocated (e.g. in the stack or a global).
void hashset_free_fields(hashset_t *hs) {
    hashnode_t *h_ptr = hs->order_first;
    hashnode_t *t_ptr;
    hs->order_first = NULL;
    hs->order_last = NULL;
    while (h_ptr!=NULL) {
        t_ptr = h_ptr->order_next;
        free(h_ptr);
        h_ptr = t_ptr;
    }
    free(hs->table);
    hs->item_count = 0;
    hs->table_size = 0;
    hs->table = NULL;
}


// Displays detailed structure of the hash set. Shows stats for the
// hash set as below including the load factor (item count divided
// by table_size) to 4 digits of accuracy.  Then shows each table
// array index ("bucket") on its own line with the linked list of
// items in the bucket on the same line. 
// 
// EXAMPLE:
// item_count: 4
// table_size: 5
// order_first: Rick
// order_last : Tinyrick
// load_factor: 0.8000
// [ 0] : {7738144525137111380 Tinyrick >>NULL} 
// [ 1] : 
// [ 2] : 
// [ 3] : {125779953153363 Summer >>Tinyrick} {1801677138 Rick >>Morty} 
// [ 4] : {521644699469 Morty >>Summer} 
//
// NOTES:
// - Uses format specifier "[%2d] : " to print the table indices
// - Nodes in buckets have the following format:
//   {1415930697 IceT >>Goldenfold}
//    |          |       |        
//    |          |       +-> order_next->item OR NULL if last node
//    |          +->`item` string     
//    +-> hashcode("IceT"), print using format "%ld" for 64-bit longs
// 
void hashset_show_structure(hashset_t *hs) {
    hashnode_t *ptr;
    printf("item_count: %d\n", hs->item_count);
    printf("table_size: %d\n", hs->table_size);

    //print order first
    if (hs->order_first->item==NULL) {
        printf("order_first: NULL\n");
    } else {
        printf("order_first: %s\n", hs->order_first->item);
    }

    //print order last
    if (hs->order_last->item==NULL) {
        printf("order_last : NULL\n");
    } else {
        printf("order_last : %s\n", hs->order_last->item);
    }

    //loop through hashset and print all values in the correct format
    printf("load_factor: %.4f\n", (1.0*hs->item_count) / hs->table_size);
    for (int i=0; i<hs->table_size; i++) {
        printf("[%2d] : ", i);
        ptr = hs->table[i];
        while (ptr!=NULL) {
            if (ptr->order_next->item==NULL) {
                printf("{%ld %s >>NULL} ", hashcode(ptr->item), ptr->item);
            } else {
                printf("{%ld %s >>%s} ", hashcode(ptr->item), ptr->item, ptr->order_next->item);
            }
            ptr = ptr->table_next;
        }
        printf("\n");
    }
}


// Outputs all elements of the hash set according to the order they
// were added. Starts at the `order_first` field and iterates through
// the list defined there. Each item is printed on its own line
// preceded by its add position with 1 for the first item, 2 for the
// second, etc. Prints output to `FILE *out` which should be an open
// handle. NOTE: the output can be printed to the terminal screen by
// passing in the `stdout` file handle for `out`.
void hashset_write_items_ordered(hashset_t *hs, FILE *out) {
    hashnode_t *ptr = hs->order_first;
    int count = 1;
    while (ptr!=NULL) {
        fprintf(out, "  %d %s\n", count, ptr->item);
        count++;
        ptr = ptr->order_next;
    }
}


// Writes the given hash set to the given `filename` so that it can be
// loaded later.  Opens the file and writes its 'table_size' and
// 'item_count' to the file. Then uses the hashset_write_items_ordered()
// function to output all items in the hash set into the file.
// EXAMPLE FILE:
// 
// 5 6
//   1 Rick
//   2 Morty
//   3 Summer
//   4 Jerry
//   5 Beth
//   6 Tinyrick
// 
// First two numbers are the 'table_size' and 'item_count' field and
// remaining text is the output of hashset_write_items_ordered();
// e.g. insertion position and item.
void hashset_save(hashset_t *hs, char *filename) {
    FILE *in_f = fopen(filename, "w");
    if (in_f == NULL) {
        printf("ERROR: could not open file '%s'\n", filename);
    } else {
        fprintf(in_f, "%d %d\n", hs->table_size, hs->item_count);
        hashset_write_items_ordered(hs, in_f);
        fclose(in_f);
    }

    
}


// Loads a hash set file created with hashset_save(). If the file
// cannot be opened, prints the message
// 
// ERROR: could not open file 'somefile.hs'
//
// and returns 0 without changing anything. Otherwise clears out the
// current hash set `hs`, initializes a new one based on the size
// present in the file, and adds all items from the file into the new
// hash set. Ignores the indices at the start of each line and uses
// hashset_add() to insert items in the order they appear in the
// file. Returns 0 on successful loading. This function does no error
// checking of the contents of the file so if they are corrupted, it
// may cause an application to crash or loop infinitely.
int hashset_load(hashset_t *hs, char *filename) {
    FILE *in_f = fopen(filename, "r");
    //check if file is read
    if (in_f == NULL) {
        printf("ERROR: could not open file '%s'\n", filename);
        return 0;
    }

    hashset_free_fields(hs);
    int hold;
    int sad;
    char item[128];
    fscanf(in_f, "%d %d", &hs->table_size, &hold);
    hashset_init(hs, hs->table_size);

    for (int i=0; i<hold; i++) {
        fscanf(in_f, "%d %s", &sad, item);
        hashset_add(hs, item);    
    }
    fclose(in_f);
    return 1;
}


// If 'num' is a prime number, returns 'num'. Otherwise, returns the
// first prime that is larger than 'num'. Uses a simple algorithm to
// calculate primeness: check if any number between 2 and (num/2)
// divide num. If none do, it is prime. If not, tries next odd number
// above num. Loops this approach until a prime number is located and
// returns this. Used to ensure that hash table_size stays prime which
// theoretically distributes elements better among the array indices
// of the table.
int check_prime(int num) {
    for (int i=2; i<=(num/2); i++) {
        if (num%i==0) {
            return 0;
        }
    }
    return 1;
}

int next_prime(int num) {
    if (check_prime(num)==1) {
        return num;
    } else if ((num%2)==1) {
        return next_prime(num+2);
    } else if ((num%2)==0) {
        return next_prime(num+1);
    }
    
    return -1;
}


// Allocates a new, larger area of memory for the `table` field and
// re-adds all current items to it. The size of the new table is
// next_prime(2*table_size+1) which keeps the size prime.  After
// allocating the new table, all table entries are initialized to NULL
// then the old table is iterated through and all items are added to
// the new table according to their hash code. The memory for the old
// table is de-allocated and the new table assigned to the hash set
// fields "table" and "table_size".  This function increases
// "table_size" while keeping "item_count" the same thereby reducing
// the load of the hash table. Ensures that the memory associated with
// the old table is free()'d. Makes NO special effort to preserve old
// nodes: re-adds everything into the new table and then frees the old
// one along with its nodes. Uses functions such as hashset_init(),
// hashset_add(), hashset_free_fields() to accomplish the transfer.
void hashset_expand(hashset_t *hs) {
    /*  this function creates a hold hashset to be able to read
        in things from the main hashset with add(). after all 
        elements in the main table are copied over, main is nuked
        and re-init. Then, hold shoves its values into main and 
        hold is then nuked.                                        */

    //initialize hold set
    hashset_t *h_set = malloc(sizeof(hashset_t));
    int n_siz = next_prime(2*hs->table_size+1);
    hashset_init(h_set, n_siz);


    //copy over values from hs into hold set
    hashnode_t *ptr = hs->order_first;
    while (ptr!=NULL) {
        hashset_add(h_set, ptr->item);
        ptr = ptr->order_next;
    }
    

    //nuke hs and re init it
    hashset_free_fields(hs);//nuke
    hashset_init(hs, h_set->table_size);

    //copy back values from hold to hs
    ptr = h_set->order_first;
    while (ptr!=NULL) {
        hashset_add(hs, ptr->item);
        ptr = ptr->order_next;
    }

    //nuke and free all of h_set
    hashset_free_fields(h_set);
    free(h_set);
}





