package com.example.quochuy.cry;

/**
 * Created by Quoc Huy on 4/5/2018.
 */

public class CoinAPI {
    private String coinLimit="http://cryws.herokuapp.com/api/coins/nonchart/offset/";
    private String coinDetail="http://cryws.herokuapp.com/api/coins/nonchart/";
    private String coinDetail_7day="http://cryws.herokuapp.com/api/coins/chart7days/";
    private String coinDetail_All="http://cryws.herokuapp.com/api/coins/";
    private String coinImage ="http://cryws.herokuapp.com/res/coins_high/32/icon/";

    public String getCoinLimit(int start_position,int end_position)
    {
        return coinLimit+start_position+"/"+end_position;
    }

    public String getCoinDetail(String coin)
    {
        return coinDetail+coin;
    }

    public String getCoinDetail_7day(String coin)
    {
        return coinDetail_7day+coin;
    }

    public String getCoinDetail_All(String coin)
    {
        return coinDetail_All+coin;
    }

    public String getCointImage(String symbol){
        return this.coinImage+symbol+".png";
    }
}
