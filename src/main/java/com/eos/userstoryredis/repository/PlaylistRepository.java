package com.eos.userstoryredis.repository;

import com.eos.userstoryredis.model.Playlist;
import com.eos.userstoryredis.model.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
}
