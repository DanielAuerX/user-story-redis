package com.eos.userstoryredis.controller;

import com.eos.userstoryredis.model.Song;
import com.eos.userstoryredis.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user-story/song")
@RequiredArgsConstructor
//@CrossOrigin
public class SongController {

    private final SongService songService;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @PostMapping
    public ResponseEntity<Object> saveSong(@NonNull @RequestBody Song request){
        return ResponseEntity.ok(songService.saveSong(request));
    }

    @GetMapping
    public ResponseEntity<Object> getSongs(){
        return ResponseEntity.ok(songService.getSongs());
    }
}
