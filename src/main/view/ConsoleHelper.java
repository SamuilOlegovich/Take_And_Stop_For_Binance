package main.view;

import main.model.Agent;
import main.model.DatesTimes;
import main.model.Enums;
import main.model.Lines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static void writeMessage(String string) {
        write(DatesTimes.getDateLogs() + Lines.delimiter + string + Lines.newline);
    }



    public static void writeERROR(String string) {
        write( DatesTimes.getDateLogs() + Lines.delimiter + Enums.ERROR + string + Lines.newline);
    }



    public static void writeDEBUG(String string) {
        write( DatesTimes.getDateLogs() + Lines.delimiter + Enums.DEBUG + string + Lines.newline);
    }


    public static void writeINFO(String string) {
        write( DatesTimes.getDateLogs() + Lines.delimiter + Enums.INFO + string + Lines.newline);
    }


    private static void write(String outString) {
        Agent.getWriterAndReadFile().writerFile(outString, Agent.getFilesAndPathCreator().getPathLogs(), true);
        System.out.print(outString);
    }


    public static String readString() {
        try { return reader.readLine();
        } catch (IOException e){
            writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            return readString();
        }
    }



    public static int readInt() {
        try { return Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            return readInt();
        }
    }
}
