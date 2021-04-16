package com.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;



@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hello"}
)
public class HelloAppEngine extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
	  
	  String vision = request.getParameter("vision");
	  String numberofimage = request.getParameter("numberofimage");
	 
  DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
  Query q2 = new Query("Person");
  q2.setFilter(new Query.FilterPredicate(vision, Query.FilterOperator.GREATER_THAN, "0.2")); 
  
  PreparedQuery pq = ds.prepare(q2);
  pq.asList(FetchOptions.Builder.withLimit(Integer.parseInt(numberofimage)));
  
  String fashion="";
  String hair="";
  String imageSource ="";
  String addedDate ="";
  List<VisionParameters> list = new ArrayList<VisionParameters>();
  
  int numberofentities = pq.countEntities();
  for (Entity result : pq.asIterable()) { 
	  VisionParameters vm = new VisionParameters();
	  // Like this do it for all parameters
	  if(result.getProperty("Fashion") != null ) {
		  vm.setFashion((String) result.getProperty("Fashion"));
	  }
	   
	  hair = (String) result.getProperty("Hair");
	  
	  if(result.getProperty("imageSource") != null ) {
		  vm.setImageSource((String) result.getProperty("imageSource"));
	  }
	  addedDate = (String)result.getProperty("addedDate"); 
	  
	  list.add(vm);
  } 	  
  
  request.setAttribute("list", list);
  request.setAttribute("numberofentities", numberofentities);
 
	
	RequestDispatcher dispatcher = getServletContext()
		      .getRequestDispatcher("/labels.jsp");
		    try {
				dispatcher.forward(request, response);
			} catch (ServletException | IOException ec) {
				// TODO Auto-generated catch block
				ec.printStackTrace();
			}	
		    response.setContentType("text/plain");
		    response.setCharacterEncoding("UTF-8");
  
  }
}