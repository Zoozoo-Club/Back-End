package Zoozoo.ZoozooClub.club.controller;

import Zoozoo.ZoozooClub.club.dto.ClubResponseDto;
import Zoozoo.ZoozooClub.club.entity.Club;
import Zoozoo.ZoozooClub.club.service.ClubService;
import Zoozoo.ZoozooClub.company.service.CompanyService;
import lombok.Getter;
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

    @GetMapping("/{clubId}/info")
    public ResponseEntity<ClubResponseDto> getClubInfo(@PathVariable String clubId) {
        return ResponseEntity.ok().body(clubService.getClubInfo(Long.parseLong(clubId)));

    }
}
