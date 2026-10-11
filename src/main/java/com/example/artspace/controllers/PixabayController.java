package com.example.artspace.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/pixabay")
public class PixabayController {

    @Value("${pixabay.api.key}")
    private String apiKey;

    @GetMapping("/artworks")
    public ResponseEntity<String> getArtworks() {

        String url = UriComponentsBuilder
                .fromUriString("https://pixabay.com/api/")
                .queryParam("key", apiKey)
                .queryParam("q", "art illustration")
                .queryParam("image_type", "illustration")
                .queryParam("per_page", 30)
                .build()
                .encode()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }
}