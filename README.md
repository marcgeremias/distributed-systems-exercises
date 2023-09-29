# Distributed Systems - Exercise 0: A naïve view
L’objectiu principal d’aquest exercici és aconseguir dissenyar una arquitectura distribuïda per un sistema que sigui capaç de compartir una variable entre N processos (anomenats servidors) mitjançant connectors de xarxa (sockets).

## Requirements
Java JDK Version: OpenJDK 20.0.2

## How to run the program
1. Per executar el programa, primer s'ha de compilar el codi font utilitzant el JDK de Java.
2. Seguidament, ja es pot executar el programa. Per fer-ho, hi ha dues opcions:

### a) Terminal
La manera més senzilla és simplement emprant l'intèrpret de comandes de Java directament. 
(Tip: En cas d'utilitzar l'IDE IntelliJ el codi intermedi es genera automàticament a la carpeta out/production/*)
Per fer-ho, primer s'ha d'executar el servidor central amb la següent comanda:
```bash
java MasterServer
```
Seguidament, ja podem executar la resta de servidors amb la següent comanda en cas que vulguem que siguin de tipus "readonly":
```bash
java ClientServer readonly
```
Altrament, si volem que siguin de tipus "updateonly" s'ha d'executar la següent comanda:
```bash
java ClientServer updateonly
```

### b) Script
L'altra manera d'executar el programa és primerament executant el Master com a l'opció (a) però a l'hora de simular els clients es pot fer ús d'un script el qual s'encarregui d'executar diverses instàncies del programa en diverses terminals, per fer-ho es pot modificar algun dels arxius de la carpeta "scripts" i fer que l'script apunti a on s'hagi creat el codi intermedi del programa.
### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)
