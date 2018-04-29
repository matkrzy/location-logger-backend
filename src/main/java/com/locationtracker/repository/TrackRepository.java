package com.locationtracker.repository;

import com.locationtracker.model.Track;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrackRepository extends CrudRepository<Track,Long> {
    List<Track> findAll();

    List<Track> findByRemovedIsFalse();

    Track findById(int id);

    Track save(Track track);
}
