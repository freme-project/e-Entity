# FREME e-Entity service

The service is in details described [here](https://github.com/freme-project/technical-discussion/wiki/Broker-API-Calls#user-content-e-entity).
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


### Terminology

- **Entity Spotting:** Identify entity occurrences in the text, “Berlin” is an entity.
- **Entity Linking:** assign a link (unique identifier or URI) to the spotted entities,  “Berlin” has the identifier http://dbpedia.org/resource/Berlin
- **Entity Classification:** assign a  type to the entity occurrence  “Berlin” is of type http://dbpedia.org/ontology/City
