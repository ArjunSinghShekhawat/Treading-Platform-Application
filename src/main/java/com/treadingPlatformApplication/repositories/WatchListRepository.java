package com.treadingPlatformApplication.repositories;
import com.treadingPlatformApplication.models.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList,Long> {
    WatchList findByUserId(Long userId);
}
