package main.model;

import main.view.ConsoleHelper;

import java.util.ArrayList;
import java.io.*;



public class WriterAndReadFile {

    public void writerFile(String string, String path, boolean reWrite) {
        File file = new File(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, reWrite))) {
            writer.write(string);
            writer.flush();
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЗАПИСИ файла - " + string + " === " + path);
        }
    }



    public ArrayList<String> readFile(String path) throws Exception {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(path);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) { arrayList.add(reader.readLine()); }
        } catch (Exception e) {
            ConsoleHelper.writeMessage("Ошибка в ЧТЕНИИ файла - " + path);
            throw new Exception();
        }
        return arrayList;
    }
}
