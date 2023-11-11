#include "chat.h"
#include <ncurses.h>
#include <string.h>

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

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "%s <chatServerIP> <nickname>\n", argv[0]);
        return 1;
    }

    CLIENT *clnt = create_client(argv[1]);

	//writeChat(clnt, strcat(argv[2], ": Hello, server!"));

    char *chat = getChat(clnt);
    printf("%s\n", chat);

	destroy_client(clnt);
    return 0;
}