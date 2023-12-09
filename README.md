CURRENTS TODOs:
- CoreNode:
  - Update everywhere = Permet a l'usuari efectuar operacions de modificació a qualsevol dels nodes 
    - / Primary Copy = Força a tots els clients a executar operacions en el mateix node

  - Active = Es passa la recepta (en el nostre cas en concret les transaccions a fer a la resta de nodes) 
    - / Passive = Es passa directament la imatge/resultat/elquesigui (en el nostre cas el hashmap resultant a les layers 1 i 2) 

  - Eager replication = No executa l'operació entrant fins que s'ha assegurat la consistència de les dades
    - / Lazy replication = S'executa la operació encara que encara no s'hagi assegurat la consistència de les dades 


  Core    = update everywhere, active, eager replication
  First   = lazy (every 10 updates), passive replication, primary backup
  Second  = lazy (every 10 seconds), passive replication, primary backup


TODOs / TO IMPROVE:
    - Message hirarchy in different classes (Message, MessageTransaction,...)
    - Message payload concept?
    - Integar != int ??


# Distributed Systems - Exercise 4: Epidemic Replication
TODO TODO TODO TODO TODO TODO TODO TODO TODO
## Requirements
TODO TODO TODO TODO TODO TODO TODO TODO TODO
## Recommendations
TODO TODO TODO TODO TODO TODO TODO TODO TODO
## How to run the program
TODO TODO TODO TODO TODO TODO TODO TODO TODO
### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)
