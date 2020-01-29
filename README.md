###Test task for Java position.
####Technologies used :
-Spring Boot 2.2.4

-Hibernate 5

-Lombok

-MySQL

-Jackson 

The following software represents simple network model.
Application was built using RESTful Api that makes all basic 
CRUD commands with the model.
Current Api goes as follows:
#### For */network* address :
1. GET request requires no params and returns all Networks.
2. POST request requires Node entity with NETWORK type and creates whole network at once.
#### For */network/{id}* address :
1. GET request requires node id as path variable - returns node with such id.
2. DELETE request requires node id as path variable - deletes node with all it's entries.
3. PUT request requires node id as path variable and modified parent node - updates whole network.
#### For */network/{id}/entries" :
1. GET request requires node id as path variable - returns list of node entries.
#### For */network/{id}/entry" :
1. DELETE request requires node id as path variable - deletes node with all it's entries.
2. PUT request requires modified node and node id as path variable - updates single node. Node's entries and id are not updated.
#### For */network/{id}/add-node" :
1. PUT request requires node id as path variable and list of entries. Adds entries to the node with given id.
#### For */network/{id}/verify" :
1. GET request requires node id as path variable - Verifies that network is in consistent state (hierarchy is valid, no id's are duplicated).
Returns boolean value (true - network is valid, false - network is non-valid).