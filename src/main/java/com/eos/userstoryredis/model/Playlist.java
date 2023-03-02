package com.eos.userstoryredis.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@RedisHash("Playlist")
public class Playlist implements Serializable {

    private Integer id;  //generates Integer/Long/UUID. How?
    private String name;
    private String genre;
    private List<Song> songs;

}
