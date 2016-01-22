/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.eservices.eentity.api;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.freme.eservices.eentity.exceptions.BadRequestException;
import eu.freme.eservices.eentity.exceptions.ExternalServiceFailedException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EEntityService {

    @Value("${freme.eentity.dbpediaSpotlightEndpointUrl:http://spotlight.nlp2rdf.aksw.org/spotlight}")
    private String dbpediaSpotlightURL;

//    private String fremeNERURL = "http://139.18.2.231:8080/api/entities?";

    @Value("${freme.eentity.fremeNerEndpointUrl:http://rv2622.1blu.de:8081/api/entities}")
    private String fremeNERURL;

    public String callDBpediaSpotlight(String text, String confidenceParam, String languageParam, String prefix)
            throws ExternalServiceFailedException, BadRequestException {


        try {
            
            if(prefix.equals("http://freme-project.eu/")) {
                prefix = "http://freme-project.eu/#";
            }
            
            // if confidence param is not set, the default value is 0.3
            if(confidenceParam == null) {
                confidenceParam = "0.3";
            }
           
            HttpResponse<String> response = Unirest.post(dbpediaSpotlightURL+"?f=text&t=direct&confidence="+confidenceParam+"&prefix="+URLEncoder.encode(prefix,"UTF-8"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body("i="+URLEncoder.encode(text, "UTF-8")).asString();
            
            if (response.getStatus() != HttpStatus.OK.value()) {
                if( response.getStatus() == HttpStatus.BAD_REQUEST.value() ) {
                    throw new BadRequestException(response.getBody());
                } else {
                    throw new ExternalServiceFailedException(response.getBody());
                }
            }
            String nif = response.getBody();
//            System.out.println(nif);
            return nif;
        } catch (UnirestException e) {
            throw new ExternalServiceFailedException(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EEntityService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String callFremeNER(String text, String languageParam, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat, String domain, String types)
            throws ExternalServiceFailedException, BadRequestException {
        
//        System.out.println(types);
        try {
            boolean firstmode = true;
            String modes = "";
            for(String m : rMode) {
                if(firstmode) {
                    modes=m;
                    firstmode = false;
                } else {
                    modes += ","+m;
                }                
            }
            
//            for(String m : rMode) {
//                System.out.println("mode: " + m);
//            }
//            
//            System.out.println("text: " + text);
//            System.out.println("informat: " + informat);

            
            HttpResponse<String> response = null;
            
            // linking single entity
            if(informat.equals("text/plain") && rMode.size() == 1 && modes.equals("link") ) {
                
                Model m = ModelFactory.createDefaultModel();
                Resource strRes = m.createResource(prefix+"#char=0,"+text.length());
                strRes.addProperty(RDF.type, m.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Context"));
                strRes.addProperty(RDF.type, m.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#RFC5147String"));
                strRes.addProperty(RDF.type, m.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#String"));
                strRes.addProperty(RDF.type, m.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Phrase"));
                strRes.addLiteral(m.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf"), text);
                strRes.addLiteral(m.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#beginIndex"), 0);
                strRes.addLiteral(m.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#endIndex"), text.length());
                strRes.addProperty(m.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#referenceContext"), strRes);
                strRes.addLiteral(m.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#isString"), text);
                
                String syntax = "TTL";
                StringWriter out = new StringWriter();
                m.write(out, syntax);
                String result = out.toString();

//                System.out.println("data:"+ result);
//                System.out.println("path: " + fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes);
                
                response = Unirest.post(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes)
                        .header("Content-Type", "text/turtle; charset=UTF-8")
                        .body(result).asString();
                
                if (response.getStatus() != HttpStatus.OK.value()) {
                    if( response.getStatus() == HttpStatus.BAD_REQUEST.value() ) {
                        throw new BadRequestException(response.getBody());
                    } else {
                        throw new ExternalServiceFailedException(response.getBody());
                    }
                }
                String nif = response.getBody();
                return nif;
                
            } else {
                if(domain == null && types == null) {
                    // domain and types are not specified
                    response = Unirest.post(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes)
                        .header("Content-Type", informat+"; charset=UTF-8")
                        .body(text).asString();
                } else if(domain != null && types != null) {
                    // domain and types are specified
                    response = Unirest.post(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes+"&domain="+URLEncoder.encode(domain,"UTF-8")+"&types="+URLEncoder.encode(types,"UTF-8"))
                        .header("Content-Type", informat+"; charset=UTF-8")
                        .body(text).asString();
                } else if(domain != null && types == null) {
                    // only domain is specfied
                    response = Unirest.post(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes+"&domain="+URLEncoder.encode(domain,"UTF-8"))
                        .header("Content-Type", informat+"; charset=UTF-8")
                        .body(text).asString();
                } else if(domain == null && types != null) {
                    // only types is specfied
//                    System.out.println(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes+"&types="+URLEncoder.encode(types,"UTF-8"));
                    response = Unirest.post(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes+"&types="+URLEncoder.encode(types,"UTF-8"))
                        .header("Content-Type", informat+"; charset=UTF-8")
                        .body(text).asString();                    
                }

//                    .header("Content-Type", "text/plain; charset=UTF-8")
    //                    .body(URLEncoder.encode(text, "UTF-8").replaceAll("\\+", " ")).asString();
    //            HttpResponse<String> response = Unirest.post(fremeNERURL+"language="+languageParam+"&dataset="+dataset)
    //                    .header("Content-Type", "application/x-www-form-urlencoded")
    //                    .body("i="+URLEncoder.encode(text, "UTF-8")).asString();

                if (response.getStatus() != HttpStatus.OK.value()) {
                    if( response.getStatus() == HttpStatus.BAD_REQUEST.value() ) {
                        throw new BadRequestException(response.getBody());
                    } else {
                        throw new ExternalServiceFailedException(response.getBody());
                    }
                }
                String nif = response.getBody();
                return nif;
            }
        } catch (UnirestException e) {
            throw new ExternalServiceFailedException(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EEntityService.class.getName()).log(Level.SEVERE, null, ex);
        }
//        catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(EEntityService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
        throw new ExternalServiceFailedException("External service failed to process the request.");
    }
}
