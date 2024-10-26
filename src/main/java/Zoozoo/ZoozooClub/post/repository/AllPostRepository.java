package Zoozoo.ZoozooClub.post.repository;

import Zoozoo.ZoozooClub.post.entity.AllPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllPostRepository extends JpaRepository<AllPost, Long> {
    List<AllPost> findAllByOrderByCreatedAtDesc();
}