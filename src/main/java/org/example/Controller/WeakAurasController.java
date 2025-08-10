package org.example.Controller;

import org.example.model.WeakAuraEntry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/")

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

     List<WeakAuraEntry> parseWeakAurasFile(MultipartFile file) throws IOException {
        List<WeakAuraEntry> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String currentName = "Unnamed WeakAura";
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("|") && line.endsWith("|") && line.length() > 2) {
                    currentName = line.substring(1, line.length() - 1).trim();
                }
                else if (line.startsWith("|") && line.contains("|") && line.contains("!WA:2!")) {
                    String[] parts = line.split("\\|", 3);
                    if (parts.length >= 2) {
                        currentName = parts[1].trim();
                        String importString = parts[2].trim();
                        if (importString.startsWith("!WA:2!")) {
                            entries.add(new WeakAuraEntry(currentName, importString));
                            currentName = "Unnamed WeakAura";
                        }
                    }
                }
                else if (line.startsWith("!WA:2!")) {
                    entries.add(new WeakAuraEntry(currentName, line));
                    currentName = "Unnamed WeakAura";
                }
            }
        }

        return entries;
    }
}