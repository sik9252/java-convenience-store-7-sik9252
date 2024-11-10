package store.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilsTest {

    @Test
    @DisplayName("파일을 읽어 올바른 라인 목록을 반환한다")
    void readFile_ShouldReturnLines_WhenFileExists() {
        String filename = "products.md";

        List<String> lines = FileUtils.readFile(filename);

        assertThat(lines).isNotEmpty();
        assertThat(lines.get(0)).isEqualTo("name,price,quantity,promotion");
        assertThat(lines.get(1)).isEqualTo("콜라,1000,10,탄산2+1");
    }
}
