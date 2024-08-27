package com.treadingPlatformApplication.controllers;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.WatchList;
import com.treadingPlatformApplication.service.CoinService;
import com.treadingPlatformApplication.service.UserService;
import com.treadingPlatformApplication.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList>getUserWatchList(@RequestHeader("Authorization") String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        WatchList watchList = this.watchListService.findUserWatchList(user.getId());
        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }
    @GetMapping("/{watchListId")
    public ResponseEntity<WatchList>getWatchListById(@PathVariable Long watchListId){
        WatchList watchList = this.watchListService.findById(watchListId);
        return new ResponseEntity<>(watchList,HttpStatus.OK);
    }
    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin>addItemToWatchList(@RequestHeader("Authorization") String jwt,@PathVariable String coinId){
        Coin coin = this.coinService.findById(coinId);
        User user = this.userService.findUserProfileByJwt(jwt);
        Coin savedCoin= this.watchListService.addItemToWatchList(coin,user);
        return new ResponseEntity<>(savedCoin,HttpStatus.OK);
    }
}
