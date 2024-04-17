package com.example.demoapp.Connectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCategoryConnector {
    @Autowired
    RestTemplate connector;

    String baseUrl="http://localhost:8080/YourNotesApp/Logged/Categories/AddCategory";

    public boolean checkCategoryExists(String categoryName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        String url = baseUrl + "/check/" + categoryName;
        System.out.println("Link do resta: " + url);

        try{
            ResponseEntity<Void> response = connector.exchange(url, HttpMethod.GET, request, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound ex){
            return false;
        }
    }

}