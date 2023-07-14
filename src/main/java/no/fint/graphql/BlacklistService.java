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
            log.info("Cast exception because IP is blacklisted " + bearerToken);
            throw new RuntimeException("Client is blacklisted: " + ip);
        }
    }
}
