package com.rct.adapter;

import com.rct.models.QuoteModel;

import org.json.JSONException;

import java.io.IOException;

public interface OnQuoteClick {
    void OnQuoteClick (QuoteModel model) throws JSONException, IOException;
}
