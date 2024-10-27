package Zoozoo.ZoozooClub.post.controller;

import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.post.dto.PostResponseDTO;
import Zoozoo.ZoozooClub.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/public")
    public ResponseEntity<List<PostResponseDTO>> getPublicPosts() {
        List<PostResponseDTO> posts = postService.getPublicPosts();
        return ResponseEntity.ok(posts);
    }

//    @GetMapping("/{postId}")
//    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {
//        PostResponseDTO post = postService.getPostById(postId);
//        return ResponseEntity.ok(post);
//    }


    @GetMapping("/my-club")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "test API")
    public ResponseEntity<List<PostResponseDTO>> getClubPosts(@LoginUserId Long userId) {
        List<PostResponseDTO> posts = postService.getMyClubPosts(userId);
        return ResponseEntity.ok(posts);
    }
}
