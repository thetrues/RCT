package com.rctapp.adapter;

import com.rctapp.models.QuoteModel;

import org.json.JSONException;

import java.io.IOException;

public interface OnQuoteClick {
    void OnQuoteClick (QuoteModel model) throws JSONException, IOException;
}
