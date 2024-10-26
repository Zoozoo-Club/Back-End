package Zoozoo.ZoozooClub.follow.repository;


import Zoozoo.ZoozooClub.follow.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findAllByFollowerId(Long followerId);
    List<Follower> findAllByFollowingId(Long followingId);
    Optional<Follower> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
