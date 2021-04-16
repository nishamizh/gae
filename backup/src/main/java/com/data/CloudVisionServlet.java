package com.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import org.apache.commons.io.IOUtils;

@MultipartConfig
@WebServlet(name = "CloudVisionServlet", urlPatterns = { "/upload" })
public class CloudVisionServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 
    	String userID = (String)request.getParameter("userID");
    	ArrayList<String> photoID = new ArrayList<String>(Arrays.asList(request.getParameterValues("imageID")[0].split(",")));
        ArrayList<String> imageLinks = new ArrayList<String>(Arrays.asList(request.getParameterValues("imageLinks")[0].split(",")));
       
        List<VisionParameters> list = new ArrayList<VisionParameters>();
        Map<String,String> visionresult = new HashMap<String,String>();
    	try {
            if (imageLinks != null) {
                int index = 0;
                for (String photo : imageLinks) {
                    Entity user = ifAlreadyExists(photoID.get(index));
                    
                   if (user == null) {
                   	 	List<AnnotateImageResponse> labelResponses = generateLabel(photo);
                   	 	
                   	 	
                   	 	VisionParameters vm = new VisionParameters();
                   	 
	           		  for(AnnotateImageResponse res : labelResponses) { 
	           		  	 if (res.hasError()) {
	           		  		response.getWriter().println("Error: %s%n" + res.getError().getMessage()); 
	           		  }
           		  
	           		  for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
	           			  Map<Descriptors.FieldDescriptor, Object> fields = annotation.getAllFields();
	           			  for(Descriptors.FieldDescriptor fd: fields.keySet()){
	           			  if(!fd.getName().contains("mid") && !fd.getName().contains("topicality")) {
	           				visionresult.put(fd.getJsonName(), fields.get(fd).toString());
								/*
								 * if(fd.getJsonName().equalsIgnoreCase("Jwellery")) {
								 * vm.setJewellery(fields.get(fd).toString()); }
								 * if(fd.getJsonName().equalsIgnoreCase("Fashion")) {
								 * vm.setFashion(fields.get(fd).toString()); }
								 */
	           			  }
	           			} 
	           		}
           		  
	           		  for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
	           		  Map<Descriptors.FieldDescriptor, Object> fields = annotation.getAllFields();
	           		  for(Descriptors.FieldDescriptor fd: fields.keySet()){
	           			  if(!fd.getName().contains("bounding") && !fd.getName().contains("landmark")){
	           				  visionresult.put(fd.getJsonName(), fields.get(fd).toString());
	           				  request.setAttribute(fd.getJsonName(), fields.get(fd).toString());
	           				  
	           				  }
	           		  		} 
	           		  	}
           		  
           		  } 
	           		visionresult.put("user_id", userID);
	           		visionresult.put("fb_image_id", photoID.get(index));
	           		visionresult.put("image_url", photo); 
                    }
                   uploadToDataStore(visionresult);
               	
                    index++;
                }//end for
            }
        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }
    	
    	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.html"); 
		  try { 
			  dispatcher.forward(request,response); 
			  } 
		  catch (ServletException | IOException ec) { 
			  ec.printStackTrace(); 
		  	}

    }

    
    //Saving to data store.
    private void uploadToDataStore(Map<String,String> map) {
    	Entity e = new Entity("Person");

    	for (Map.Entry<String,String> entry : map.entrySet())  {
    		e.setProperty(entry.getKey(), entry.getValue());
        } 
    	ds.put(e);
    }
    
    private synchronized Entity ifAlreadyExists(String photoID) {
        Query q =	new Query("Person")
                        .setFilter(new FilterPredicate("fb_image_id", FilterOperator.EQUAL, photoID));
        PreparedQuery pq = ds.prepare(q);
        Entity result = pq.asSingleEntity();

        return result;
    }

    private List<AnnotateImageResponse> generateLabel(String imageLink) throws Exception {
    	
    	byte[]  imgBytes = downloadFile(new URL(imageLink));
        ByteString byteString = ByteString.copyFrom(imgBytes);
    	
        List<AnnotateImageRequest> requests = new ArrayList<>();

        System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));//give your path name

        Image img = Image.newBuilder().setContent(byteString).build();
     
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        
        Feature feat2 = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
        
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);
        
        AnnotateImageRequest request2 =
                AnnotateImageRequest.newBuilder().addFeatures(feat2).setImage(img).build();
        requests.add(request2);

        try {
            ImageAnnotatorClient client = ImageAnnotatorClient.create();
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            return responses;

        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    public static byte[] downloadFile(URL url) throws Exception {
        try (InputStream in = url.openStream()) {
            byte[] bytes = IOUtils.toByteArray(in);
            return bytes;
        }
    }
}
