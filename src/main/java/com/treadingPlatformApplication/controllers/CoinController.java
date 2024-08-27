package com.treadingPlatformApplication.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Coin>>getCoinList(@RequestParam(name = "page",required = false) int page){
       List<Coin>coinList =  this.coinService.getCoinList(page);
       return new ResponseEntity<>(coinList, HttpStatus.OK);
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode>getMarketChart(@PathVariable String coinId,@RequestParam(name = "days") int days){

        String marketChart = this.coinService.getMarketChart(coinId,days);
        JsonNode jsonNode = null;
        try{
            jsonNode =  this.objectMapper.readTree(marketChart);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(jsonNode,HttpStatus.ACCEPTED);
    }
    @GetMapping("/search")
    ResponseEntity<JsonNode>searchCoin(@RequestParam(name = "q") String keywords) {
       String coin =  this.coinService.searchCoin(keywords);
        JsonNode jsonNode = null;
        try{
            jsonNode =  this.objectMapper.readTree(coin);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
       return new ResponseEntity<>(jsonNode,HttpStatus.OK);
    }
    @GetMapping("/top50")
    ResponseEntity<JsonNode>getTop50CoinByMarketChart(){
        String coin = this.coinService.getTop50CoinsByMarketRank();
        JsonNode jsonNode = null;
        try{
            jsonNode =  this.objectMapper.readTree(coin);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(jsonNode,HttpStatus.OK);
    }
    @GetMapping("/treading")
    ResponseEntity<JsonNode>getTreadingCoin() throws JsonProcessingException {
        String coin = this.coinService.getTreadingCoins();
        JsonNode jsonNode = null;
        try{
            jsonNode =  this.objectMapper.readTree(coin);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(jsonNode);
    }
    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode>getCoinDetails(@PathVariable String coinId) throws JsonProcessingException {
        String coin = coinService.getCoinDetails(coinId);
        JsonNode jsonNode = null;
        try{
            jsonNode =  this.objectMapper.readTree(coin);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(jsonNode);
    }
}
