@echo off

REM Number of CMD terminals to open
set numTerminals=10

REM Loop to open multiple CMD terminals and execute the Java class
for /l %%i in (1,1,%numTerminals%) do (
    start cmd.exe /k java -cp "C:\Users\marcg\OneDrive - La Salle\5\PArqD\git_local\Exercise0\distributed-systems-exercises\out\production\distributed-systems-exercises" ClientServer updateonly
)

REM Loop to open multiple CMD terminals and execute the Java class
for /l %%i in (1,1,%numTerminals%) do (
    start cmd.exe /k java -cp "C:\Users\marcg\OneDrive - La Salle\5\PArqD\git_local\Exercise0\distributed-systems-exercises\out\production\distributed-systems-exercises" ClientServer readonly
)