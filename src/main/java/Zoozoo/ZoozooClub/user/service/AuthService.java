package Zoozoo.ZoozooClub.user.service;

import Zoozoo.ZoozooClub.user.dto.AuthLoginRequestDto;
import Zoozoo.ZoozooClub.user.dto.AuthLoginResponseDto;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.exception.NoUserException;
import Zoozoo.ZoozooClub.user.exception.NotCorrectPasswordException;
import Zoozoo.ZoozooClub.user.repository.UserRepository;
import Zoozoo.ZoozooClub.commons.auth.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
        String userId = authLoginRequestDto.getUserId();
        String password = authLoginRequestDto.getPassword();
        User user = userRepository.findUserByUserId(userId).orElseThrow(NoUserException::new);

        //todo password 검토
        if(user.getPassword().equals(password)) {
            if(user.getClub() == null) {
                return new AuthLoginResponseDto(jwtUtil.createToken(user.getId(), null));
            }
            return new AuthLoginResponseDto(jwtUtil.createToken(user.getId(), user.getClub().getId()));
        }

        throw new NotCorrectPasswordException();

    }

}
