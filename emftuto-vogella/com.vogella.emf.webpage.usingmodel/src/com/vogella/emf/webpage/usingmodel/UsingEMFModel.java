package com.vogella.emf.webpage.usingmodel;

import com.vogella.emf.webpage.model.webpage.Web;
import com.vogella.emf.webpage.model.webpage.Webpage;
import com.vogella.emf.webpage.model.webpage.WebpageFactory;

public class UsingEMFModel {
	public static void main(String[] args) {		
		WebpageFactory factory = WebpageFactory.eINSTANCE;
		Web web = factory.createWeb();
		web.setName("Hello");
		web.setDescription("Description");
		Webpage page = factory.createWebpage();
		page.setTitle("Pagetitle");
		web.getWebpage().add(page);
	}

}
