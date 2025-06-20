package br.org.apae.auth.api.controller;

import br.org.apae.auth.api.dto.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${url_login_token}")
    private String url;
    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("username", loginRequestDto.username());
        map.add("password", loginRequestDto.password());
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForEntity(url, request, String.class);
    }
}
