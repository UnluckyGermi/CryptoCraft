package utils;

import main.CryptoManager;
import main.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Utils {

    public static String currencySymbol = Main.plugin.getConfig().getString("currency-symbol");

    public static double round(double x){
        return (Math.round(x * 100.0) / 100.0);
    }

    public static double round5(double x){
        return (Math.round(x * 10000.0) / 10000.0);
    }

    public static List<String> filterCoin(String s){
        List<String> coinList = new ArrayList<>();
        for(String str : CryptoManager.getCoinList()){
            if(str.toLowerCase().startsWith(s.toLowerCase())){
                coinList.add(str);
            }
        }
        return coinList;
    }

    private static List<String> orderByValue(List<String> array){

        List<String> sorted = array.stream().sorted(
                (String s1, String s2) -> Double.compare(CryptoManager.getCoinValue(s2), CryptoManager.getCoinValue(s1))
        ).collect(Collectors.toList());

        return sorted;
    }

    public static String[][] splitArray(List<String> array, int tam){
        array = orderByValue(array);
        int divisions = array.size() / tam;
        int rest = array.size() % tam;
        if(rest != 0) divisions++;

        String[][] arrayDiv = new String[divisions][tam];

        for(int i = 0; i < array.size(); i++) {
            arrayDiv[i / tam][i % tam] = array.get(i);
        }

        return arrayDiv;
    }
}
