package com.treadingPlatformApplication.service;

import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.WatchList;

public interface WatchListService {

    WatchList findUserWatchList(Long userId);
    WatchList createWatchList(User user);
    WatchList findById(Long id);
    Coin addItemToWatchList(Coin coin,User user);

}
