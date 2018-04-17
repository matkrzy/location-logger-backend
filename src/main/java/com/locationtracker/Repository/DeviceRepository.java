package com.locationtracker.Repository;

import com.locationtracker.Model.Device;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findAll();

    List<Device> findByRemovedIsFalse();

    List<Device> findByRemovedIsFalseAndUserId(int id);

    Device findById(int id);

    Device save(Device device);

}
