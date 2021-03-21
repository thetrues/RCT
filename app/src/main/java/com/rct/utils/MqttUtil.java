package com.rct.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttUtil implements MqttCallback {
    private static volatile MqttUtil instance;
    String TAG = "Mqtt";
    String message = "no sms";
    Context context;
    private MqttUtil() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get a single instance of a singleton");
        }
    }

    public static MqttUtil getInstance() {
        if (instance == null) {
            synchronized (MqttUtil.class) {
                if (instance == null) {
                    instance = new MqttUtil();
                }
            }
        }
        return instance;
    }

    protected MqttUtil readResolve() {
        return getInstance();
    }

    /**
     * Method to publish MQTT message

     */
/*    public void publishMessage(Messenger messenger) {
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            MqttClient mqttClient = new MqttClient(Vars.MQTT_BROKER_ADDR, MqttClient.generateClientId(), memoryPersistence);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttClient.connect(mqttConnectOptions);
            MqttMessage mqttMessage = new MqttMessage(messenger.getMessage().getBytes());
            mqttMessage.setQos(Vars.QOS);
            mqttClient.publish(messenger.getTopic(), mqttMessage);
            mqttClient.disconnect();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }*/

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

    public void test(){
        System.out.println("Test Subscription started...");
        try {
            MqttClient client = new MqttClient(Vars.MQTT_BROKER_ADDR, MqttClient.generateClientId());
            client.connect();
            client.setCallback(this);
            client.subscribe("rct");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d(TAG, "connectionLost");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println(s);
        Log.d( "messageArrived: ", mqttMessage.toString());
        message = mqttMessage.toString();
        //create intent to start activity
        Intent intent = new Intent();
        intent.setClassName(context, "com.rct.utils");
        intent.putExtra("handle", message);

        //notify the user
        Notify.notifcation(context, message, intent, 1);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}
