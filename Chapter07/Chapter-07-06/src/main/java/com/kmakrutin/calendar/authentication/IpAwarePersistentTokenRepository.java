package com.kmakrutin.calendar.authentication;

import com.kmakrutin.calendar.domain.PersistentToken;
import com.kmakrutin.calendar.repositories.PersistentTokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class IpAwarePersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentTokenRepositoryImpl tokenRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentToken persistentToken = new PersistentToken();
        persistentToken.setSeries(getIpSeries(token.getSeries()));
        persistentToken.setUsername(token.getUsername());
        persistentToken.setToken(token.getTokenValue());
        persistentToken.setLastUsed(token.getDate());
        tokenRepository.save(persistentToken);
    }

    private String getIpSeries(String tokenSeries) {
        return tokenSeries + getIP();
    }

    private String getIP() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest().getRemoteAddr();
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        tokenRepository.findById(getIpSeries(series)).ifPresent(persistentToken -> {
            persistentToken.setToken(tokenValue);
            persistentToken.setLastUsed(lastUsed);
            tokenRepository.save(persistentToken);
        });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return tokenRepository.findById(getIpSeries(seriesId))
                .map(persistentToken -> new PersistentRememberMeToken(
                        persistentToken.getUsername(),
                        seriesId,
                        persistentToken.getToken(),
                        persistentToken.getLastUsed()))
                .orElse(null);
    }

    @Override
    public void removeUserTokens(String username) {
        tokenRepository.deleteByUsername(username);
    }
}
