package giezz.speech2texttgbot.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class FileUtils {
    public byte[] readFileBytes(final Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException thrown) {
            throw new UncheckedIOException("Ошибка при чтении файла: " + path, thrown);
        }
    }
}
