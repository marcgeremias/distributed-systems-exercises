#include <stdio.h>  
#include <rpc.h>
#define ERROR_ARGS "Usage: ./clientChat <chatServerIP> <nickname>\n"

// ./clientChat <chatServerIP> <nickname>
int main(int argc, char *argv[]) {
    if(argc != 3) {
        printf(ERROR_ARGS);
        return 1;
    }

    return 0;
}
