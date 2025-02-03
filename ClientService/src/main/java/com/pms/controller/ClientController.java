	package com.pms.controller;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.model.Bill;
import com.pms.model.Payment;

import org.springframework.core.ParameterizedTypeReference;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import java.util.*;
import java.util.logging.Logger;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Controller
public class ClientController {
	
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
       
    }
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@RequestMapping(value="/index")
	public String regindex() {
	    return "index";
		
	}
	
	@RequestMapping(value="/")
	public String registrationPage() {
	    return "frontpage";
		
	}
	
	
	
	
	
	@RequestMapping(value="/generateBill")
	public String generateBillPage(Model m) {
		m.addAttribute("bill", new Bill());
	    return "generatebill";
	
	}
	 
     
     @RequestMapping(value="/searchByBillId")
 	public String searchByBillIdmethod () {
 	   return "viewByBillId";
 	
 	}
     @RequestMapping(value="/searchByPatientId")
 	public String searchByPatientIdmethod() {
 		return "viewByPatientId";
 	
 	}
     @RequestMapping(value="/searchByDate")
 	public String searchByDatemethod() {
 		return "viewByDate";
 	
 	}
     
     @RequestMapping(value="/updateBillStatus")
  	public String updatebill() {
  		return "updateBill";
  	
  	}
    
     
	
     //http://localhost:8080/bills/2
     @RequestMapping(value = "/bills/{appointmentId}", method = RequestMethod.POST)
     public String submitNewBill(@PathVariable("appointmentId") int appointmentId, @ModelAttribute("bill") Bill bill, Model model) throws JsonMappingException, JsonProcessingException {
      /*   if(bill==null)
         { 
        	 model.addAttribute("errorMessage", "Something went wrong: Resource not found.");
             return "statuspage"; 
        	 
         }*/
    	 Bill billobj = null;
         System.out.println("AppointmentId -" + appointmentId);
         model.addAttribute("appointmentId", appointmentId);
         String url = "http://localhost:8080/bills/" + appointmentId;

         // Set up headers for the request
         HttpHeaders headers = new HttpHeaders();
         headers.set("Content-Type", "application/json");
         // Wrap the bill object into an HTTP request entity
         HttpEntity<Bill> request = new HttpEntity<>(bill, headers);

         // Send the POST request
         try {
             ResponseEntity<Bill> response = restTemplate().exchange(
                 url,
                 HttpMethod.POST,
                 request,
                 Bill.class
             );

             billobj = response.getBody(); // Get the created bill object from the response

         } catch (HttpClientErrorException e) {
        	 if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                 // 404 error
                 model.addAttribute("errorMessage", "Pls Enter Valid Appointment Id  ");
                 return "statuspage"; 
             } 
             // Handle client errors (4xx)
             ObjectMapper objectMapper = new ObjectMapper();
             JsonNode rootNode = objectMapper.readTree(e.getResponseBodyAsString());
             String errorMessage = rootNode.path("message").asText();
             model.addAttribute("errorMessage", errorMessage);
             return "statuspage"; // Redirect to an error page
         } catch (HttpServerErrorException e) {
             // Handle server errors (5xx)
             model.addAttribute("errorMessage", "Server error: " + e.getMessage());
             return "statuspage"; // Redirect to an error page
         } catch (Exception e) {
             // Handle unexpected errors
             model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
             return "statuspage"; // Redirect to an error page
         }

         // If the bill was created successfully, show a success page
         if (billobj != null) {
        	 
             model.addAttribute("bill", billobj); // Optionally add the newly created bill to the model
             return "bill_list"; // A confirmation page to show the bill details
         } else {
             // If the bill wasn't created, show an error message
             model.addAttribute("errorMessage", "Failed to create the bill. Try again.");
             return "statuspage"; // Redirect to an error page
         }
     }
    
             

     @RequestMapping(value = "/findBillByIdforUpdatingPaymentStatus", method = RequestMethod.GET)
 	public String findBillByIdforUpdatingPaymentStatus(@RequestParam("billId") int billId, Model model) {
 	    Bill bill = null;

 	    String url = "http://localhost:8080/bills/" + billId;
 	    HttpHeaders headers = new HttpHeaders();
 	    headers.set("Content-Type", "application/json");

 	    try {
 	        ResponseEntity<Bill> response = restTemplate().exchange(
 	            url,
 	            HttpMethod.GET,
 	            null,
 	            new ParameterizedTypeReference<Bill>() {}
 	        );
 	        if (response != null && response.getBody() != null) {
 	            bill = response.getBody();
 	        }
 	    } catch (HttpClientErrorException e) {
 	    	 if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                 // 404 error
                 model.addAttribute("errorMessage", "Pls Enter Valid Bill Id  ");
                 return "statuspage"; 
             }
 	        model.addAttribute("errorMessage", "Client error: " + e.getMessage());
 	        return "statuspage";
 	    } catch (HttpServerErrorException e) {
 	        model.addAttribute("errorMessage", "Server error: " + e.getMessage());
 	        return "statuspage";
 	    } catch (Exception e) {
 	        model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
 	        return "statuspage";
 	    }

 	    if (bill != null) {
 	        model.addAttribute("bill", bill);
 	        return "bill_list_updating";
 	    } else {
 	        model.addAttribute("errorMessage", "No bill details found with the given ID.");
 	        return "statuspage";
 	    }
 	}
     
     
     @RequestMapping(value = "/updatepaymentstatus", method = RequestMethod.POST)
 	public String updatepaymentstatus(@ModelAttribute("bill") Bill bill, Model model) {
    	 Bill billobj = null;
 	    System.out.println("Bill Id for update-"+bill.getBillId());
 	   System.out.println("Bill object"+bill);
 	    String url = "http://localhost:8080/bills/" + bill.getBillId();
 	  
 	// Set up headers for the request
       HttpHeaders headers = new HttpHeaders();
       headers.set("Content-Type", "application/json");
       // Wrap the bill object into an HTTP request entity
       HttpEntity<Bill> request = new HttpEntity<>(bill, headers);
       // Send the POST request
      

 	   
 	    try {
 	        ResponseEntity<Bill> response = restTemplate().exchange(
 	            url,
 	            HttpMethod.PUT,
 	            request,
 	            //new ParameterizedTypeReference<Bill>() {}
 	           Bill.class
 	        );
 	        if (response != null && response.getBody() != null) {
 	            billobj = response.getBody();
 	        }
 	    }
 	  
 	    catch (HttpClientErrorException e) {
 	        model.addAttribute("errorMessage", "Client error: " + e.getMessage());
 	        return "statuspage";
 	    } catch (HttpServerErrorException e) {
 	        model.addAttribute("errorMessage", "Server error: " + e.getMessage());
 	        return "statuspage";
 	    } catch (Exception e) {
 	        model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
 	        return "statuspage";
 	    }

 	   // If the bill was created successfully, show a success page
        if (billobj != null) {
            model.addAttribute("bill", billobj); // Optionally add the newly created bill to the model
            return "bill_list"; // A confirmation page to show the bill details
        } else {
            // If the bill wasn't created, show an error message
            model.addAttribute("errorMessage", "Failed to create the bill. Try again.");
            return "statuspage"; // Redirect to an error page
        }
 	}
    	
	
		@RequestMapping(value = "/findBillByPatientId", method = RequestMethod.GET)
	public String findBillByPatientId(@RequestParam("patientId") int patientId, Model model) {
	    List<Bill> bills = null;

	   
	    String url = "http://localhost:8080/bills/patient/" + patientId;
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");

	    try {
	        // Send the GET request to fetch the list of bills
	        ResponseEntity<List<Bill>> response = restTemplate().exchange(
	            url,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<List<Bill>>() {}
	        );

	        if (response != null && response.getBody() != null) {
	            bills = response.getBody();
	        }
	    } catch (HttpClientErrorException e) {
	        model.addAttribute("errorMessage", "Client error: " + e.getMessage());
	        return "statuspage";
	    } catch (HttpServerErrorException e) {
	        model.addAttribute("errorMessage", "Server error: " + e.getMessage());
	        return "statuspage";
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
	        return "statuspage";
	    }

	    // Check if any bills are returned
	    if (bills != null && !bills.isEmpty()) {
	    	 model.addAttribute("bills", bills);
		        model.addAttribute("patient_id", patientId);
		        return "patient_list";
	      
	    } else {
	        model.addAttribute("errorMessage", "No bills found for the given Patient ID.");
	        return "statuspage";
	    }
	}
		@RequestMapping(value = "/findBillByDate", method = RequestMethod.GET)
		public String findBillByDate1(@RequestParam("billDate") LocalDate billDate, Model model) {
			    List<Bill> bills = null;

		    // Modify URL to fetch the list of bills for the given Patient ID
			    String url = "http://localhost:8080/bills/date/" + billDate;
				  
		    HttpHeaders headers = new HttpHeaders();
		    headers.set("Content-Type", "application/json");

		    try {
		        // Send the GET request to fetch the list of bills
		        ResponseEntity<List<Bill>> response = restTemplate().exchange(
		            url,
		            HttpMethod.GET,
		            null,
		            new ParameterizedTypeReference<List<Bill>>() {}
		        );

		        if (response != null && response.getBody() != null) {
		            bills = response.getBody();
		        }
		    } catch (HttpClientErrorException e) {
		        model.addAttribute("errorMessage", "Client error: " + e.getMessage());
		        return "statuspage";
		    } catch (HttpServerErrorException e) {
		        model.addAttribute("errorMessage", "Server error: " + e.getMessage());
		        return "statuspage";
		    } catch (Exception e) {
		        model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
		        return "statuspage";
		    }

		    // Check if any bills are returned
		    if (bills != null && !bills.isEmpty()) {
		        model.addAttribute("bills", bills);
		        model.addAttribute("date", billDate);
		        return "date_list";  
		    } else {
		        model.addAttribute("errorMessage", "No bills found for the given Patient ID.");
		        return "statuspage";
		    }
		}
	    
	

	
	
	
	
	@RequestMapping(value = "/findBillById", method = RequestMethod.GET)
	public String findBillById(@RequestParam("billId") int billId, Model model) {
	    Bill bill = null;

	    String url = "http://localhost:8080/bills/" + billId;
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");

	    try {
	        ResponseEntity<Bill> response = restTemplate().exchange(
	            url,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<Bill>() {}
	        );
	        if (response != null && response.getBody() != null) {
	            bill = response.getBody();
	        }
	    } catch (HttpClientErrorException e) {
	        model.addAttribute("errorMessage", "Client error: " + e.getMessage());
	        return "statuspage";
	    } catch (HttpServerErrorException e) {
	        model.addAttribute("errorMessage", "Server error: " + e.getMessage());
	        return "statuspage";
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "Unexpected error: " + e.getMessage());
	        return "statuspage";
	    }

	    if (bill != null) {
	        model.addAttribute("bill", bill);
	        return "bill_list";
	    } else {
	        model.addAttribute("errorMessage", "No bill details found with the given ID.");
	        return "statuspage";
	    }
	}

	/* payment Controller */
	
	

	  private static final Logger logger = Logger.getLogger(ClientController.class.getName());
	private final String backendUrl = "http://localhost:8080/api/payments";

    

	 @GetMapping("/payments")
	    public String getAllPayments(Model model) {
	        RestTemplate restTemplate = new RestTemplate();
	        String jsonResponse = restTemplate.getForObject(backendUrl + "/searchbypayment", String.class);
	        logger.info("Backend JSON response: " + jsonResponse);
	        try {
	        	 Payment[] paymentArray = restTemplate.getForObject(backendUrl + "/searchbypayment", Payment[].class);
	             List<Payment> payments = Arrays.asList(paymentArray);
	                     model.addAttribute("payments", payments);
	        } catch (Exception e) {
	            logger.severe("Error fetching payments: " + e.getMessage());
	            model.addAttribute("error", "Error fetching payments");
	        }
	        return "payments";
	    }

 
    
    @RequestMapping("/create")
    public String showCreatePage(@RequestParam("billId") String billId, 
                                  @RequestParam("finalamount") double finalamount, 
                                  Model model) {
        model.addAttribute("billId", billId);
        model.addAttribute("finalamount", finalamount);
       
        return "createpayment";
        
    }
    
    
    @GetMapping("/viewpayments")
    public String viewpayments() {
        return "payments";
    }
    
   

   
   
    @GetMapping("/searchbypayment")
    public String searchPayments(
            @RequestParam(required = false) Integer billId,
            @RequestParam(required = false) String paymentMethod,
            Model model) {
        try {
            logger.info("Payment Method: " + paymentMethod);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendUrl + "/searchbypayment");
            if (billId != null) {
                builder.queryParam("billId", billId);
            }
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                builder.queryParam("paymentMethod", paymentMethod);
            }

            String queryUrl = builder.toUriString();
            logger.info("Query URL: " + queryUrl);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Payment[]> response = restTemplate.exchange(
                    queryUrl, 
                    HttpMethod.GET, 
                    null, 
                    Payment[].class
            );
            if (response.getBody() != null && response.getBody().length > 0) {
                model.addAttribute("payments", Arrays.asList(response.getBody()));
            } else {
                model.addAttribute("errorMessage", "No payments found matching the criteria.");
            }
        } catch (HttpClientErrorException.NotFound ex) {
            logger.severe("Error searching payments: " + ex.getResponseBodyAsString());
            model.addAttribute("errorMessage", ex.getResponseBodyAsString());
        } catch (Exception e) {
            logger.severe("Unexpected error searching payments: " + e.getMessage());
            model.addAttribute("errorMessage", "An unexpected error occurred while searching payments.");
        }
        return "payments";
    }
    @GetMapping("/searchByPaymentDate")
    public String filterPaymentsByDate(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        try {
            if ((startDate == null || startDate.isEmpty()) || (endDate == null || endDate.isEmpty())) {
                model.addAttribute("errorMessage", "Please provide both start and end dates.");
                return "payments";
            }

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(backendUrl + "/searchByDate")
                    .queryParam("startDate", startDate)
                    .queryParam("endDate", endDate);

            String queryUrl = builder.toUriString();
            logger.info("Date Query URL: " + queryUrl);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Payment[]> response = restTemplate.exchange(
                    queryUrl, 
                    HttpMethod.GET, 
                    null, 
                    Payment[].class
            );
            if (response.getBody() != null && response.getBody().length > 0) {
                model.addAttribute("payments", Arrays.asList(response.getBody()));
            } else {
                model.addAttribute("errorMessage", "No payments found for the specified date range.");
            }
        } catch (HttpClientErrorException.NotFound ex) {
            logger.severe("Error filtering payments by date: " + ex.getResponseBodyAsString());
            model.addAttribute("errorMessage", ex.getResponseBodyAsString());
        } catch (Exception e) {
            logger.severe("Unexpected error filtering payments by date: " + e.getMessage());
            model.addAttribute("errorMessage", "An unexpected error occurred while filtering payments by date.");
        }
        return "payments";
    }


	
	
}
