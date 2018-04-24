package com.locationtracker.Repository;

import com.locationtracker.Model.Point;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PointRepository extends CrudRepository<Point,Long> {
    List<Point> findAllById(Long id);

    List<Point> findAllByTrackId(int id);

    Point findFirstByTrackId(int id);

    Point findByTrackIdOrderByIdDesc(int id);
}
