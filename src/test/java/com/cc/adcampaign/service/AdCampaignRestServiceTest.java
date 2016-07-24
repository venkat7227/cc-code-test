package com.cc.adcampaign.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cc.adcampaign.context.AdCampaignServletContextListener;
import com.cc.adcampaign.exceptionhandler.AdCampaignException;
@RunWith(MockitoJUnitRunner.class)
public class AdCampaignRestServiceTest {

	AdCampaignRestService service;
	AdCampaign adcampaign1,adcampaign2,adcampaign3;
	Map<String,AdCampaign> adCampaignMap;
	@Mock ServletContext context;

	@Before
	public void setUp() throws Exception {
		adcampaign1 = new AdCampaign("partner1", 10, "content1");
		adcampaign2 = new AdCampaign("partner2", 10, "content2");
		adcampaign3 = new AdCampaign("partner3", 0, "content3");
		adCampaignMap = new HashMap();
		service = new AdCampaignRestService();
		service.setContext(context);

	}

	@Test
	public void testSaveAdCampaignForValidAdCampaigns() throws AdCampaignException {
		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);
		final Response response1 = service.saveAdCampaign(adcampaign1);
		assertEquals(200, response1.getStatus());
		final Response response2 = service.saveAdCampaign(adcampaign2);
		assertEquals(200, response2.getStatus());
		final Response response3 = service.saveAdCampaign(adcampaign3);
		assertEquals(200, response3.getStatus());

	}

	@Test
	public void testSaveAdCampaignForInActiveAdCampaigns() throws AdCampaignException {
		adCampaignMap.put("partner1", adcampaign1);
		adCampaignMap.put("partner2", adcampaign2);
		adcampaign3.setCreationTimeInMillis(System.currentTimeMillis());
		adCampaignMap.put("partner3", adcampaign3);

		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);
		final Response saveAdCampaign = service.saveAdCampaign(adcampaign3);
		assertEquals(200, saveAdCampaign.getStatus());
	}


	@Test(expected=AdCampaignException.class)
	public void testSaveAdCampaignForInvalidAdCampaigns() throws AdCampaignException {
		adcampaign1.setCreationTimeInMillis(System.currentTimeMillis());
		adCampaignMap.put("partner1", adcampaign1);
		adCampaignMap.put("partner2", adcampaign2);

		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);
		service.saveAdCampaign(adcampaign1);
	}

	@Test
	public void testGetAdCampaign() throws AdCampaignException {
		adcampaign1.setCreationTimeInMillis(System.currentTimeMillis());
		adCampaignMap.put("partner1", adcampaign1);
		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);

		final AdCampaign adCampaign = service.getAdCampaign("partner1");
		assertEquals("partner1", adCampaign.getPartnerId());
		assertEquals(10, adCampaign.getDurationInSeconds());
		assertEquals("content1", adCampaign.getAdContent());

		assertTrue(adCampaign.getCreationTimeInMillis() < System.currentTimeMillis() + 1);

	}

	@Test(expected=AdCampaignException.class)
	public void testGetAdCampaignForInvalidPartnerId() throws AdCampaignException {
		adcampaign1.setCreationTimeInMillis(System.currentTimeMillis());
		adCampaignMap.put("partner1", adcampaign1);
		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);

		service.getAdCampaign("someInvalidId");
	}

	@Test(expected=AdCampaignException.class)
	public void testGetAdCampaignForInActivePartner() throws AdCampaignException {
		final long epochMilliYesterday = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		adcampaign1.setCreationTimeInMillis(epochMilliYesterday);
		adCampaignMap.put("partner1", adcampaign1);
		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);

		service.getAdCampaign("partner1");

	}

	@Test
	public void testGetAllAdCampaigns() throws AdCampaignException {
		adCampaignMap.put("partner1", adcampaign1);
		adCampaignMap.put("partner2", adcampaign2);

		Mockito.when(context.getAttribute(AdCampaignServletContextListener.AD_CAMPAIGN_IN_MEMORY_MAP)).thenReturn(adCampaignMap);

		final Collection<AdCampaign> adCampaigns = service.getAllAdCampaigns();
		assertEquals(2, adCampaigns.size());

	}

}
