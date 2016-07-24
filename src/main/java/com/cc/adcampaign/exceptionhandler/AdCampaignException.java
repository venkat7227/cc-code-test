package com.cc.adcampaign.exceptionhandler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handling and customizing Ad Campaign Exceptions
 *
 * @author venkat
 *
 */
@Provider
public class AdCampaignException extends Exception implements ExceptionMapper<AdCampaignException> {

	private static final long serialVersionUID = 1L;

	public AdCampaignException() {
		super();
	}

	public AdCampaignException(String string) {
		super(string);
	}

	@Override
	public Response toResponse(AdCampaignException exception) {
		return Response.status(400).entity(exception.getMessage()).type("application/json").build();
	}
}