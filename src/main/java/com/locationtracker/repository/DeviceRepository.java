package com.locationtracker.repository;

import com.locationtracker.model.Device;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findAll();

    List<Device> findByRemovedIsFalseAndUserId(int id);

    Device findById(int id);

    Device findByUuid(String uuid);

    Device save(Device device);

}
