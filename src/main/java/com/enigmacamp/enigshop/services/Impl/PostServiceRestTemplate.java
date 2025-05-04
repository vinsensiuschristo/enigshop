package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.models.dto.request.PostRequest;
import com.enigmacamp.enigshop.models.dto.response.PostResponse;
import com.enigmacamp.enigshop.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service("postServiceRestTemplate")
@RequiredArgsConstructor
public class PostServiceRestTemplate implements PostService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "https://jsonplaceholder.typicode.com";

    @Override
    public List<PostResponse> getAllPosts() {
        return restTemplate.exchange(
                baseUrl + "/posts",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<PostResponse>>() {
                }).getBody();
    }

    @Override
    public PostResponse getPostById(Integer id) {
        return restTemplate.getForObject(baseUrl + "/posts/" + id, PostResponse.class);
    }

    @Override
    public PostResponse addPost(PostRequest request) {
        return restTemplate.postForObject(baseUrl + "/posts", request, PostResponse.class);
    }

    public PostResponse patchPost(Integer postId, PostRequest request) {
        HttpEntity<PostRequest> requestEntity = new HttpEntity<>(request);
        return restTemplate.exchange(
                baseUrl + "/posts/" + postId,
                HttpMethod.PATCH,
                requestEntity,
                PostResponse.class
        ).getBody();
    }

    @Override
    public PostResponse updatePost(Integer postId, PostRequest request) {
        HttpEntity<PostRequest> requestEntity = new HttpEntity<>(request);
        return restTemplate.exchange(
                baseUrl + "/posts/" + postId,
                HttpMethod.PUT,
                requestEntity,
                PostResponse.class
        ).getBody();
    }

    @Override
    public void deletePost(Integer postId) {
        restTemplate.delete(baseUrl + "/posts/" + postId);
    }
}
