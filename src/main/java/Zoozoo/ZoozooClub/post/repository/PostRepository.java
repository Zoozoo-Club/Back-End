package Zoozoo.ZoozooClub.post.repository;

import Zoozoo.ZoozooClub.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);
    List<Post> findAllByClubId(Long clubId);
    List<Post> findAllByClubIdOrderByCreatedAtDesc(Long clubId);
}