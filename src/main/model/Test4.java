package main.model;

import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;



public class Test4 {
    private static BufferedReader bufferedReader = null;
    private static HttpURLConnection connection = null;
    private static StringBuffer stringBuffer = null;
    private static int interval = 1;
    private static String url = "";



    public static void main(String[] args) {
        if (args.length == 2) {
            try {
                interval = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("The interval was entered incorrectly.");
                System.exit(0);
            }
            url = args[1];
//            url = "http://httpstat.us/500";

            while (true) {
                try {
                    URL obj = new URL(url);
                    connection = (HttpURLConnection) obj.openConnection();
                    connection.setRequestMethod("GET");

                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    stringBuffer = new StringBuffer();
//                    String inputLine;
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
                    System.out.println("OK(200)");
                    close();
                } catch (Exception e) {
                    close();
                    if (e.getMessage().split(":").length >= 3) {
                        System.out.println("ERR({" + e.getMessage() + "})");
                    } else {
                        System.out.println("URL parsing error");
                        System.exit(0);
                    }
                }
                try {
                    Thread.sleep(interval * 1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    private static void close() {

        try {
            if (bufferedReader != null)
            bufferedReader.close();
        } catch (IOException ioException) { }
        if (connection != null)
        connection.disconnect();
    }
}
