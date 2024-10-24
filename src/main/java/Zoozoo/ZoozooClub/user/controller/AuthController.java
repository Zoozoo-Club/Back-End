package Zoozoo.ZoozooClub.user.controller;


import Zoozoo.ZoozooClub.user.dto.AuthLoginRequestDto;
import Zoozoo.ZoozooClub.user.dto.AuthLoginResponseDto;
import Zoozoo.ZoozooClub.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

   private final AuthService authService;


    @PostMapping("/login")
    private ResponseEntity<AuthLoginResponseDto> login(@RequestBody AuthLoginRequestDto authLoginRequestDto) {
        return ResponseEntity.ok().body(authService.login(authLoginRequestDto));
    }

    @GetMapping("/test")
    private ResponseEntity<String> test() {
        return ResponseEntity.ok().body("test");
    }


}
