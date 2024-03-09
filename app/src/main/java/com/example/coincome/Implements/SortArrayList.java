package com.example.coincome.Implements;

import android.util.Log;

import com.example.coincome.RecyclerView.Coin;

import java.util.Comparator;
public class SortArrayList {
    public abstract class NameSortArrayList implements Comparator<Coin> {
        private int index;
        public NameSortArrayList(int index) {
            this.index = index;
        }

        @Override
        public int compare(Coin coin, Coin t1) {
            switch (index){
                case -1:
                    if(coin.getCoinName().compareTo(t1.getCoinName())>0){
                        return -1;
                    }
                    return 1;
                case 1:
                    if(coin.getCoinName().compareTo(t1.getCoinName())>0){
                        return 1;
                    }
                    return -1;
                default:
                    if(coin.getTradeVolume()>t1.getTradeVolume()){
                        return -1;
                    }else if(coin.getTradeVolume()<t1.getTradeVolume()){
                        return 1;
                    }
                    return 0;
            }
        }
    }
    public abstract class PriceSortArrayList implements Comparator<Coin> {
        private int index;
        public PriceSortArrayList(int index) {
            this.index = index;
        }

        @Override
        public int compare(Coin coin, Coin t1) {
            switch (index){
                case -1:
                    if(coin.getCoinPrice().compareTo(t1.getCoinPrice())>0){
                        return -1;
                    }
                    return 1;
                case 1:
                    if(coin.getCoinPrice().compareTo(t1.getCoinPrice())>0){
                        return 1;
                    }
                    return -1;
                default:
                    if(coin.getTradeVolume()>t1.getTradeVolume()){
                        return -1;
                    }else if(coin.getTradeVolume()<t1.getTradeVolume()){
                        return 1;
                    }
                    return 0;
            }
        }
    }
    public abstract class DaytodaySortArrayList implements Comparator<Coin> {
        private int index;
        public DaytodaySortArrayList(int index) {
            this.index = index;
        }

        @Override
        public int compare(Coin coin, Coin t1) {
            switch (index){
                case -1:

                    if(coin.getCoinDaytoday()>t1.getCoinDaytoday()){
                        return -1;
                    }
                    if(coin.getCoinDaytoday()==t1.getCoinDaytoday()){
                        if(coin.getCoinName().compareTo(t1.getCoinName())>0){
                            return 1;
                        }
                    }
                    return 1;
                case 1:
                    if(coin.getCoinDaytoday()>t1.getCoinDaytoday()){
                        return 1;
                    }
                    if(coin.getCoinDaytoday()==t1.getCoinDaytoday()){
                        if(coin.getCoinName().compareTo(t1.getCoinName())>0){
                            return -1;
                        }
                    }
                    return -1;
                default:
                    if(coin.getTradeVolume()>t1.getTradeVolume()){
                        return -1;
                    }else if(coin.getTradeVolume()<t1.getTradeVolume()){
                        return 1;
                    }
                    return 0;
            }
        }
    }
    public abstract class PremiumSortArrayList implements Comparator<Coin> {
        private int index;
        public PremiumSortArrayList(int index) {
            this.index = index;
        }

        @Override
        public int compare(Coin coin, Coin t1) {

            switch (index){
                case -1:
                    if(coin.getCoinPremium()==null){
                        return (t1.getCoinPremium()==null) ? 0 : 1;
                    }
                    if(t1.getCoinPremium()==null){
                        return -1;
                    }
                    return t1.getCoinPremium().compareTo(coin.getCoinPremium());

                case 1:
                    if(coin.getCoinPremium()==null){
                        return (t1.getCoinPremium()==null) ? 0 : 1;
                    }
                    if(t1.getCoinPremium()==null){
                        return -1;
                    }
                    return coin.getCoinPremium().compareTo(t1.getCoinPremium());
                default:
                    if(coin.getTradeVolume()>t1.getTradeVolume()){
                        return -1;
                    }else if(coin.getTradeVolume()<t1.getTradeVolume()){
                        return 1;
                    }
                    return 0;
            }
        }
    }

}
