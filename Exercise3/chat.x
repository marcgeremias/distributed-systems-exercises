program APP{
    version CHAT_PRIMITIVES {
        void SEND_MSG(string) = 1;
        string GET_MSGS(void) = 2;
    } = 1;
} = 0x20000001;