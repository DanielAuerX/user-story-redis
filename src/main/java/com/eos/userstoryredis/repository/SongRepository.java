package com.eos.userstoryredis.repository;

import com.eos.userstoryredis.model.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SongRepository extends CrudRepository<Song, UUID> {

}
