/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "chat.h"

void appendMsg(char *msg) {
	FILE *file = fopen("chat.txt", "a");
	if (file == NULL) {
		printf("Error opening file!\n");
		exit(1);
	}

	fprintf(file, "%s\n", msg);
	printf("Message appended to file: %s\n", msg);
	fclose(file);
}

void *
send_msg_1_svc(char **argp, struct svc_req *rqstp)
{
	static char * result;

	appendMsg(*argp);

	return (void *) &result;
}

char * readFile(){

	FILE *file = fopen("chat.txt", "r");
	if (file == NULL) return ""; // Return empty string if file does not exist
	
	fseek (file, 0, SEEK_END);
	long length = ftell (file);
	fseek (file, 0, SEEK_SET);

	if (length == 0) return ""; // Return empty string if file is empty

    char *buffer = malloc(length + 1);
    if (buffer) {
        fread(buffer, 1, length, file);
        buffer[length] = '\0';  // Add null terminator at the end
        fclose(file);
    } else {
        printf("Memory allocation error!\n");
        exit(1);
    }

	// Add the null terminator at the end
	buffer[length] = '\0';
	return buffer;
}

char **
get_msgs_1_svc(void *argp, struct svc_req *rqstp)
{
	static char * result;
 
	result = readFile();

	return &result;
}
