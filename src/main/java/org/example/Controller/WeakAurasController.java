package org.example.Controller;

import org.example.model.WeakAuraEntry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class WeakAurasController {

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
            return "upload";
        }

        try {
            List<WeakAuraEntry> entries = parseWeakAurasFile(file);
            model.addAttribute("entries", entries);
            return "results";
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            return "upload";
        }
    }

    private List<WeakAuraEntry> parseWeakAurasFile(MultipartFile file) throws IOException {
        List<WeakAuraEntry> entries = new ArrayList<>();
        Pattern pattern = Pattern.compile("!WA:2![^\\s]+\\s*\\|(.*?)\\|(.*)");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            Matcher matcher = pattern.matcher(fileContent.toString());
            while (matcher.find()) {
                String name = matcher.group(1).trim();
                String importString = matcher.group(0).trim(); // Полная строка импорта
                entries.add(new WeakAuraEntry(name, importString));
            }
        }

        return entries;
    }
}