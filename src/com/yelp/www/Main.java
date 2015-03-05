package com.yelp.www;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public Thread threads[] = new Thread[10];

	public static void main(String[] args) {
		Main main = new Main();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Please give the id of the business: ");
		try {
			String businessId = reader.readLine();
			Parser parser = new Parser("http://www.yelp.com/biz/" + businessId);
			main.threads[0] = new Thread(parser);
			main.threads[0].run();
			main.threads[0].join();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
