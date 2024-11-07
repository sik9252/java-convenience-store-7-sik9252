package store.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static store.exception.ErrorMessage.FAILED_READ_FILE;

public class FileUtils {
    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(FileUtils.class.getClassLoader().getResourceAsStream(filename))))) {
            lines = br.lines().toList();
        } catch (IOException e) {
            System.out.println(FAILED_READ_FILE.getMessage());
        }

        return lines;
    }
}
