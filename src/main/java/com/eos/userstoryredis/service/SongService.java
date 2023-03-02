package com.eos.userstoryredis.service;


import com.eos.userstoryredis.model.Song;
import com.eos.userstoryredis.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    public Song saveSong(Song song){
        song.setId(UUID.randomUUID());
        return songRepository.save(song);
    }

    public List<Song> getSongs(){
        Iterable<Song> songsIterable = songRepository.findAll();

        if (songsIterable.iterator().hasNext()){
            return new ArrayList<Song>((Collection) songsIterable);
        }
        else {
            throw new RuntimeException("No songs in the DB.");
        }
    }
}
