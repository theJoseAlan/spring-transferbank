package com.spring.tb.domain.services;

import com.spring.tb.api.model.EnderecoResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private final String BASE_URL = "http://viacep.com.br/ws/";

    private RestTemplate restTemplate;

    public ViaCepService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public EnderecoResponse getEndereco(String cep){


        String url = BASE_URL + cep + "/json";

        ResponseEntity<EnderecoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                EnderecoResponse.class
        );

        return response.getBody();

    }

}
