# FREME e-Entity service

**This repository is deprecated. It has moved to [e-services repository](https://github.com/freme-project/e-services).**

The service is in details described [here](https://github.com/freme-project/technical-discussion/wiki/Broker-API-Calls#user-content-e-entity).
## Building

### Requirements

* Java >= 1.8
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

## License

Copyright 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
Institut für Angewandte Informatik e. V. an der Universität Leipzig,
Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This project uses 3rd party tools. You can find the list of 3rd party tools including their authors and licenses [here](LICENSE-3RD-PARTY).
