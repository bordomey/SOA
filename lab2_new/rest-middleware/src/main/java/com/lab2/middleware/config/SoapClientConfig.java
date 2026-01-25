package com.lab2.middleware.config;

import com.lab2.movie.soap.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class SoapClientConfig {

    @Value("${soap.movie.service.url:http://localhost:8080/MovieService}")
    private String soapServiceUrl;

    @Bean
    public MovieService movieService() throws MalformedURLException {
        URL wsdlUrl = new URL(soapServiceUrl + "?wsdl");
        QName qname = new QName("http://soap.movie.lab2.com/", "MovieService");

        Service service = Service.create(wsdlUrl, qname);
        return service.getPort(MovieService.class);
    }
}