struct numbers{
    int a;
    int b;
};

program ADD_PROGRAM{
    version ADD_VERS {
        int add(numbers) = 1;
    } = 1;
} = 0x20000001;