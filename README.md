# FREME e-Entity service

## Building

### Requirements

* Java >= 1.7
* Maven
* Git

### Compile

    cd e-Services/e-entity
    mvn clean install    

## Run the Broker

## Try it

Example #1: text for processing sent as plain text.

    curl -v "http://localhost:8080/e-entity/dbpedia-spotlight?confidence=0.3&input=This+is+Germany." -H "Accept: text/turtle"

    curl -v "http://localhost:8080/e-entity/dbpedia-spotlight?confidence=0.3&input=This+is+Germany." -H "Accept: application/json-ld"

Example #1: text for processing as NIF document.

    curl -X POST -d @../src/main/resources/data/data.ttl "http://localhost:8080/e-entity/dbpedia-spotlight/?confidence=0.3" -H "Content-Type: text/turtle" -H "Accept: text/turtle"