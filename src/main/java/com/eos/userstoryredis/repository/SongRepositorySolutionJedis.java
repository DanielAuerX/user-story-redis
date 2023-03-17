package com.eos.userstoryredis.repository;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SongRepositorySolutionJedis {
    private final Jedis jedis;

    public SongRepositorySolutionJedis() {
        this.jedis = new Jedis("localhost", 6379);
    }

    public List<Map<String, String>> findByGenre(String genre) {
        List<Map<String, String>> songs = new ArrayList<>();

        // Set the search pattern to match all Playlist hashes
        ScanParams params = new ScanParams().match("Song:*");
        String cursor = ScanParams.SCAN_POINTER_START;
        List<String> keys;

        // Loop through all keys that match the search pattern
        do {
            // Scan for keys matching the search pattern and get the cursor to the next iteration
            ScanResult<String> scanResult = jedis.scan(cursor, params);
            cursor = scanResult.getCursor();
            keys = scanResult.getResult();

            // Loop through all keys and retrieve the genre field
            for (String key : keys) {
                Map<String, String> song = jedis.hgetAll(key);
                if (song.containsKey("genre") && song.get("genre").equals(genre)) {
                    songs.add(song);
                }
            }
        } while (!cursor.equals(ScanParams.SCAN_POINTER_START));

        return songs;
    }

    public void close() {
        jedis.close();
    }
}
