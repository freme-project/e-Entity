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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.freme.eservices.eentity.exceptions.BadRequestException;
import eu.freme.eservices.eentity.exceptions.ExternalServiceFailedException;
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
    
    public String callFremeNER(String text, String languageParam, String prefix, String dataset, int numLinks, ArrayList<String> rMode, String informat)
            throws ExternalServiceFailedException, BadRequestException {
        
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
//            System.out.println(text);
//            System.out.println(URLDecoder.decode(text, "UTF-8"));
            System.out.println("links: "+numLinks);
            HttpResponse<String> response = Unirest.post(fremeNERURL+"?language="+languageParam+"&dataset="+dataset+"&prefix="+URLEncoder.encode(prefix,"UTF-8")+"&numLinks="+numLinks+"&mode="+modes)
                    .header("Content-Type", informat+"; charset=UTF-8")
//                    .header("Content-Type", "text/plain; charset=UTF-8")
//                    .body(URLEncoder.encode(text, "UTF-8").replaceAll("\\+", " ")).asString();
                    .body(text).asString();
            
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
