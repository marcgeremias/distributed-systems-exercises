#include "chat.h"
#include <ncurses.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

// Mutex for thread synchronization
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

// Global variables for shared data between threads
time_t start_time;
char input_buffer[256];
int buffer_index = 0;

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


void *updateChat(void *arg) {
    CLIENT *clnt = (CLIENT *)arg;

    while (true){
        char *chat = getChat(clnt);
        char *line = strtok(chat, "\n");
        int row = 0; // Start printing from the second row
        while (line != NULL) {
            mvprintw(row++, 1, "%s", line);
            line = strtok(NULL, "\n");
        }
        refresh();
        sleep(1);
        free(chat);
        clear();
    }
    
}



void print_time_elapsed(time_t start_time) {
    time_t current_time = time(NULL);
    int elapsed_seconds = difftime(current_time, start_time);
    mvprintw(0, 0, "Time Elapsed: %d seconds", elapsed_seconds);
}

void print_chat(CLIENT *clnt) {
    char *chat = getChat(clnt);
    char *line = strtok(chat, "\n");
    int row = 0; // Start printing from the second row
    while (line != NULL) {
        mvprintw(row++, 1, "%s", line);
        line = strtok(NULL, "\n");
    }
    free(chat);
}


void* updateTime(void* arg) {
    CLIENT *clnt = (CLIENT *)arg;

    while (1) {
        pthread_mutex_lock(&mutex);
        //print_time_elapsed(start_time);
        print_chat(clnt);
        pthread_mutex_unlock(&mutex);
        refresh();  // Refresh the screen
        napms(1000);  // Sleep for 1 second
    }
    return NULL;
}


int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "%s <chatServerIP> <nickname>\n", argv[0]);
        return 1;
    }
    CLIENT *clnt = create_client(argv[1]);

    // Initialize ncurses
    initscr();
    cbreak();
    keypad(stdscr, TRUE);
    curs_set(1); // Make cursor visible

    // Split the console
    int height, width;
    getmaxyx(stdscr, height, width);

    WINDOW *top_window, *bottom_window;
    top_window = newwin(height - 1, width, 0, 0);
    bottom_window = newwin(1, width, height - 1, 0);

    // Set up colors if supported
    if (has_colors()) {
        start_color();
        init_pair(1, COLOR_BLACK, COLOR_WHITE);
        wbkgd(top_window, COLOR_PAIR(1));
    }

    // Set up initial time
    start_time = time(NULL);

    // Create a thread for updating time
    pthread_t timeThread;
    //pthread_create(&timeThread, NULL, updateTime, NULL);
    pthread_create(&timeThread, NULL, updateTime, clnt);


    // String buffer for user input
    memset(input_buffer, 0, sizeof(input_buffer));

    // Main loop for user input
    int ch;
    while (1) {
        // Get user input in bottom window
        wclear(bottom_window);
        pthread_mutex_lock(&mutex);
        mvwprintw(bottom_window, 0, 0, "Input: %s", input_buffer);
        pthread_mutex_unlock(&mutex);
        wrefresh(bottom_window);

        ch = wgetch(bottom_window);  // Use wgetch for the bottom window

        // Handle user input
        pthread_mutex_lock(&mutex);
        if (ch == '\n') {
            // If Enter is pressed, do something with the input (for example, print it)
            mvwprintw(bottom_window, 0, 0, "You entered: %s", input_buffer);
            wclrtoeol(bottom_window);
            wrefresh(bottom_window);

            // Clear the input buffer
            memset(input_buffer, 0, sizeof(input_buffer));
            buffer_index = 0;
        } else if (ch == 'q') {
            break;  // Quit the program on 'q' key press
        } else {
            // Add the character to the input buffer
            if (buffer_index < sizeof(input_buffer) - 1) {
                input_buffer[buffer_index++] = ch;
            }
        }
        pthread_mutex_unlock(&mutex);
    }

    // Disable echo and clean up
    pthread_cancel(timeThread);  // Cancel the time update thread
    pthread_join(timeThread, NULL);  // Wait for the thread to finish
    curs_set(0); // Make cursor invisible before exiting
    endwin();
    return 0;
}