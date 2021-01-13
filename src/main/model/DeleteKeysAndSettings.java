package main.model;

import main.model.binance.API;

public class DeleteKeysAndSettings {
    public DeleteKeysAndSettings() {
        System.out.println("DeleteKeysAndSettings");
        API.setSecretKey(null);
        API.setApiKey(null);
    }
}
