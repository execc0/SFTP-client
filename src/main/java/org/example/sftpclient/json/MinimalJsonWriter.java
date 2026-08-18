package org.example.sftpclient.json;

import org.example.sftpclient.model.DomainEntry;

import java.util.List;

public class MinimalJsonWriter {

    private static final String INDENT = "  "; // 2 пробела на отступ

    public String writeJson(List<DomainEntry> entryList) {

        StringBuilder sb = new StringBuilder();

        sb.append("{\n").append(INDENT).append("\"addresses\": [\n");

        for (DomainEntry entry: entryList) {

            sb.append(INDENT).append(INDENT).append("{\n");
            sb.append(INDENT).append(INDENT).append(INDENT).append("\"domain\": ").append("\"").append(entry.getDomain()).append("\",\n");
            sb.append(INDENT).append(INDENT).append(INDENT).append("\"ip\": ").append("\"").append(entry.getIp()).append("\"\n");
            sb.append(INDENT).append(INDENT).append("},\n");

        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(INDENT).append("]\n").append("}");

        return sb.toString();

    }

}
