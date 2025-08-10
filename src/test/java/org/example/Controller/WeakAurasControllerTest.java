package org.example.Controller;


import org.example.model.WeakAuraEntry;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WeakAurasControllerTest {

    private final WeakAurasController controller = new WeakAurasController();

    @Test
    void testParseWeakAurasFile_StandardFormat() throws IOException {
        // Подготовка тестовых данных
        String content = "| Healing Rain |\n!WA:2!test1\n| Rune Swapper |\n!WA:2!test2";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());

        // Вызов тестируемого метода
        List<WeakAuraEntry> result = controller.parseWeakAurasFile(file);

        // Проверки
        assertEquals(2, result.size());

        assertEquals("Healing Rain", result.get(0).getName());
        assertEquals("!WA:2!test1", result.get(0).getImportString());

        assertEquals("Rune Swapper", result.get(1).getName());
        assertEquals("!WA:2!test2", result.get(1).getImportString());
    }

    @Test
    void testParseWeakAurasFile_CombinedFormat() throws IOException {
        String content = "| Healing Rain | !WA:2!test1\n| Rune Swapper | !WA:2!test2";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());

        List<WeakAuraEntry> result = controller.parseWeakAurasFile(file);

        assertEquals(2, result.size());
        assertEquals("Healing Rain", result.get(0).getName());
        assertEquals("Rune Swapper", result.get(1).getName());
    }

    @Test
    void testParseWeakAurasFile_UnnamedAuras() throws IOException {
        String content = "!WA:2!test1\n| Some Name |\n!WA:2!test2\n!WA:2!test3";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes());

        List<WeakAuraEntry> result = controller.parseWeakAurasFile(file);

        assertEquals(3, result.size());
        assertEquals("Unnamed WeakAura", result.get(0).getName());
        assertEquals("Some Name", result.get(1).getName());
        assertEquals("Unnamed WeakAura", result.get(2).getName());
    }

    @Test
    void testParseWeakAurasFile_EmptyFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "empty.txt", "text/plain", "".getBytes());
        List<WeakAuraEntry> result = controller.parseWeakAurasFile(file);
        assertTrue(result.isEmpty());
    }
}