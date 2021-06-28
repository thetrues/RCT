package com.rctapp.utils;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class TaskNotify extends IntentService {
    /**
     * @param name
     * @deprecated
     */
    public TaskNotify(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
