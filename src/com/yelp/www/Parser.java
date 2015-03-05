package com.yelp.www;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parser implements Runnable{
	private String url;
	private int timeOut = 20000;
	
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
		String date_string = review.select(".rating-qualifier").select("meta").attr("content");
		DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		Date date;
		try {
			date = format.parse(date_string);
		} catch (ParseException e) {
			date = null;
		}
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
