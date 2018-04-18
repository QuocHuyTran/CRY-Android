package com.example.quochuy.cry.Coin;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Quoc Huy on 4/6/2018.
 */

public class Max7Days_Values {
    private LastValues[] lastValues=new LastValues[12];

    public Max7Days_Values(LastValues[] lastValues) {
        this.lastValues = lastValues;
    }

    public Max7Days_Values() {
    }

    public LastValues[] getLastValues() {
        return lastValues;
    }

    public void setLastValues(LastValues[] lastValues) {
        this.lastValues = lastValues;
    }

    public Float[] getArray(){
        Float arrayValues[]=new Float[12];
        try{
            for(int i=0;i<9;i++)
            {
                arrayValues[i]=Float.parseFloat(lastValues[i].getPrice());
            }
        }catch (Exception e)
        {
            Log.e("Error array",e.toString());
        }
        return arrayValues;
    }

    public float getMinPrice(){
        float minPrice=0;

        ArrayList <Float> arrayList=new ArrayList<>();
        for (int i=0;i<8;i++)
        {
            arrayList.add(Float.parseFloat(lastValues[i].getPrice()));
        }
        Collections.sort(arrayList);
        minPrice=arrayList.get(0);
        return minPrice;
    }

    public float getMaxPrice(){
        float maxPrice=0;

        ArrayList <Float> arrayList=new ArrayList<>();
        for (int i=0;i<8;i++)
        {
            arrayList.add(Float.parseFloat(lastValues[i].getPrice()));
        }
        Collections.sort(arrayList);
        maxPrice=arrayList.get(arrayList.size()-1);
        return maxPrice;
    }
}
