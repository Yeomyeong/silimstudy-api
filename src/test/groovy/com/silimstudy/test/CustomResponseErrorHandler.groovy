package com.silimstudy.test

import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.ResponseErrorHandler

/**
 * http://stackoverflow.com/questions/15404605/spring-resttemplate-invoking-webservice-with-errors-and-analyze-status-code
 */
class CustomResponseErrorHandler implements ResponseErrorHandler{
    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
    @Override
    boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    void handleError(ClientHttpResponse response) throws IOException {
        String theString = response.getBody().toString()
        RuntimeException exception = new RuntimeException();
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("code", response.getStatusCode().toString());
        properties.put("body", theString);
        properties.put("header", response.getHeaders());
        exception.setProperties(properties);
        throw exception;

    }
}
