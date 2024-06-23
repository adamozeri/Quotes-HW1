package com.example.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.common.databinding.ActivityPanelBaseBinding;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PanelBaseActivity extends AppCompatActivity {

    private ActivityPanelBaseBinding binding;
    private SharedPreferences sp;
    private DataManager dataManager;
    private static final String PREFS_NAME = "QuotesPrefs";
    private static final String NO_QUOTE_AVAILABLE = "No Quoutes Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panel_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityPanelBaseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sp = this.getSharedPreferences("QuotesPrefs", MODE_PRIVATE);
        dataManager = new DataManager();
        dataManager.setQuotes(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.quotes))));
        loadQuotes();
        updateView();
        addListeners();
    }

    private void updateView() {
        ArrayList<String> quotes = dataManager.getQuotes();
        if (!quotes.isEmpty()) {
            binding.quote.setText(quotes.get(dataManager.getCurrentIndex()));
        } else {
            binding.quote.setText(NO_QUOTE_AVAILABLE);
        }
    }


    private void addListeners() {
        binding.addBtn.setOnClickListener(add -> addQuote());
        binding.nextBtn.setOnClickListener(next -> binding.quote.setText(nextQuote()));
        binding.backBtn.setOnClickListener(next -> binding.quote.setText(prevQuote()));
    }

    private void addQuote() {
        dataManager.addQuote(Objects.requireNonNull(binding.quoteInput.getText()).toString());
        if (dataManager.getQuotes().size() == 1) {
            binding.quote.setText(dataManager.getQuotes().get(dataManager.getCurrentIndex()));
        }
        binding.quoteInput.setText(null);
        saveQuotes();
    }

    private String nextQuote() {
        ArrayList<String> quotes = dataManager.getQuotes();
        if (dataManager.getCurrentIndex() < quotes.size() - 1) {
            dataManager.setCurrentIndex(dataManager.getCurrentIndex() + 1);
            return quotes.get(dataManager.getCurrentIndex());
        }
        if (!quotes.isEmpty()) {
            return quotes.get(quotes.size() - 1);
        }
        return NO_QUOTE_AVAILABLE;
    }

    private String prevQuote() {
        ArrayList<String> quotes = dataManager.getQuotes();
        if (dataManager.getCurrentIndex() > 0) {
            dataManager.setCurrentIndex(dataManager.getCurrentIndex() - 1);
            return quotes.get(dataManager.getCurrentIndex());
        } else if (!quotes.isEmpty()) {
            return quotes.get(0);
        }
        return NO_QUOTE_AVAILABLE;
    }


    private void loadQuotes() {
        String quotesJson = sp.getString(PREFS_NAME, null);

        if (quotesJson != null) {

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> quotes = gson.fromJson(quotesJson, type);

            dataManager.setQuotes(quotes);
        }
    }

    private void saveQuotes() {
        Gson gson = new Gson();
        String quotesJson = gson.toJson(dataManager.getQuotes());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_NAME, quotesJson);
        editor.apply();
    }

}