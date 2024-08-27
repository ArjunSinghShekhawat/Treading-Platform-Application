package com.treadingPlatformApplication.service.implement;
import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.Asset;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.repositories.AssetRepository;
import com.treadingPlatformApplication.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetServiceImple implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {

        Asset asset = new Asset();
        asset.setUser(user);
        asset.setQuantity(quantity);
        asset.setCoin(coin);
        asset.setBuyPrice(coin.getCurrentPrice());

        return this.assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) {
        return this.assetRepository.findById(assetId).orElseThrow(()->new ResourceNotFoundException("asset","id",String.valueOf(assetId)));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAsset(Long userId) {
        return this.assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
        Asset asset = getAssetById(assetId);
        asset.setQuantity(quantity+asset.getQuantity());
        return this.assetRepository.save(asset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return this.assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        this.assetRepository.deleteById(assetId);
    }
}
