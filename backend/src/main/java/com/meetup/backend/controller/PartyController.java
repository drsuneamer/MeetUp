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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seongmin on 2022/11/08
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/group")
public class PartyController {

    private final AuthService authService;
    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<?> createParty(@RequestBody PartyRequestDto requestDto) {
        partyService.createParty(authService.getMyInfoSecret().getId(), requestDto);
        return ResponseEntity.status(CREATED).build();
    }

}
