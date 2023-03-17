package com.eos.userstoryredis.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
@RedisHash("Song")
public class Song implements Serializable {

    private UUID id;
    private String name;
    private String artist;
    private String album;
    private String genre;

    public static Song mapToSong(Map<String, String> songMap){
        Song song = new Song();
        song.setId(UUID.fromString(songMap.get("id")));
        song.setName(songMap.get("name"));
        song.setAlbum(songMap.get("album"));
        song.setGenre(songMap.get("genre"));
        return song;
    }

    public static Song mapBytesToSong(Map<byte[], byte[]> songMap){
        Song song = new Song();
        song.setId(UUID.fromString(new String(songMap.get("id".getBytes()))));
        song.setName(new String(songMap.get("name".getBytes())));
        song.setAlbum(new String(songMap.get("album".getBytes())));
        song.setGenre(new String(songMap.get("genre".getBytes())));
        return song;
    }

}