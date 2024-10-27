package Zoozoo.ZoozooClub;


import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/userId")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "test API")
    public ResponseEntity<Long> userId(@LoginUserId Long userId) {
        return ResponseEntity.ok(userId);
    }


}
