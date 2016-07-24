package com.cc.adcampaign.service;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
/**
 * Model for AdCampaign
 * @author Venkat
 *
 */
@JsonPropertyOrder({"partner_id", "duration", "ad_content"})
public class AdCampaign {

	@JsonProperty("partner_id")
	private String partnerId;
	@JsonProperty("duration")
	private int durationInSeconds;
	@JsonProperty("ad_content")
	private String adContent;
	@JsonIgnore
	private long creationTimeInMillis;

	public AdCampaign() {
		super();
	}
	public AdCampaign(String partnerId, int durationInSeconds, String adContent) {
		super();
		this.partnerId = partnerId;
		this.durationInSeconds = durationInSeconds;
		this.adContent = adContent;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public int getDurationInSeconds() {
		return durationInSeconds;
	}
	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}
	public String getAdContent() {
		return adContent;
	}
	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}
	public long getCreationTimeInMillis() {
		return creationTimeInMillis;
	}
	public void setCreationTimeInMillis(long creationTimeInMillis) {
		this.creationTimeInMillis = creationTimeInMillis;
	}


}
