package com.meetup.backend.controller;

import com.meetup.backend.dto.party.PartyRequestDto;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.service.party.PartyService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seongmin on 2022/11/08
 * updated by seongmin on 2022/11/10
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/group")
public class PartyController {

    private final AuthService authService;
    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<?> createParty(@RequestBody @Valid PartyRequestDto requestDto) {
        partyService.createParty(authService.getMyInfoSecret().getId(), requestDto);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> getMyParty() {
        return ResponseEntity.status(OK).body(partyService.getMyParty(authService.getMyInfoSecret().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPartyMembers(@PathVariable @NotNull Long id) {
        return ResponseEntity.status(OK).body(partyService.getPartyMembers(authService.getMyInfoSecret().getId(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParty(@PathVariable @NotNull Long id) {
        partyService.deleteParty(authService.getMyInfoSecret().getId(), id);
        return ResponseEntity.status(OK).build();
    }

}
