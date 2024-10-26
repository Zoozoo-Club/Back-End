package Zoozoo.ZoozooClub.follow.service;

import Zoozoo.ZoozooClub.follow.dto.FollowResponseDTO;
import Zoozoo.ZoozooClub.follow.entity.Follower;
import Zoozoo.ZoozooClub.follow.repository.FollowerRepository;
import Zoozoo.ZoozooClub.user.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final AuthService authService;

    @Transactional
    public void follow(Long followerId, Long targetUserId) {
        if (followerId.equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        if (followerRepository.existsByFollowerIdAndFollowingId(followerId, targetUserId)) {
            throw new IllegalStateException("Already following this user");
        }

        authService.getUserById(targetUserId);

        Follower follower = Follower.builder()
                .followerId(followerId)
                .followingId(targetUserId)
                .build();

        followerRepository.save(follower);
    }

    @Transactional
    public void unfollow(Long followerId, Long targetUserId) {
        Follower follower = followerRepository.findByFollowerIdAndFollowingId(followerId, targetUserId)
                .orElseThrow(() -> new IllegalStateException("Not following this user"));

        followerRepository.delete(follower);
    }

    public List<FollowResponseDTO> getFollowers(Long userId) {
        List<Follower> followers = followerRepository.findAllByFollowingId(userId);
        return followers.stream()
                .map(follower -> FollowResponseDTO.from(authService.getUserById(follower.getFollowerId())))
                .collect(Collectors.toList());
    }

    public List<FollowResponseDTO> getFollowing(Long userId) {
        List<Follower> following = followerRepository.findAllByFollowerId(userId);
        return following.stream()
                .map(follower -> FollowResponseDTO.from(authService.getUserById(follower.getFollowingId())))
                .collect(Collectors.toList());
    }

    public boolean isFollowing(Long followerId, Long targetUserId) {
        return followerRepository.existsByFollowerIdAndFollowingId(followerId, targetUserId);
    }
}