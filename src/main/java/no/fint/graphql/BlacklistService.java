package no.fint.graphql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlacklistService {

    private final ApplicationConfig applicationConfig;

    public BlacklistService(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;

        log.info("Blacklist: ");
        for (String blacklist : applicationConfig.getBlacklist()) {
            log.info(blacklist);
        }
    }

    public void failIfBlacklisted(String ip, String bearerToken) {
        if (StringUtils.isBlank(ip)) return;

        if (applicationConfig.getBlacklist().contains(ip)) {
            log.info("Cast exception because IP is blacklisted");
            log.info("CN blocked was: " + getJwtInfo(bearerToken));
            throw new RuntimeException("Client is blacklisted: " + ip);
        }
    }

    private String getJwtInfo(String bearerToken) {
        try {
            String tokenWithoutBearer = bearerToken.substring(7);
            NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation("https://idp.felleskomponent.no/nidp/oauth/nam/");
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getClaims().get("cn").toString();
        } catch (Exception e) {
            log.error("Error on extract jwt info: " + e.getMessage());
            return "";
        }
    }
}
