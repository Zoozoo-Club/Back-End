package Zoozoo.ZoozooClub.follow.controller;

import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.follow.dto.FollowResponseDTO;
import Zoozoo.ZoozooClub.follow.service.FollowerService;
import Zoozoo.ZoozooClub.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping("/{targetUserId}")
    public ResponseEntity<Void> followUser(
            @LoginUserId Long userId,
            @PathVariable Long targetUserId) {
        followerService.follow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unfollows/{targetUserId}")
    public ResponseEntity<Void> unfollowUser(
            @LoginUserId Long userId,
            @PathVariable Long targetUserId) {
        followerService.unfollow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponseDTO>> getMyFollowers(@LoginUserId Long userId) {
        List<FollowResponseDTO> followers = followerService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following")
    public ResponseEntity<List<FollowResponseDTO>> getMyFollowing(@LoginUserId Long userId) {
        List<FollowResponseDTO> following = followerService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{targetUserId}/followers")
    public ResponseEntity<List<FollowResponseDTO>> getUserFollowers(@PathVariable Long targetUserId) {
        List<FollowResponseDTO> followers = followerService.getFollowers(targetUserId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{targetUserId}/following")
    public ResponseEntity<List<FollowResponseDTO>> getUserFollowing(@PathVariable Long targetUserId) {
        List<FollowResponseDTO> following = followerService.getFollowing(targetUserId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{targetUserId}/status")
    public ResponseEntity<Boolean> getFollowStatus(
            @LoginUserId Long userId,
            @PathVariable Long targetUserId) {
        boolean isFollowing = followerService.isFollowing(userId, targetUserId);
        return ResponseEntity.ok(isFollowing);
    }
}