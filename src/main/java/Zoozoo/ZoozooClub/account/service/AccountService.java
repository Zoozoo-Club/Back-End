package Zoozoo.ZoozooClub.account.service;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}