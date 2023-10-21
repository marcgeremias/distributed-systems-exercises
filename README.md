# Distributed Systems - Exercise 1: Questionnaire About Threads And Concurrency
L’objectiu principal d’aquest exercici és resoldre els problemes plantejats en el qüestionari sobre threads i concurrencia.

## Requirements
Java JDK Version: OpenJDK 20.0.2
## Recommendations
IDE: IntelliJ IDEA 2023.2.2

## How to run the program
Per poder provar per separat el correcte funcionament de les subactivitats
de l'Exercise1 s'han creat 3 directoris diferents de tipus Package on es troben
les classes Java necessàries per executar cada apartat, a continuació s'explica breument
com realitzar l'execució de cada apartat.

### Activity3
En aquest primer directori es troben les classes AscendantSearch, 
DescendantSearch i ParallelSearch. Per fer funcionar aquesta primera activitat s'ha
d'executar el main() de la classe ParallelSearch la qual s'encarrega de crear e iniciar 
els dos threads per recórrer l'ArrayList de manera ascendent i descendent simultàniament
i mostrar per pantalla quin thread ha trobat el valor primer.

Per defecte, l'ArrayList és de 1000 caselles on a cada casella hi ha el valor de 
l'índex, a més quan s'executi es preguntarà per terminal el valor que es vol cercar.


### Activity4_5
En aquest segon directori trobem les classes ParallelSearchArray, SearchBetween i SearchThread.
Per executar la cerca amb dins l'array amb diferents threads simplement s'ha d'executar
el main() de la classe principal ParallelSearchArray i escriure per terminal el valor a buscar
junt amb el nombre de threads a utilitzar.

Per defecte l'array és de 100000000 caselles i no s'utilitza memòria compartida, és a dir que
es realitza una còpia de l'array a cada nou thread que es crea. En cas que es vulgui modificar
l'algoritme a utilitzar s'ha de comentar l'algoritme actual i descommentar l'altre algoritme a 
utilitzar.

### Activity7_8
En aquest segon directori, es troba la classe MultithreadingSort, la qual implementa les altre dos
classes MergeSort i ParallelMergeSort. Aquestes tres classes conjunteament s'encarreguen de realitzar
el MergeSort de manera paral·lela i sequencial i comparar el temps d'execució de cada un d'ells (tal 
i com se'ns demana en el apartat 7 i 8 de l'enunciat).

Per executar aquesta activitat s'ha d'executar el main() de la classe MultithreadingSort i el propi
programa generarà un array random de X caselles (determinades per l'usuari) i realitzarà el MergeSort
d'aquest array de manera parl·lela i sequencial, mostrant per pantalla el temps d'execució de cada un
d'ells i el resultat de l'array ordenat.


### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)
