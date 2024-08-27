package com.treadingPlatformApplication.service;
import com.treadingPlatformApplication.models.Coin;
import java.util.List;

public interface CoinService {
    List<Coin> getCoinList(int page);
    String getMarketChart(String coinId,int days);
    String getCoinDetails(String coinId);
    Coin findById(String coinId);
    String searchCoin(String keywords);
    String getTop50CoinsByMarketRank();
    String getTreadingCoins();
}
