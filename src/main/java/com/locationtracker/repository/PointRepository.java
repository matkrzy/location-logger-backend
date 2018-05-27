package com.locationtracker.repository;

import com.locationtracker.model.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointRepository extends CrudRepository<Point,Long> {
    List<Point> findAllByTrackId(int id);

    Integer removeAllByTrackId(int id);

    Point findDistinctTopByTrackId(int id);
}
