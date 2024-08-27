package com.treadingPlatformApplication.service.implement;

import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.WatchList;
import com.treadingPlatformApplication.repositories.WatchListRepository;
import com.treadingPlatformApplication.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;

public class WatchListImple implements WatchListService {

    @Autowired
    private WatchListRepository watchListRepository;


    @Override
    public WatchList findUserWatchList(Long userId) {
        return this.watchListRepository.findByUserId(userId);
    }

    @Override
    public WatchList createWatchList(User user) {

        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return this.watchListRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) {
        return this.watchListRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("WatchList","id",String.valueOf(id)));
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) {
        WatchList watchList = this.findUserWatchList(user.getId());

        if(watchList.getCoins().contains(coin)){
            watchList.getCoins().remove(coin);
        }
        else{
            watchList.getCoins().add(coin);
        }

        this.watchListRepository.save(watchList);
        return coin;
    }
}
