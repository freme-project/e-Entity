package eu.freme.eservices.eentity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="")
public class BadRequestException extends RuntimeException{

	public BadRequestException(String msg){
		super(msg);
	}
}
