# Distributed Systems - Exercise 2: Distributed Mutual Exclusion

L'objectiu d'aquest exercici és entendre els mecanismes de sincronització de processos i implementar dos
algorismes basats en l'aplicació d'un rellotge lògic per aconseguir l'exclusió mútua en un sistema distribuït.

Caldrà dissenyar una aplicació que contingui dos processos principals: ProcessA i ProcessB. 
En primer lloc, el ProcessA ha de crear 3 processos secundaris que anomenarem ProcessLWA1, ProcessLWA2 
i ProcessLWA3. D'altra banda, ProcessB farà el mateix però aquest cas els anomenarem ProcessLWB1, 
ProcessLWB2, ProcessLWB3. Cada procés secundari ha de funcionar en un bucle infinit que es basarà en
mostrar la seva identificació a la pantalla 10 vegades mentre espera 1 segon.

Ambdós processos principals han de funcionar a la mateixa màquina, de manera que tots els processos 
secundaris competiran pel mateix recurs compartit: la pantalla. El que es demana en aquest cas és 
implementar una política d'exclusió mútua basada en tokens entre els dos processos principals; mentre
que els processos secundàris de A s'hauràn de coordinar amb una política d'exculsió mútua basada en 
l'algorisme de Lamport i els de B, amb una política basada en l'algorisme de Ricart i Agrawala.

## Requirements
Java JDK Version: OpenJDK 20.0.2

## How to run the program

El programa està dissenyat de manera que per executar-lo, simplement s'ha de prémer el botó de run i 
automàticament s'iniciaran els dos processos principals i els respectius processos secundaris.

### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)
