package com.rctapp.utils;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttUtil2  implements MqttCallback {
    String TAG = "Mqtt";
    public void subscribeMessage(String quoteId) {
        System.out.println("Subscription started...");
        try {
            MqttClient client = new MqttClient(Vars.MQTT_BROKER_ADDR, MqttClient.generateClientId(), new MemoryPersistence());
            client.connect();
            client.setCallback(this);
            client.subscribe(quoteId);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("Mqtt", "subscribeMessage: "+ e.getMessage());
        }
    }
    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "connectionLost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        Log.d(TAG, payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete");
    }
}
