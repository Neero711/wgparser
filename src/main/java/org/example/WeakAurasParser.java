package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeakAurasParser {

    public static void main(String[] args) {
        String filePath = "/Users/paul/Downloads/List.txt";
        List<String> weakAurasStrings = parseWeakAurasStrings(filePath);

        if (weakAurasStrings.isEmpty()) {
            System.out.println("Строки для WeakAuras не найдены.");
        } else {
            System.out.println("Найдено строк для импорта: " + weakAurasStrings.size());
            System.out.println("Примеры найденных строк:");
            for (int i = 0; i < Math.min(3, weakAurasStrings.size()); i++) {
                System.out.println(weakAurasStrings.get(i));
                System.out.println("-----");
            }
        }
    }

    public static List<String> parseWeakAurasStrings(String filePath) {
        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile("!WA:2![^\\s]+\\s*\\|.*?\\|.*");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            Matcher matcher = pattern.matcher(fileContent.toString());
            while (matcher.find()) {
                String match = matcher.group().trim();
                result.add(match);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return result;
    }
}