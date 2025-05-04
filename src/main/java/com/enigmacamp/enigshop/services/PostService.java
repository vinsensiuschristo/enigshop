package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.PostRequest;
import com.enigmacamp.enigshop.models.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> getAllPosts();
    PostResponse getPostById(Integer id);
    PostResponse addPost(PostRequest request);
    PostResponse patchPost(Integer postId, PostRequest request);
    PostResponse updatePost(Integer postId, PostRequest request);
    void deletePost(Integer postId);
}
