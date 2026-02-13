package com.lab2.middleware.config;

import com.lab2.movie.soap.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class SoapClientConfig {

    @Value("${mule.esb.url}")
    private String muleEsbUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MovieService movieService() throws MalformedURLException {
        try {
            // Point to Mule ESB instead of direct SOAP service
            String soapServiceUrl = muleEsbUrl + "/MovieService";
            URL wsdlUrl = new URL(muleEsbUrl + "/MovieService?wsdl");
            QName qname = new QName("http://soap.movie.lab2.com/", "MovieService");

            Service service = Service.create(wsdlUrl, qname);
            MovieService movieService = service.getPort(MovieService.class);
            
            // Configure the endpoint to go through Mule ESB
            ((javax.xml.ws.BindingProvider) movieService).getRequestContext()
                .put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, soapServiceUrl);
            
            System.out.println("SOAP client configured to use Mule ESB at: " + muleEsbUrl);
            System.out.println("Full SOAP service URL: " + soapServiceUrl);
            return movieService;
        } catch (Exception e) {
            // Log the error but don't fail startup
            System.err.println("Warning: Failed to create SOAP client for MovieService through Mule ESB at " + muleEsbUrl);
            System.err.println("Exception: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}