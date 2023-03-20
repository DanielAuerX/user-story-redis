package com.eos.userstoryredis.config;

import com.eos.userstoryredis.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisCommandsProvider;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    //private final RedisTemplate<String, Object> redisTemplate;
    private final RedisCommandsProvider redisCommandsProvider;

    public List<String> updateGenreSet() {
        List<String> genres = new ArrayList<>();
        Cursor<byte[]> scan = getAllSongs();
        while (scan.hasNext()) {
            Map<String, String> songAsMap = getSongAsMap(scan);
            if (songAsMap.containsKey("genre") && !genres.contains(songAsMap.get("genre"))) {
                genres.add(songAsMap.get("genre"));
            }
        }
        if (!genres.isEmpty())
            for (String genre : genres) {
                redisCommandsProvider.setCommands().sAdd("genres".getBytes(), genre.getBytes());
            }
        return genres;
    }

    private Cursor<byte[]> getAllSongs() {
        try {
            ScanOptions match = ScanOptions.scanOptions().match("Song:*").build();
            return redisCommandsProvider.keyCommands().scan(match);
        } catch (Exception e) {
            throw new RuntimeException("A problem occurred while scanning for songs!", e);
        }
    }

    private Map<String, String> getSongAsMap(Cursor<byte[]> scan) {
        Map<byte[], byte[]> song = redisCommandsProvider.hashCommands().hGetAll(scan.next());
        Map<String, String> songAsString = new HashMap<>();
        if (song != null) {
            for (Map.Entry<byte[], byte[]> entry : song.entrySet()) {
                String key = new String(entry.getKey(), StandardCharsets.UTF_8);
                String value = new String(entry.getValue(), StandardCharsets.UTF_8);
                songAsString.put(key, value);
            }
        }
        return songAsString;
    }

    public Set<byte[]> getGenres() {
        try {
            return redisCommandsProvider.setCommands().sMembers("genres".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Long> writeKeysIntoGenreList(Set<byte[]> genres) {
        Map<String, Long> addedValues = new HashMap<>();
        try {
            for (byte[] genre : genres) {
                List<String> songKeys = new ArrayList<>();
                Cursor<byte[]> scan = getAllSongs();
                while (scan.hasNext()) {
                    Map<String, String> song = getSongAsMap(scan);
                    if (song.containsKey("genre") && song.get("genre").equals(new String(genre))) {
                        songKeys.add("Song:" + song.get("id"));
                    }
                }
                Long counter = putSongKeysIntoGenreSet(genre, songKeys);
                addedValues.put(new String(genre), counter);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addedValues;
    }

    private Long putSongKeysIntoGenreSet(byte[] genre, List<String> songKeys) {
        Long counter = 0L;
        for (String songKey : songKeys) {
            Long addedKey = redisCommandsProvider.setCommands().sAdd(genre, songKey.getBytes());
            counter += addedKey;
        }
        return counter;
    }

    public List<Song> getRandomSongsByGenre(String genre, Integer amount) {
        //Long numberOfSongsInGenre = redisCommandsProvider.setCommands().sCard(genre.getBytes());
        List<Song> songs = new ArrayList<>();
        try {
            List<byte[]> keys = redisCommandsProvider.setCommands().sRandMember(genre.getBytes(), amount * -1L); //negative number: if not enough songs, duplicated
            if (keys.isEmpty()) {
                throw new RuntimeException("There are no songs in this genre");
            }
            for (byte[] key : keys) {
                Map<byte[], byte[]> songMap = redisCommandsProvider.hashCommands().hGetAll(key);
                Song song = Song.mapBytesToSong(songMap);
                songs.add(song);
            }
            return songs;
        } catch (Exception e) {
            throw new RuntimeException("A problem occurred while retrieving the random songs", e);
        }

    }

    /*
    public List<Map<String, String>> getSongByGenreDeprecated(String genre, Integer amount) {
        List<Map<String, String>> songs = new ArrayList<>();
        ScanOptions match = ScanOptions.scanOptions().match("Song:*").build();
        Cursor<byte[]> scan = redisCommandsProvider.keyCommands().scan(match);
        while (scan.hasNext()) {
            Map<byte[], byte[]> song = redisCommandsProvider.hashCommands().hGetAll(scan.next());
            Map<String, String> songAsString = new HashMap<>();
            for (Map.Entry<byte[], byte[]> entry : song.entrySet()) {
                String key = new String(entry.getKey(), StandardCharsets.UTF_8);
                String value = new String(entry.getValue(), StandardCharsets.UTF_8);
                songAsString.put(key, value);
            }
            if (songAsString.containsKey("genre") && songAsString.get("genre").equals(genre)) {
                songs.add(songAsString);
            }
        }
        Collections.shuffle(songs);
        return songs.subList(0, amount);
    }

    public List<Map<String, String>> findSongByGenreDeprecated(String genre, int amount) {
        List<Map<String, String>> songs = new ArrayList<>();
        ScanOptions match = ScanOptions.scanOptions().match("Song:*").build();
        Cursor<byte[]> scan = redisCommandsProvider.keyCommands().scan(match);
        while (scan.hasNext()) {
            Map<byte[], byte[]> song = redisCommandsProvider.hashCommands().hGetAll(scan.next());
            Map<String, String> songAsString = new HashMap<>();
            for (Map.Entry<byte[], byte[]> entry : song.entrySet()) {
                String key = new String(entry.getKey(), StandardCharsets.UTF_8);
                String value = new String(entry.getValue(), StandardCharsets.UTF_8);
                songAsString.put(key, value);
            }
            if (songAsString.containsKey("genre") && songAsString.get("genre").equals(genre)) {
                songs.add(songAsString);
            }
        }
        Collections.shuffle(songs);
        return songs.subList(0, amount);
    }




    public String getRandomKey() {
        byte[] randomKeyBytes = redisCommandsProvider.keyCommands().randomKey();
        if (randomKeyBytes != null) {
            return new String(randomKeyBytes);
        }
        throw new RuntimeException("Random key is null!");
    }


    public String getRandomKeyDeprecated() {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] randomKeyBytes = connection.randomKey();
            if (randomKeyBytes != null) {
                return new String(randomKeyBytes);
            }
            throw new RuntimeException("Random key is null!");
        });
    }

     */
}


