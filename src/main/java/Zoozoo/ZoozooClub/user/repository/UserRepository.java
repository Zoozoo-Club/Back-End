package Zoozoo.ZoozooClub.user.repository;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(String userId);

    /**
     * User의 id(Long)로 Account를 찾는 메서드
     * @param userId
     * @return
     */
    @Query("SELECT u.account FROM User u WHERE u.id = :userId")
    Optional<Account> findAccountByUserSeq(@Param("userId") Long userId);

    /**
     * userId(String)로 Account를 찾는 메서드
     * @param userId
     * @return
     */
    @Query("SELECT u.account FROM User u WHERE u.userId = :userId")
    Optional<Account> findAccountByUserStringId(@Param("userId") String userId);
}
