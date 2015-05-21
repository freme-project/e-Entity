package eu.freme.eservices.eentity.api;

import org.springframework.http.HttpStatus;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.freme.eservices.eentity.exceptions.BadRequestException;
import eu.freme.eservices.eentity.exceptions.ExternalServiceFailedException;

public class EEntityService {

	String dbpediaSpotlightURL = "http://spotlight.nlp2rdf.aksw.org/spotlight?f=text&i={text}&t=direct&confidence=";

	public String callDBpediaSpotlight(String text, String confidenceParam, String languageParam)
			throws ExternalServiceFailedException, BadRequestException {

		try {
			HttpResponse<String> response = Unirest.get(dbpediaSpotlightURL+confidenceParam)
					.routeParam("text", text)
					.header("Content-type", "text/turtle").asString();

			if (response.getStatus() != HttpStatus.OK.value()) {
				if( response.getStatus() == HttpStatus.BAD_REQUEST.value() ){
					throw new BadRequestException(response.getBody());
				} else{
					throw new ExternalServiceFailedException(response.getBody());					
				}
			}

			String nif = response.getBody();
			return nif;

		} catch (UnirestException e) {
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}
}
