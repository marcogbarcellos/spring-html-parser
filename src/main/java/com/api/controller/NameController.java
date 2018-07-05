package com.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.UrlSearchDTO;
import com.api.model.NameUrl;
import com.api.util.HtmlParser;

@Component
@ConfigurationProperties
@RestController
@Validated
public class NameController {

	
	private static final Map<String, String> namesMap =  new HashMap<String, String>();
	
	@Value("${application.message}")
	String message;

	@Value("${application.appname}")
	String appname;

	@RequestMapping("/")
	String home() {
		return "Ping OK on " + appname;
	}

	@RequestMapping(value = "/names/{name:[A-Za-z0-9]+}", method = RequestMethod.PUT)
	public ResponseEntity<String> create(@PathVariable(value = "name", required = true) String name,
			@Valid @RequestBody UrlSearchDTO urlSearchDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(namesMap.put(name, urlSearchDTO.getUrl()));
	}

	@RequestMapping(value = "/names/{name:[A-Za-z0-9]+}", method = RequestMethod.GET)
	public ResponseEntity<NameUrl> get(@Valid @PathVariable("name") String name) {
		String url = namesMap.get(name);
		if (url == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(new NameUrl(name, url));
	}
	
	@RequestMapping(value = "/names", method = RequestMethod.GET)
	public ResponseEntity<Map<String,String>> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(namesMap);
	}
	
	@RequestMapping(value = "/names", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAll() {
		namesMap.clear();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/annotate", method = RequestMethod.POST, 
			consumes="text/plain")
	public ResponseEntity<String> annotate(@RequestBody String html) {
		String result = HtmlParser.annotate(namesMap, html); 
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
