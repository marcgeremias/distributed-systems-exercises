#include "chat.h"
#include <ncurses.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

#define MAX_MESSAGE_LENGTH 256

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

void write_chat(CLIENT *clnt, char *msg, char *user_name) {
    void *result_1;
    char *final_msg;

    // Built the msg
    final_msg = (char *)malloc(MAX_MESSAGE_LENGTH * sizeof(char));
    if (final_msg == NULL) return;
    snprintf(final_msg, MAX_MESSAGE_LENGTH, "%s: %s", user_name, msg);
    
    // Send the msg
    result_1 = send_msg_1(&final_msg, clnt);
    if (result_1 == (void *)NULL) {
        clnt_perror(clnt, "call failed");
    }
    free(final_msg);
}

char* get_chat(CLIENT *clnt) {
    char **result_2;

    result_2 = get_msgs_1(NULL, clnt);
    if (result_2 == (char **)NULL) {
        clnt_perror(clnt, "call failed");
        return NULL;
    }

    return *result_2;
}

void print_chat(WINDOW *top_window) {
    char *chat = get_chat(clnt);
    char *line = strtok(chat, "\n");
    while (line != NULL) {
        wprintw(top_window, "%s\n", line);
        line = strtok(NULL, "\n");
    }
    free(chat);
}

void *top_window_thread(void *arg) {
    WINDOW *top_window = (WINDOW *)arg;
    while (true) {
        wclear(top_window);
        print_chat(top_window);
        wrefresh(top_window);
        refresh();
        napms(1000);
    }
    return NULL;
}

void start_user_input_loop(WINDOW *bottom_window, char* user_name){
    char input_buffer[MAX_MESSAGE_LENGTH];
    int buffer_index = 0;
    char current_char;

    memset(input_buffer, 0, sizeof(input_buffer));

    while (1) {
        wclear(bottom_window);
        mvwprintw(bottom_window, 0, 0, "%s --> %s",user_name, input_buffer);
        wrefresh(bottom_window);

        current_char = wgetch(bottom_window);
        // Handle user input
        if (current_char == '\n') {
            write_chat(clnt, input_buffer, user_name);
            wclrtoeol(bottom_window);
            wrefresh(bottom_window);
            memset(input_buffer, 0, sizeof(input_buffer));
            buffer_index = 0;
        } else if (current_char == 127) { // Delete KEY
            if (buffer_index > 0) {
                buffer_index--;
                input_buffer[buffer_index] = '\0';
            }
        } else {
            // Add the character to the input buffer
            if (buffer_index < sizeof(input_buffer) - 1) {
                input_buffer[buffer_index++] = current_char;
            }
        }
    }
}

void set_ncurses_windows(WINDOW **top_window, WINDOW **bottom_window){
    int height, width;
    getmaxyx(stdscr, height, width);
    *top_window = newwin(height - 1, width, 0, 0);
    *bottom_window = newwin(1, width, height - 1, 0);
    scrollok(*top_window, TRUE);
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "%s <chatServerIP> <nickname>\n", argv[0]);
        return 1;
    }
    clnt = create_client(argv[1]);

    initscr();  // Initialize ncurses

    WINDOW *top_window, *bottom_window;
    set_ncurses_windows(&top_window, &bottom_window);
    // Start the 1s chat refresh
    pthread_t thread;
    pthread_create(&thread, NULL, top_window_thread, top_window);
    // Start user input loop
    start_user_input_loop(bottom_window, argv[2]);

    endwin();   // Kill ncurses

    return 0;
}