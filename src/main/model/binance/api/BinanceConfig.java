package main.model.binance.api;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================
 * */


import org.sellcom.core.Strings;

import java.util.Properties;




public class BinanceConfig {

    /**
     * properties that are loaded from local resource file
     * свойства, которые загружаются из локального файла ресурсов
     */
    private Properties prop = null;

    public BinanceConfig() {
        this.loadProperties();
    }

    /**
     *  Loading available properties from local resource file
     *  Загрузка доступных свойств из файла локальных ресурсов
     */
    protected void loadProperties() {
        try {
            prop = new Properties();
            prop.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            // it is fine not to have that resource file
            // ignoring any error here
            // это нормально, если у вас нет этого файла ресурсов
            // игнорируем любую ошибку здесь
        }
    }

    /**
     * Getting variable from one of the multiple sources available
     * Получение переменной из одного из нескольких доступных источников
     *
     * @param key variable name => имя переменной
     * @return string result => строковый результат
     */
    public String getVariable(String key) {
        // checking VM options for properties
        // проверка параметров виртуальной машины для свойств
        String sysPropertyValue = System.getProperty(key);
        if (!Strings.isNullOrEmpty(sysPropertyValue)) return sysPropertyValue;

        // checking enviroment variables for properties
        // проверка переменных среды для свойств
        String envPropertyValue = System.getenv(key);
        if (!Strings.isNullOrEmpty(envPropertyValue)) return envPropertyValue;

        // checking resource file for property
        // проверка файла ресурсов на наличие свойств
        if (prop != null) {
            String property = prop.getProperty(key);
            if (!Strings.isNullOrEmpty(property)) return property;
        }
        return "";
    }
}
