package Zoozoo.ZoozooClub.club.controller;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.club.dto.ClubResponseDto;
import Zoozoo.ZoozooClub.club.dto.MyClubResponseDto;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.club.service.ClubService;
import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/my-club")
    @SecurityRequirement(name = "JWT")
    @Operation(summary =  "내 클럽 가져오기 API")
    public ResponseEntity<MyClubResponseDto> getMyClub(@LoginUserId Long userId) {
        Club club = authService.getClubById(userId);
        return ResponseEntity.ok().body(MyClubResponseDto.builder()
                .clubId(club.getId())
                .clubName(club.getCompany().getName()).build());
    }

    @GetMapping("/{clubId}/current")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "현재 클럽의 주식 가격 가져오는 API")
    public ResponseEntity<StockPriceResponseDTO> getClubCurrentPrice(@PathVariable Long clubId , @LoginUserId Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        return ResponseEntity.ok().body(clubService.getClubCurrentPriceById(clubId,account));

    }
}
