package com.eos.userstoryredis.controller;

import com.eos.userstoryredis.model.Playlist;
import com.eos.userstoryredis.service.PlaylistService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user-story/playlist")
@RequiredArgsConstructor
//@CrossOrigin
public class PlaylistController {

    private final PlaylistService playlistService;

    public record PlaylistRequest(String name, String genre, Integer numberOfSongs) {
    }

    @PostMapping
    public ResponseEntity<Object> generatePlaylist(@NonNull @RequestBody PlaylistRequest request) {
        Playlist playlist = new Playlist();
        playlist.setName(request.name());
        playlist.setGenre(request.genre());
        return ResponseEntity.ok(playlistService.generatePlaylist(playlist, request.numberOfSongs()));
    }

    @GetMapping
    public ResponseEntity<Object> getPlaylists() {
        return ResponseEntity.ok(playlistService.getPlaylists());
    }
}
