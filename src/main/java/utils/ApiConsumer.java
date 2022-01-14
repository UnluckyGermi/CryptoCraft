package utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.CryptoManager;
import main.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;


public class ApiConsumer {

    public static HashMap<String, Double> getCoinData(){
        try {
            URL url = new URL("https://api.nomics.com/v1/currencies/ticker?key=m_8d284e6241b41dd8be51568017c72fa055f344b9");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
            }

            BufferedReader json = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            JsonArray jsonArray = new Gson().fromJson(json, JsonArray.class);

            HashMap<String, Double> coinData = new HashMap<>();

            for(int i = 0; i < jsonArray.size(); i++){
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String name = jsonObject.get("currency").getAsString();
                double value = jsonObject.get("price").getAsDouble();
                coinData.put(name, value);
                Main.plugin.getLogger().info(name);
            }

            return coinData;

        } catch (Exception e) {
            System.out.println("Exception in API call: " + e);
        }

        return null;

    }



}
