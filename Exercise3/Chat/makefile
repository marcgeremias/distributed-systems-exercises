# Makefile for chat application

CC = gcc
CFLAGS = -Wall -g
RPCGEN = rpcgen

# Source files
SERVER_SOURCES = chat_svc.c chat_server.c
CLIENT_SOURCES = chat_clnt.c chat_client.c

# Object files
SERVER_OBJS = $(SERVER_SOURCES:.c=.o)
CLIENT_OBJS = $(CLIENT_SOURCES:.c=.o)

# Executables
SERVER_EXEC = chat_server
CLIENT_EXEC = chat_client

# Targets
all: $(SERVER_EXEC) $(CLIENT_EXEC)

$(SERVER_EXEC): $(SERVER_OBJS)
	$(CC) $(CFLAGS) -o $@ $^ -ltirpc

$(CLIENT_EXEC): $(CLIENT_OBJS)
	$(CC) $(CFLAGS) -o $@ $^ -ltirpc -lncurses -lpthread

%.c: %.x
	$(RPCGEN) $<

%.o: %.c
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f $(SERVER_EXEC) $(CLIENT_EXEC) $(SERVER_OBJS) $(CLIENT_OBJS)
