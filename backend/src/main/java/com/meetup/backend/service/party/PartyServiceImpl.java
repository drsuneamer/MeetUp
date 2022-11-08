package com.meetup.backend.service.party;

import com.meetup.backend.dto.party.PartyRequestDto;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.exception.ExceptionEnum;
import com.meetup.backend.repository.party.PartyRepository;
import com.meetup.backend.repository.party.PartyUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by seongmin on 2022/11/08
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final PartyUserRepository partyUserRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long createParty(String userId, PartyRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        if (partyUserRepository.existByGroupName(user, true, requestDto.getName())) {
            throw new ApiException(DUPLICATE_GROUP);
        }

        Party party = new Party(requestDto.getName());
        requestDto.getMembers().forEach(memberId -> {
            User member = userRepository.findById(memberId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
            party.addPartyUser(new PartyUser(member, party, false));
        });

        party.addPartyUser(new PartyUser(user, party, true));
        return partyRepository.save(party).getId();
    }
}
