package Zoozoo.ZoozooClub.account.repository;

import Zoozoo.ZoozooClub.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
