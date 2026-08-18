package org.example.sftpclient.json;

import org.example.sftpclient.model.DomainEntry;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class MinimalJsonWriterTest {

    private final MinimalJsonWriter jsonWriter = new MinimalJsonWriter();

    private String readResourceFile(String fileName) throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + fileName);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    @Test
    public void shouldWriteAValidJson() throws IOException {

        List<DomainEntry> entryList = Arrays.asList(
                new DomainEntry("another.domain", "123.123.65.12"),
                new DomainEntry("first.domain", "192.168.0.1"),
                new DomainEntry("second.domain", "192.168.0.2"),
                new DomainEntry("third.domain", "192.168.0.3")
        );

        String expected = readResourceFile("addresses.json");
        String result = jsonWriter.writeJson(entryList);

        String expectedNorm = expected.replace("\r\n", "\n").trim();
        String resultNorm   = result.replace("\r\n", "\n").trim();

        assertEquals(resultNorm, expectedNorm);
    }

}


