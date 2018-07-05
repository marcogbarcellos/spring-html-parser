package com.api.dto;

import javax.validation.constraints.NotNull;

/**
 * This Object is used to transport/validate information from the http requests
 * to ensure that only valid information can be persisted.
 */
public final class UrlSearchDTO implements SearchDTO {
	
   @NotNull
   private String url;
        
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
    @Override
    public String toString() {
        return "Name DTO{ url=" + url + " }";
    }
    
}