package com.treadingPlatformApplication.service;

import com.treadingPlatformApplication.models.Asset;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity);
    Asset getAssetById(Long assetId);
    Asset getAssetByUserIdAndId(Long userId,Long assetId);
    List<Asset> getUserAsset(Long userId);
    Asset updateAsset(Long assetId,double quantity);
    Asset findAssetByUserIdAndCoinId(Long userId,String coinId);
    void deleteAsset(Long assetId);

}
