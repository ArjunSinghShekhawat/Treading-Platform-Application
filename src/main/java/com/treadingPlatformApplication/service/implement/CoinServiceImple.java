package com.treadingPlatformApplication.service.implement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.repositories.CoinRepository;
import com.treadingPlatformApplication.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class CoinServiceImple implements CoinService {


    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public List<Coin> getCoinList(int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        int retries = 5;
        long waitTime = 1000; // Initial wait time in milliseconds

        for (int i = 0; i < retries; i++) {
            try {
                ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
                return this.objectMapper.readValue(response.getBody(), new TypeReference<List<Coin>>() {});
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    System.out.println("Rate limit exceeded. Retrying after " + waitTime + " ms...");
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted during sleep", interruptedException);
                    }
                    waitTime *= 2; // Exponential backoff
                } else {
                    throw new RuntimeException("Error fetching coin data", e);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error processing JSON response", e);
            }
        }
        throw new RuntimeException("Exceeded maximum retry attempts");
    }




    @Override
    public String getMarketChart(String coinId, int days) {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId+"/market_chart?vs_currency=usd&days="+days;

        RestTemplate template = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            ResponseEntity<String>response = template.exchange(url,HttpMethod.GET,entity,String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public String getCoinDetails(String coinId) {

        String url = "https://api.coingecko.com/api/v3/coins/"+coinId;
        RestTemplate template = new RestTemplate();

        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            ResponseEntity<String>response = template.exchange(url, HttpMethod.GET,entity,String.class);
            JsonNode jsonNode = this.objectMapper.readTree(response.getBody());

            Coin coin = new Coin();

            coin.setId(jsonNode.get("id").asText());
            coin.setName(jsonNode.get("name").asText());
            coin.setSymbol(jsonNode.get("symbol").asText());
            coin.setImage(jsonNode.get("image").get("large").asText());

            JsonNode marketData = jsonNode.get("market_data");

            coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
            coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
            coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
            coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
            coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
            coin.setTotalSupply(marketData.get("total_supply").asLong());
            this.coinRepository.save(coin);

            return response.getBody();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Coin findById(String coinId) {
        return this.coinRepository.findById(coinId).orElseThrow(()->new ResourceNotFoundException("coin","coinId",String.valueOf(coinId)));
    }

    @Override
    public String searchCoin(String keywords) {

        String url = "https://api.coingecko.com/api/v3/search?query="+keywords;
        RestTemplate restTemplate = new RestTemplate();

        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String>entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String>response = restTemplate.exchange(url,HttpMethod.GET,entity,String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getTop50CoinsByMarketRank() {

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();

        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String>entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String>response = restTemplate.exchange(url,HttpMethod.GET,entity,String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getTreadingCoins() {
        String url = "https://api.coingecko.com/api/v3/search/trending";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
