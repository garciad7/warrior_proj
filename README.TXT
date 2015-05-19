The solution does not fully contain extra bonus, but the server uses
multiple-thread.


The client contains 4 classes:
  WarriorFile:  Read and save warrior from .wdat file

  Warrior: Contains the information of warrior

  Client: The Main class

  WarriorClient: Process the Protocol


The server contains 5 classes:
  User: Contains the warrior information
  Combat: Stores the combat information (two warriors, actions)
  CombatField: Stores all the warriors and threads
  Server: The Main class
  UserThread: Server create a thread for each warrrior, this class
    will process the protocol











Register Protocol:
Client -> Server:  name place description
Server -> Client: welcome message      
Server -> Client: number of warriors
Server -> Client: (name place descriptoin) (name place description) ...
                       
========================================================

Battle Protocol:
Client -> Server: REQ target action
Server -> Client: ERR message  (if something error, 
               for example target does not exist)
or
Server -> target: NOTIFY id source action message
target -> Server: RSP id target action
Server -> Client: RESULT enemyAction demage message

Client -> Server: EXIT
Client -> Server: DIE
Server -> Client: QUIT name message (notify other clients that one client quited)              
Server -> Client: USER name place descriptoin  (notify other clients that one client entered) 



