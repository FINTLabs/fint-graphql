package no.fint.graphql;

import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.config.ApplicationConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class BlacklistService {

    @Value("${fint.graphql.blacklist:}")
    private List<String> blacklist;

    @PostConstruct
    public void init() {
        log.info("Blacklist: {}", blacklist);
    }

    public void failIfBlacklisted(String ip, String bearerToken) {
        if (StringUtils.isBlank(ip)) return;

        if (blacklist.contains(ip)) {
            log.info("Cast exception because IP is blacklisted " + bearerToken);
            throw new RuntimeException("Client is blacklisted: " + ip);
        }
    }
}
