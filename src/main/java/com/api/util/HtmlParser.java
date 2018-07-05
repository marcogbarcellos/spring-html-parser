package com.api.util;

import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class HtmlParser {
	
	public static String annotate(Map<String,String> names, String html) {
		// parsing Html string to Jsoup Document and leave html in only one line
		// (by default it formats it so we need to manually set it) 
		Document doc = Jsoup.parse(html);
		doc.outputSettings().indentAmount(0).prettyPrint(false);
		// Getting all Html Tags
		Elements allHtmlTags = doc.body().getAllElements();
		// Loop through each existing element of HTML
		for (Element currentElementTag : allHtmlTags) {
			// we won't change anything inside an existing hyperlink
			if( currentElementTag.tagName().equals("a") == true) { 
				continue;
		    }
			// loop through all texts found inside the element
			List<TextNode> textsFoundInsideHtmlTag = currentElementTag.textNodes();
		    for (TextNode tn : textsFoundInsideHtmlTag){
		    	// find words that match given Regex
		        String orig = tn.text();
		        
		        for (Map.Entry<String, String> entry : names.entrySet()) {
		        	orig = orig.replaceAll("\\b"+entry.getKey()+"\\b", annotateIt(names, entry.getKey()));
		        }
		        // Replacing texts to use new hyperlinked text
		        int sibIndex = tn.siblingIndex();
		        tn.remove();
		        if(sibIndex == 0) {
		        	currentElementTag.prepend(orig);
		        } else {
		        	currentElementTag.getElementsByIndexEquals(sibIndex-1).after(orig);
		        }
		    }
	    }
		// removing extra tags inserted by Jsoup when it first parsed the given html
		// (It inserts tags like <html>, <head> and <body> for example...)
		StringBuilder nodesSb = new StringBuilder();
		for (Node node : allHtmlTags.get(0).childNodes()) {
			nodesSb.append(node.outerHtml());
		}
		// return the html on the wanted format
		return nodesSb.toString();
	}
	
	// Annotate a given word with its link
	private static String annotateIt(Map<String,String> names, String word) {
		String result = "<a href=" + names.get(word) + ">" + word + "</a>";
		return result;
	} 
}

