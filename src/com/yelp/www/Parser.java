package com.yelp.www;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parser implements Runnable{
	private String url;
	private int timeOut = 10000;
	private static int count = 0;
	
	public Parser(String url) {
		this.url = url;
	}
	
	public void parse(Document document) {
		Elements reviews = document.select(".reviews");
		Iterator<?> iterator = reviews.select("div.review").listIterator();
		while(iterator.hasNext()) {
			parseReview(iterator.next().toString());
		}
	}
	
	public void parseReview(String content) {
		Document review = Jsoup.parse(content);
		String userName = review.select(".user-display-name").text();
		String date = review.select(".rating-qualifier").select("meta").attr("content");
		String rating = review.select(".rating-very-large").select("meta").attr("content");
		String comment = review.select(".review-content p").text();
		System.out.println(userName + " -> " + "(" + date + ") (" + rating + ") " + comment);
	}

	public void run() {
		try {
			Document document = Jsoup.connect(url).timeout(timeOut).get();
			parse(document);
			int i = 1, reviewCount = 40;
			while(!document.select("a.next").isEmpty()){
				document = Jsoup.connect(url+ "?start=" + i * reviewCount).timeout(timeOut).get();
				parse(document);
				i += 1;
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
