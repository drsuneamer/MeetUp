package com.meetup.backend.service.party;

import com.meetup.backend.dto.party.PartyRequestDto;
import com.meetup.backend.dto.party.PartyResponseDto;
import com.meetup.backend.dto.user.UserInfoDto;
import com.meetup.backend.entity.party.Party;
import com.meetup.backend.entity.party.PartyUser;
import com.meetup.backend.entity.user.User;
import com.meetup.backend.exception.ApiException;
import com.meetup.backend.repository.party.PartyRepository;
import com.meetup.backend.repository.party.PartyUserRepository;
import com.meetup.backend.repository.user.UserRepository;
import com.meetup.backend.service.Client;
import com.meetup.backend.service.auth.AuthService;
import com.meetup.backend.util.converter.JsonConverter;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.MattermostClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by seongmin on 2022/11/08
 * updated by seongmin on 2022/11/13
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final PartyUserRepository partyUserRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

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

    @Override
    public List<PartyResponseDto> getMyParty(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        List<PartyUser> myPartyList = partyUserRepository.findByUser(user);

        return myPartyList.stream()
                .map(PartyResponseDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInfoDto> getPartyMembers(String userId, Long partyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));
        if (!partyUserRepository.existsByUserAndParty(user, party)) {
            throw new ApiException(PARTY_ACCESS_DENIED);
        }

        List<UserInfoDto> result = party.getPartyUsers().stream()
                .filter(partyUser -> !partyUser.getUser().equals(user))
                .map(partyUser -> UserInfoDto.of(partyUser.getUser()))
                .collect(Collectors.toList());

        MattermostClient client = Client.getClient();
        client.setAccessToken(authService.getMMSessionToken(userId));
        for (UserInfoDto userInfoDto : result) {
            if (userInfoDto.getNickname() == null) {
                Response response = client.getUser(userInfoDto.getId()).getRawResponse();
                try {
                    JSONObject userObj = JsonConverter.toJson((BufferedInputStream) response.getEntity());
                    userInfoDto.setNickname(userObj.getString("nickname"));
                } catch (ClassCastException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteParty(String userId, Long partyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new ApiException(PARTY_NOT_FOUND));
        PartyUser partyUser = partyUserRepository.findByUserAndParty(user, party).orElseThrow(() -> new ApiException(PARTY_ACCESS_DENIED));
        if (partyUser.isLeader()) {
            partyRepository.delete(party);
        } else {
            partyUserRepository.delete(partyUser);
        }
    }
}
