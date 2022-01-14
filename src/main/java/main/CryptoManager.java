package main;


import models.CryptoPlayer;
import utils.ApiConsumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CryptoManager {

    private static final int STARTING_BALANCE = Main.plugin.getConfig().getInt("starting-money");

    private static List<CryptoPlayer> CRYPTO_PLAYERS = new ArrayList<>();
    private static HashMap<String, Double> CRYPTO_COINS_VALUE = new HashMap<>();

    public static double getCoinValue(String coin){
        return CRYPTO_COINS_VALUE.get(coin);
    }

    public static void updateData(){
        CRYPTO_COINS_VALUE = ApiConsumer.getCoinData();
    }

    public static void addCryptoPlayer(UUID uuid){
        CRYPTO_PLAYERS.add(new CryptoPlayer(uuid, STARTING_BALANCE));
    }

    public static void addCryptoPlayer(UUID uuid, double balance, HashMap<String, Double> coinsQuantity){
        CRYPTO_PLAYERS.add(new CryptoPlayer(uuid, balance, coinsQuantity));
    }

    public static List<CryptoPlayer> getCryptoPlayers(){
        return CRYPTO_PLAYERS;
    }

    public static CryptoPlayer getCryptoPlayer(UUID uuid){
        for(CryptoPlayer cp : CRYPTO_PLAYERS){
            if(cp.getUuid().equals(uuid)){
                return cp;
            }
        }
        return null;
    }


}
