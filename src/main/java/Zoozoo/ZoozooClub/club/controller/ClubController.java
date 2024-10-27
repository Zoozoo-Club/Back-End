package Zoozoo.ZoozooClub.club.controller;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.club.dto.ClubResponseDto;
import Zoozoo.ZoozooClub.club.service.ClubService;
import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final AuthService authService;

    @GetMapping("/{clubId}/info")
    public ResponseEntity<ClubResponseDto> getClubInfo(@PathVariable Long clubId) {
        return ResponseEntity.ok().body(clubService.getClubInfo(clubId));

    }

    @SecurityRequirement(name="JWT")
    @GetMapping("/{clubId}/current")
    public ResponseEntity<StockPriceResponseDTO> getClubCurrentPrice(@PathVariable Long clubId , @LoginUserId Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        return ResponseEntity.ok().body(clubService.getClubCurrentPriceById(clubId,account));

    }
}
