package no.fint.graphql;

import lombok.extern.slf4j.Slf4j;
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

    public boolean isBlacklisted(String ip) {
        if (StringUtils.isBlank(ip)) return false;
        return blacklist.contains(ip);
    }
}
