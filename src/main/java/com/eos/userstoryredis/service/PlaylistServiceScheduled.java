package com.eos.userstoryredis.service;

import com.eos.userstoryredis.config.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class PlaylistServiceScheduled {

    private static final Logger log = LoggerFactory.getLogger(PlaylistServiceScheduled.class);
    private final RedisUtil redisUtil;

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 1)
    public void updateGenres(){
        List<String> genres = redisUtil.updateGenreSet();
        StringBuilder sb = new StringBuilder();
        genres.forEach(genre -> sb.append(genre).append(", "));
        sb.delete(sb.length() - 2, sb.length()); //removes the last ", "
        String outputGenres = sb.toString();
        log.info("+++PLAYLIST SERVICE+++ Found the following genres: "+outputGenres+".");
    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedDelay = 1)
    public void updateGenreLists(){
        Map<String, Long> updatedGenres = redisUtil.writeKeysIntoGenreList(redisUtil.getGenres());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> updatedGenre : updatedGenres.entrySet()) {
            sb.append(updatedGenre.getKey()).append(": ").append(updatedGenre.getValue()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()); //removes the last ", "
        String outputGenres = sb.toString();
        log.info("+++PLAYLIST SERVICE+++ Added songs: "+outputGenres+".");
    }
}
