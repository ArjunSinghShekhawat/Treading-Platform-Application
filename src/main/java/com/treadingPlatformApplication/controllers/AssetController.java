package com.treadingPlatformApplication.controllers;
import com.treadingPlatformApplication.models.Asset;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.service.AssetService;
import com.treadingPlatformApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    private ResponseEntity<Asset>getAssetById(@PathVariable Long assetId){
        Asset asset = this.assetService.getAssetById(assetId);
        return new ResponseEntity<>(asset, HttpStatus.FOUND);
    }
    @GetMapping("coin/{coinId}/user")
    private ResponseEntity<Asset>getAssetByUserIdAndCoinId(@PathVariable String coinId, @RequestHeader("Authorization")String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        Asset asset = this.assetService.findAssetByUserIdAndCoinId(user.getId(),coinId);
        return new ResponseEntity<>(asset, HttpStatus.FOUND);
    }
    @GetMapping()
    public ResponseEntity<List<Asset>>getAssetForUser(@RequestHeader("Authorization")String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Asset> assets=  this.assetService.getUserAsset(user.getId());
        return  new ResponseEntity<>(assets, HttpStatus.OK);
    }
}
