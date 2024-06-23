package com.example.common;

import java.util.ArrayList;

public class DataManager {

    private ArrayList<String> quotes;
    private int currentIndex = 0;

    public DataManager() {
        this.quotes = new ArrayList<>();
    }


    public ArrayList<String> getQuotes() {
        return quotes;
    }


    public DataManager setQuotes(ArrayList<String> quotes) {
        this.quotes = quotes;
        return this;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void addQuote(String quote)
    {
        this.quotes.add(quote);
    }
}
