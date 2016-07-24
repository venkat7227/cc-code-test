package com.cc.adcampaign.service;

import org.glassfish.jersey.server.ResourceConfig;

import com.cc.adcampaign.exceptionhandler.AdCampaignException;

/**
 * Registering Ad Campaign REST Service and Custom Exception Mapper for
 * Integration tests
 *
 * @author Venkat
 *
 */
public class ResourceRegister extends ResourceConfig {

	public ResourceRegister() {
		register(AdCampaignRestService.class);
		register(AdCampaignException.class);
	}
}