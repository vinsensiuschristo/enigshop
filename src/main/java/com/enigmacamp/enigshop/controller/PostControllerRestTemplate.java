package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.PostRequest;
import com.enigmacamp.enigshop.models.dto.response.CommonResponse;
import com.enigmacamp.enigshop.models.dto.response.PostResponse;
import com.enigmacamp.enigshop.services.PostService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.POST_TEMPLATE_API)
public class PostControllerRestTemplate {
    private final PostService postService;
    public PostControllerRestTemplate(@Qualifier("postServiceRestTemplate") PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();

        return ResponseEntity.ok(
                new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        posts,
                        null
                )
        );
    }

    @PostMapping
    public ResponseEntity<CommonResponse<PostResponse>> addPost(
            @RequestBody PostRequest request
    ) {
        PostResponse post = postService.addPost(request);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        post,
                        null
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> getPostById(@PathVariable Integer id) {
        PostResponse post = postService.getPostById(id);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        post,
                        null
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> patchPost(
            @PathVariable Integer id,
            @RequestBody PostRequest request
    ) {
        PostResponse post = postService.patchPost(id, request);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        post,
                        null
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<PostResponse>> updatePost(
            @PathVariable Integer id,
            @RequestBody PostRequest request
    ) {
        PostResponse post = postService.updatePost(id, request);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        post,
                        null
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        null,
                        null
                )
        );
    }

}
