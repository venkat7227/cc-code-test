package com.cc.adcampaign.context;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cc.adcampaign.service.AdCampaign;

/**
 * Application level Servlet listener to initialize the in-memory Ad Campaign
 * Map
 *
 * @author venkat
 *
 */
public class AdCampaignServletContextListener implements ServletContextListener {

	public static final String AD_CAMPAIGN_IN_MEMORY_MAP = "adCampaignMap";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		event.getServletContext().setAttribute(AD_CAMPAIGN_IN_MEMORY_MAP, new ConcurrentHashMap<String, AdCampaign>());

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		event.getServletContext().removeAttribute(AD_CAMPAIGN_IN_MEMORY_MAP);

	}

}
