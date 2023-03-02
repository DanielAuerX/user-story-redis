package com.eos.userstoryredis.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Data
@RedisHash("Song")
public class Song implements Serializable {

    private UUID id;
    private String name;
    private String artist;
    private String album;
    private String genre;

}