# FREME e-Entity service

The service is in details described [here](https://github.com/freme-project/technical-discussion/wiki/Broker-API-Calls#user-content-e-link).
## Building

### Requirements

* Java >= 1.7
* Maven
* Git

### Compile

    cd e-Entity
    mvn clean install    

## Run through the Broker

Learn how to start the broker [here](https://github.com/freme-project/technical-discussion/wiki/Compile-FREME-from-Source).

## Try it

#### Example #1:

Text for processing sent as a plain text.

    curl -v "http://localhost:8080/e-entity/dbpedia-spotlight?confidence=0.3&input=This+is+Germany." -H "Accept: text/turtle"

    curl -v "http://localhost:8080/e-entity/dbpedia-spotlight?confidence=0.3&input=This+is+Germany." -H "Accept: application/json-ld"

#### Example #2:

Text for processing sent as a NIF document.

    curl -X POST -d @../src/main/resources/data/data.ttl "http://localhost:8080/e-entity/dbpedia-spotlight/?confidence=0.3" -H "Content-Type: text/turtle" -H "Accept: text/turtle"

### Working on

* support for French, Spanish, Italian, German, Dutch

### TODOs

* domain specific named entity extraction
* support for languages with highest priority: English, French, Spanish, Italian, German, Dutch, Japanese
* support for dataset specific entity linking
  * user can upload his data and perform entity recognition against this dataset

### Complete

* first prototype against DBpedia Spotlight (11.5.2015)
  * support for English
  * `confidence` and `input` parameters
  * support for NIF (in/out)


