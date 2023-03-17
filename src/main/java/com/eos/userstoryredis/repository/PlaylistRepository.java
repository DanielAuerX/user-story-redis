package com.eos.userstoryredis.repository;

import com.eos.userstoryredis.model.Playlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {

}
