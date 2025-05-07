package com.mycompany.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Task3 {
    public static void start() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";
            webDriver.get(url);

            WebElement elem = webDriver.findElement(By.tagName("pre"));
            String jsonStr = elem.getText();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonStr);

            JSONObject hourly = (JSONObject) obj.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            PrintWriter writer = new PrintWriter(new FileWriter("../result/forecast.txt"));
            writer.printf("%-3s %-20s %-12s %-10s%n", "№", "Дата/время", "Температура", "Осадки (мм)");

            for (int i = 0; i < times.size(); i++) {
                String time = (String) times.get(i);
                double temp = (Double) temperatures.get(i);
                double rain = (Double) rains.get(i);
                writer.printf("%-3d %-20s %-12.1f %-10.2f%n", i + 1, time, temp, rain);
            }

            writer.close();
            System.out.println("Прогноз погоды записан в result/forecast.txt");

        } catch (Exception e) {
            System.out.println("Ошибка при получении прогноза погоды");
            System.out.println(e.toString());
        } finally {
            webDriver.quit();
        }
    }
}
