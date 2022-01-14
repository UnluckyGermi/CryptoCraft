package models;

import main.CryptoManager;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CryptoPlayer {

    private final UUID uuid;
    private double balance;

    private HashMap<String, Double> cryptoCoinQuantityMap;

    public CryptoPlayer(UUID uuid, double balance){
        this.uuid = uuid;
        this.balance = balance;

        cryptoCoinQuantityMap = new HashMap<>();
    }

    public CryptoPlayer(UUID uuid, double balance, HashMap<String, Double> cryptoCoinQuantityMap){
        this.uuid = uuid;
        this.balance = balance;
        this.cryptoCoinQuantityMap = cryptoCoinQuantityMap;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCoinQuantity(String coin){
        return cryptoCoinQuantityMap.get(coin);
    }

    public double getCoinTotalValue(String coin){
        return getCoinQuantity(coin) * CryptoManager.getCoinValue(coin);
    }

    public List<String> getCoinList(){
        return new ArrayList<>(cryptoCoinQuantityMap.keySet());
    }

    public double getAllCoinsTotalValue(){
        double total = 0;

        for(String s : cryptoCoinQuantityMap.keySet()){
            total += getCoinTotalValue(s);
        }

        return total;
    }
}
