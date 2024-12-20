package Zoozoo.ZoozooClub.user.service;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.club.exception.NoClubException;
import Zoozoo.ZoozooClub.user.dto.AuthLoginRequestDto;
import Zoozoo.ZoozooClub.user.dto.AuthLoginResponseDto;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.exception.NoUserException;
import Zoozoo.ZoozooClub.user.exception.NotCorrectPasswordException;
import Zoozoo.ZoozooClub.user.exception.NotFoundAccountException;
import Zoozoo.ZoozooClub.user.repository.UserRepository;
import Zoozoo.ZoozooClub.commons.auth.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        String userId = authLoginRequestDto.getUserId();
        String password = authLoginRequestDto.getPassword();
        User user = userRepository.findUserByUserId(userId).orElseThrow(NoUserException::new);

        if(user.getPassword().equals(password)) {
            if(user.getClub() == null) {
                return new AuthLoginResponseDto(jwtUtil.createToken(user.getId(), null), user.getNickname());
            }
            return new AuthLoginResponseDto(jwtUtil.createToken(user.getId(), user.getClub().getId()), user.getNickname());
        }

        throw new NotCorrectPasswordException();

    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NoUserException::new);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Account getAccountById(Long id){ return userRepository.findById(id).orElseThrow(NotFoundAccountException::new).getAccount(); }

    public Account getAccountIdById(Long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundAccountException::new).getAccount();
    }

    public Club getClubById(Long userId) {
        return userRepository.findById(userId).orElseThrow(NoClubException::new).getClub();
    }
}
