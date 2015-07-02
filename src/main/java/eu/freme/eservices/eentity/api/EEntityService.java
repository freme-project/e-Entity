package eu.freme.eservices.eentity.api;

import org.springframework.http.HttpStatus;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.freme.eservices.eentity.exceptions.BadRequestException;
import eu.freme.eservices.eentity.exceptions.ExternalServiceFailedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EEntityService {
    
    private String dbpediaSpotlightURL = "http://spotlight.nlp2rdf.aksw.org/spotlight?f=text&t=direct&confidence=";
    private String fremeNERURL = "http://139.18.2.231:8080/api/entities?";
    
    public String callDBpediaSpotlight(String text, String confidenceParam, String languageParam, String prefix)
            throws ExternalServiceFailedException, BadRequestException {
        
        try {
            // if confidence param is not set, the default value is 0.3
            if(confidenceParam == null) {
                confidenceParam = "0.3";
            }
           
            HttpResponse<String> response = Unirest.post(dbpediaSpotlightURL+confidenceParam+"&prefix="+prefix)
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
            return nif;
        } catch (UnirestException e) {
            throw new ExternalServiceFailedException(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EEntityService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String callFremeNER(String text, String languageParam, String prefix, String dataset)
            throws ExternalServiceFailedException, BadRequestException {
        
        try {
//            System.out.println(text);
//            System.out.println(URLDecoder.decode(text, "UTF-8"));
            
            HttpResponse<String> response = Unirest.post(fremeNERURL+"language="+languageParam+"&dataset="+dataset)
                    .header("Content-Type", "text/plain; charset=UTF-8")
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
        }
//        catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(EEntityService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }
}
