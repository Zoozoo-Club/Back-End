package Zoozoo.ZoozooClub.post.service;

import Zoozoo.ZoozooClub.post.dto.PostResponseDTO;
import Zoozoo.ZoozooClub.post.entity.AllPost;
import Zoozoo.ZoozooClub.post.entity.Post;
import Zoozoo.ZoozooClub.post.repository.AllPostRepository;
import Zoozoo.ZoozooClub.post.repository.PostRepository;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AllPostRepository allPostRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPublicPosts() {
        List<AllPost> posts = allPostRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(post -> {
                    User user = authService.getUserById(post.getUserId());
                    return convertToDTO(post, user);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getMyClubPosts(Long userId) {
        User currentUser = authService.getUserById(userId);

        if (currentUser.getClub() == null) {
            throw new IllegalStateException("User does not belong to any club");
        }

        Long clubId = currentUser.getClub().getId();
        List<Post> posts = postRepository.findAllByClubId(clubId);

        return posts.stream()
                .map(post -> {
                    User postAuthor = authService.getUserById(post.getUserId());
                    return PostResponseDTO.from(post, postAuthor);
                })
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public PostResponseDTO getPostById(Long userId, Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
//
//        User currentUser = authService.getUserById(userId);
//        if (currentUser.getClub() == null || !post.getClubId().equals(currentUser.getClub().getId())) {
//            throw new IllegalStateException("You don't have permission to view this post");
//        }
//
//        User postAuthor = authService.getUserById(post.getUserId());
//        return PostResponseDTO.from(post, postAuthor);
//    }

    private PostResponseDTO convertToDTO(AllPost post, User user) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .pv(post.getPv())
                .userId(post.getUserId())
                .nickname(user.getNickname())
                .clubName(user.getClub() != null ? user.getClub().getCompany().getName() : null)
                .clubId(post.getClubId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }


}