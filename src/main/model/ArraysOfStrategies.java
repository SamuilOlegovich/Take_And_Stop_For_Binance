package main.model;

import java.util.ArrayList;



public class ArraysOfStrategies {
    private final ArrayList<StrategyObject> stoppedStrategyList;
    private final ArrayList<StrategyObject> tradedStrategyList;

    private WriteKeysAndSettings writeKeysAndSettings;
    private ArraysOfWebSockets arraysOfWebSockets;
    private StrategyObject strategyObject;



    public ArraysOfStrategies() {
        this.stoppedStrategyList = new ArrayList<>();
        this.tradedStrategyList = new ArrayList<>();
        this.writeKeysAndSettings = null;
    }



    public void addToAllStrategyList(StrategyObject in, boolean b) {
        // в зависимости от объекта добавим его в тот или иной лист
        StrategyObject sO = in;
        if (sO.getWorks()) { tradedStrategyList.add(sO); }
        else { stoppedStrategyList.add(sO); }
        if (b) { writeKeysAndSettings.writeNewSettingsAndStates(); }
    }



    public void replaceStrategy(StrategyObject in) {
        // находит по стратегие - strategyObject - удаляем её и вставляем новую
        // для новой будет новый айди - его находим и все ее обслуживание переводим теперь на него
        // это делаем везде где она может быть
        String id = in.getClassID();
        boolean works = in.getWorks();
        int index = -1;

        for (StrategyObject object : works ? tradedStrategyList : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                if (works) {
                    object.setWorks(false);
                    index = tradedStrategyList.indexOf(object);
                    break;
                } else {
                    index = stoppedStrategyList.indexOf(object);
                    break;
                }
            }
        }

        if (works) { tradedStrategyList.remove(index); }
        else { stoppedStrategyList.remove(index); }

        in.setClassID();
        in.setWorks(false);
        stoppedStrategyList.add(in);
        writeKeysAndSettings.writeNewSettingsAndStates();
    }



    // найти стратегию
    public synchronized void findStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id;
        if (in.contains(Lines.delimiter)) { id = in.split(Lines.delimiter)[0]; }
        else { id = in; }

        for (StrategyObject object : tradedStrategyList) {
            if (id.equals(object.getClassID())) {
                strategyObject = object;
                return;
            }
        }
        for (StrategyObject object : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                strategyObject = object;
                return;
            }
        }
    }



    // запустить стратегию
    public void launchStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.delimiter)[0];
        StrategyObject sObject = null;
        int index = -1;

        for (StrategyObject object : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                index = stoppedStrategyList.indexOf(object);
                sObject = object;
                break;
            }
        }

        if (sObject != null) {
            sObject.setWorks(true);
            tradedStrategyList.add(sObject);
            stoppedStrategyList.remove(index);
            new RefreshList();
            writeKeysAndSettings.writeNewSettingsAndStates();
            arraysOfWebSockets.addOneStrategyToWiretap(sObject);
        }
    }



    // остановить стратегию
    public void stopStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.delimiter)[0];
        StrategyObject sObject = null;
        System.out.println(id);
        int index = -1;

        for (StrategyObject s : tradedStrategyList) {
            if (id.equals(s.getClassID())) {
                index = tradedStrategyList.indexOf(s);
                sObject = s;
                break;
            }
        }

        if (sObject != null) {
            sObject.setWorks(false);
            tradedStrategyList.remove(index);
            stoppedStrategyList.add(sObject);
            new RefreshList();
            writeKeysAndSettings.writeNewSettingsAndStates();
        }
    }



    // удалить стратегию
    public synchronized void removeStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию и удаляем ее отовсюду
        String id = in.split(Lines.delimiter)[0];
        int index = -1;

        for (StrategyObject object : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                index = stoppedStrategyList.indexOf(object);
                break;
            }
        }

        if (index >= 0 ) {
            stoppedStrategyList.remove(index);
            new RefreshList();
        }

        writeKeysAndSettings.writeNewSettingsAndStates();
    }



    public void setArraysOfWebSockets(ArraysOfWebSockets arraysOfWebSockets) { this.arraysOfWebSockets = arraysOfWebSockets; }
    public void setWriteKeysAndSettings(WriteKeysAndSettings in) { this.writeKeysAndSettings = in; }
    public ArrayList<StrategyObject> getStoppedStrategyListObject() { return stoppedStrategyList; }
    public ArrayList<StrategyObject> getTradedStrategyListObject() { return tradedStrategyList; }
    public StrategyObject getStrategySettingAndStatus() { return strategyObject; }
}
