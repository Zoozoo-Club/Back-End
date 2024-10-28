package Zoozoo.ZoozooClub.user.controller;

import Zoozoo.ZoozooClub.user.dto.OtherUserResponseDto;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final AuthService authService;

    @GetMapping("/{userId}")
    @Operation(summary = "타인의 닉네임 및 클럽 이름 조회하는 API")
    public ResponseEntity<OtherUserResponseDto> getOtherUser(@PathVariable Long userId) {
        User user = authService.getUserById(userId);

        return ResponseEntity.ok().body(OtherUserResponseDto.builder()
                .userName(user.getNickname())
                .clubName(user.getClub().getCompany().getName()).build());
    }
}
