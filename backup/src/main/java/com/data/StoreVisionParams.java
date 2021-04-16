package com.data;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@WebServlet(
	    name = "HelloAppEngine",
	    urlPatterns = {"/insert"}
	    )
public class StoreVisionParams extends HttpServlet {
	
	 @Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {
		
		 String list = request.getParameter("list");
		// System.out.println("joyLikelihood is: " + joyLikelihood);
		 
		 DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		 
		 Enumeration<String> parameterNames = request.getParameterNames();
		 Entity e = new Entity("Person");
		 
	        while (parameterNames.hasMoreElements()) {
	 
	            String paramName = parameterNames.nextElement();
	            
	 
	            String[] paramValues = request.getParameterValues(paramName);
	            for (int i = 0; i < paramValues.length; i++) {
	                String paramValue = paramValues[i];
	                e.setProperty(paramName, paramValue);
	            }
	        }
	        ds.put(e);
	 }

}
