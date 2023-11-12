#include "chat.h"
#include <ncurses.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

CLIENT *clnt;

CLIENT* create_client(char *host) {
    CLIENT *clnt;

    clnt = clnt_create(host, APP, CHAT_PRIMITIVES, "udp");
    if (clnt == NULL) {
        clnt_pcreateerror(host);
        exit(1);
    }

    return clnt;
}

void destroy_client(CLIENT *clnt) {
    clnt_destroy(clnt);
}

void writeChat(CLIENT *clnt, char *msg) {
    void *result_1;

    result_1 = send_msg_1(&msg, clnt);
    if (result_1 == (void *)NULL) {
        clnt_perror(clnt, "call failed");
    }
}

char* getChat(CLIENT *clnt) {
    char **result_2;

    result_2 = get_msgs_1(NULL, clnt);
    if (result_2 == (char **)NULL) {
        clnt_perror(clnt, "call failed");
        return NULL;
    }

    return *result_2;
}

void printChat(WINDOW *top_window) {
    char *chat = getChat(clnt);
    char *line = strtok(chat, "\n");
    while (line != NULL) {
        wprintw(top_window, "%s\n", line);
        line = strtok(NULL, "\n");
    }
    free(chat);
}

void *bottomWindowThread(void *arg) {
    WINDOW *top_window = (WINDOW *)arg;

    while (true) {
        wclear(top_window); // Clear the top window
        printChat(top_window);
        wrefresh(top_window);
        refresh();
        napms(1000);
    }

    return NULL;
}


int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "%s <chatServerIP> <nickname>\n", argv[0]);
        return 1;
    }
    clnt = create_client(argv[1]);
    char input_buffer[256];
    int buffer_index = 0;


    initscr();  // Initialize ncurses
    
    // Split the console
    int height, width;
    getmaxyx(stdscr, height, width);
    WINDOW *top_window, *bottom_window;
    top_window = newwin(height - 1, width, 0, 0);
    bottom_window = newwin(1, width, height - 1, 0);
    scrollok(top_window, TRUE);  // Enable scrolling for the top window

    // Start the time update thread
    pthread_t thread;
    pthread_create(&thread, NULL, bottomWindowThread, top_window);
    memset(input_buffer, 0, sizeof(input_buffer));

    char current_char;
    while (1) {
        // Get user input in bottom window
        wclear(bottom_window);
        mvwprintw(bottom_window, 0, 0, "Input: %s", input_buffer);
        wrefresh(bottom_window);

        current_char = wgetch(bottom_window);  // Use wgetch for the bottom window

        // Handle user input
        if (current_char == '\n') {
            writeChat(clnt, input_buffer);

            wclrtoeol(bottom_window);
            wrefresh(bottom_window);
            // Scroll the bottom window to see the latest input
            wscrl(bottom_window, 1);
            // Clear the input buffer
            memset(input_buffer, 0, sizeof(input_buffer));
            buffer_index = 0;
        }else if (current_char == 127) { // handle Backspace key
            if (buffer_index > 0) {
                buffer_index--;
                input_buffer[buffer_index] = '\0';
            }
        } 
        // TODO Refactor code!! 
        // TODO Handle arrow keys :D
        // TODO Add the nickname to the msg
        else {
            // Add the character to the input buffer
            if (buffer_index < sizeof(input_buffer) - 1) {
                input_buffer[buffer_index++] = current_char;
            }
        }
    }

    // Disable echo and clean up
    //curs_set(0); // Make cursor invisible before exiting
    endwin();
    return 0;
}