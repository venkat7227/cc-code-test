package com.cc.adcampaign.service;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cc.adcampaign.context.AdCampaignServletContextListener;
import com.cc.adcampaign.exceptionhandler.AdCampaignException;
/**
 * Ad Campaign REST Service
 * @author Venkat
 *
 */
@Path("/ad")
public class AdCampaignRestService {

	@Context
	private ServletContext context;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAdCampaign(AdCampaign adCampaignRequest) throws AdCampaignException {
		final Map<String, AdCampaign> map = (Map<String, AdCampaign>) context
				.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP);
		final String partnerId = adCampaignRequest.getPartnerId();
		if (map.containsKey(partnerId)) {
			final AdCampaign adCampaign = map.get(partnerId);
			final long activeCampainDuration = adCampaign.getCreationTimeInMillis()
					+ adCampaign.getDurationInSeconds() * 1000;
			if (activeCampainDuration > System.currentTimeMillis()) {
				throw new AdCampaignException(
						"Only one active campaign can exist for a given partner -> " + partnerId);
			}
		}
		adCampaignRequest.setCreationTimeInMillis(System.currentTimeMillis());
		map.put(partnerId, adCampaignRequest);

		return Response.ok().entity("Saved ad content for Partner -> " + partnerId + " Successfully.")
				.type("text/plain").build();
	}

	@GET
	@Path("/{partnerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public AdCampaign getAdCampaign(@PathParam("partnerId") String partnerId) throws AdCampaignException {
		final Map<String, AdCampaign> map = (Map<String, AdCampaign>) context
				.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP);
		if (!map.containsKey(partnerId)) {
			throw new AdCampaignException("Partner -> " + partnerId + " does not exist! Please enter a valid Id.");
		} else {
			final AdCampaign adCampaign = map.get(partnerId);
			final long activeCampainDuration = adCampaign.getCreationTimeInMillis()
					+ adCampaign.getDurationInSeconds() * 1000;
			if (activeCampainDuration < System.currentTimeMillis()) {
				throw new AdCampaignException("No active Campaigns exist for Partner -> " + partnerId);
			}

			return adCampaign;
		}
	}

	@GET
	@Path("/all-campaigns")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AdCampaign> getAllAdCampaigns() throws AdCampaignException {
		final Map<String, AdCampaign> map = (Map<String, AdCampaign>) context
				.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP);
		return map.values();
	}

	// For testing purpose only
	protected void setContext(ServletContext context) {
		this.context = context;
	}

}