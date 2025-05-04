package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.models.dto.request.PostRequest;
import com.enigmacamp.enigshop.models.dto.response.PostResponse;
import com.enigmacamp.enigshop.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import java.util.List;

@Service("postServiceRestClient")
@RequiredArgsConstructor
public class PostServiceRestClient implements PostService {
    private final RestClient restClient;

    @Override
    public List<PostResponse> getAllPosts() {
        return restClient.get().uri("/posts").retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    @Override
    public PostResponse getPostById(Integer id) {
        return restClient.get().uri("/posts/" + id).retrieve()
                .body(PostResponse.class);
    }

    @Override
    public PostResponse addPost(PostRequest request) {
        return restClient.post().uri("/posts").body(request).retrieve().body(
                new ParameterizedTypeReference<>() {});
    }

    @Override
    public PostResponse patchPost(Integer postId, PostRequest request) {
        return restClient.patch().uri("/posts/" + postId).body(request).retrieve()
                .body(PostResponse.class);
    }

    @Override
    public PostResponse updatePost(Integer postId, PostRequest request) {
        ResponseSpec retrieve = restClient.put().uri("/posts/" + postId).body(request).retrieve();
        retrieve.body(PostResponse.class);
        return getPostById(postId);
    }

    @Override
    public void deletePost(Integer postId) {
        restClient.delete().uri("/posts/" + postId).retrieve().body(Void.class);
    }
}
