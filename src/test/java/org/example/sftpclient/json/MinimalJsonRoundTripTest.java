package org.example.sftpclient.json;

import org.example.sftpclient.model.DomainEntry;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class MinimalJsonRoundTripTest {

    private final MinimalJsonParser minimalJsonParser = new MinimalJsonParser();
    private final MinimalJsonWriter minimalJsonWriter = new MinimalJsonWriter();

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
    public void shouldDoTheRoundTrip() throws IOException {

        String actualJson = readResourceFile("addresses.json");
        List<DomainEntry> entryList = minimalJsonParser.parseDomainEntries(actualJson);
        String resultJson = minimalJsonWriter.writeJson(entryList);

        String expectedNorm = actualJson.replace("\r\n", "\n").trim();
        String resultNorm   = resultJson.replace("\r\n", "\n").trim();


        assertEquals(expectedNorm, resultNorm);

    }

}
