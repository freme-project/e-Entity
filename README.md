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

## Try it

#### Example #1:

Text for processing sent as a plain text.

    curl -v "http://localhost:8080/e-entity/dbpedia-spotlight?confidence=0.3&input=This+is+Germany." -H "Accept: text/turtle"

    curl -v "http://localhost:8080/e-entity/dbpedia-spotlight?confidence=0.3&input=This+is+Germany." -H "Accept: application/json-ld"

#### Example #2:

Text for processing sent as a NIF document.

    curl -X POST -d @../src/main/resources/data/data.ttl "http://localhost:8080/e-entity/dbpedia-spotlight/?confidence=0.3" -H "Content-Type: text/turtle" -H "Accept: text/turtle"

### Working on

* integration of Entityclassifier.eu

### TODOs

* support for integration of external knowledge basese
* domain specific named entity extraction
* support for French, Spanish, Italian, German, Dutch

### Complete

* first prototype against DBpedia Spotlight (11.5.2015)
  * support for English
  * `confidence` and `input` parameters
  * support for NIF (in/out)

## License

```
Copyright 2015 Deutsches Forschungszentrum für Künstliche Intelligenz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

This project uses 3rd party tools. You can find the list of 3rd party tools including their authors and licenses [here](3RD-PARTY-LICENCES).

