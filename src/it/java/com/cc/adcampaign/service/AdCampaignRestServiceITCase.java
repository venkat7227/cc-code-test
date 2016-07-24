package com.cc.adcampaign.service;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Test;

import com.cc.adcampaign.context.AdCampaignServletContextListener;

public class AdCampaignRestServiceITCase extends JerseyTest {

	@Override
	protected TestContainerFactory getTestContainerFactory() {
		return new GrizzlyWebTestContainerFactory();
	}

	@Override
	protected DeploymentContext configureDeployment() {
		return ServletDeploymentContext.forServlet(new ServletContainer(new ResourceRegister()))
				.addListener(AdCampaignServletContextListener.class).build();
	}

	@Test
	public void test() {
		final AdCampaign adCampaign1 = new AdCampaign("partner1", 0, "content1");
		final Response post1 = target("/ad").request().post(Entity.json(adCampaign1), Response.class);
		assertEquals(200, post1.getStatus());

		final AdCampaign adCampaign2 = new AdCampaign("partner2", 60, "content2");
		final Response post2 = target("/ad").request().post(Entity.json(adCampaign2), Response.class);
		assertEquals(200, post2.getStatus());

		final AdCampaign adCampaign3 = new AdCampaign("partner3", 60, "content3");
		final Response post3 = target("/ad").request().post(Entity.json(adCampaign3), Response.class);
		assertEquals(200, post3.getStatus());

		// Exception Mapper Returns 400 status when trying to save active Ad
		// Campaign 'adCampaign2'
		final Response post4 = target("/ad").request().post(Entity.json(adCampaign2), Response.class);
		assertEquals(400, post4.getStatus());

		// Exception Mapper Returns 200 OK status when trying to save inactive
		// Ad Campaign 'adCampaign1'
		final Response post5 = target("/ad").request().post(Entity.json(adCampaign1), Response.class);
		assertEquals(200, post5.getStatus());

		// Exception Mapper Returns 400 status when trying to retrieve inactive
		// Ad Campaign 'adCampaign1'
		final Response get1 = target("/ad/partner1").request().get(Response.class);
		assertEquals(400, get1.getStatus());

		final AdCampaign get2 = target("/ad/partner2").request().get(AdCampaign.class);
		assertEquals("partner2", get2.getPartnerId());
		assertEquals(60, get2.getDurationInSeconds());
		assertEquals("content2", get2.getAdContent());

		final AdCampaign get3 = target("/ad/partner3").request().get(AdCampaign.class);
		assertEquals("partner3", get3.getPartnerId());
		assertEquals(60, get3.getDurationInSeconds());
		assertEquals("content3", get3.getAdContent());

		// Exception Mapper Returns 400 status when trying to retrieve invalid
		// Ad Campaign with Id 'invalidId'
		final Response getInvalid = target("/ad/invalidId").request().get(Response.class);
		assertEquals(400, getInvalid.getStatus());

		final Collection<AdCampaign> getAll = target("/ad/all-campaigns").request().get(Collection.class);
		assertEquals(3, getAll.size());
	}
}