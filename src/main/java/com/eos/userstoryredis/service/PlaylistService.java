package com.eos.userstoryredis.service;

import com.eos.userstoryredis.config.RedisUtil;
import com.eos.userstoryredis.model.Playlist;
import com.eos.userstoryredis.model.Song;
import com.eos.userstoryredis.repository.PlaylistRepository;
import com.eos.userstoryredis.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PlaylistService {

    public final PlaylistRepository playlistRepository;
    public final SongRepository songRepository;
    private static final Logger log = LoggerFactory.getLogger(PlaylistService.class);
    private final RedisUtil redisUtil;

    public Iterable<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }

    public Playlist generatePlaylist(Playlist playlist, Integer numberOfSongs) {
        List<Song> songsOfGenre = getSongs(playlist.getGenre(), numberOfSongs);
        playlist.setSongs(songsOfGenre);

        log.info("Added " + numberOfSongs + " Songs to playlist '" + playlist.getName() + "' and saved it.");
        return playlistRepository.save(playlist);
    }

    private List<Song> getSongs(String genre, Integer numberOfSongs) {
        List<Song> songsOfGenre = new ArrayList<>();
        Iterable<Song> all = songRepository.findAll();
        for (Song next : all) {
            if (next.getGenre().equals(genre)) {
                songsOfGenre.add(next);
            }
        }
        Collections.shuffle(songsOfGenre);
        return songsOfGenre.subList(0, numberOfSongs);
    }
}
