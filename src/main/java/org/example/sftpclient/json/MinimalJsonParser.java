package org.example.sftpclient.json;

import org.example.sftpclient.model.DomainEntry;

import java.util.ArrayList;
import java.util.List;

public class MinimalJsonParser {

    private final static String DOMAIN_KEY = "\"domain\":";
    private final static String IP_KEY = "\"ip\":";

    public List<DomainEntry> parseDomainEntries(String json) {

        List<DomainEntry> entryList = new ArrayList<>();
        if (json.isEmpty()) {
            return entryList;
        }

        int domainIndex = 0;
        int ipIndex = 0;

        while ((domainIndex = json.indexOf(DOMAIN_KEY, domainIndex)) != -1) {
            String domain = json.substring(domainIndex + DOMAIN_KEY.length(), json.indexOf(',', domainIndex)).replaceAll("[ \\s,\"]", "");
            domainIndex++;
            if ((ipIndex = json.indexOf(IP_KEY, ipIndex)) == -1) {
                throw new RuntimeException("Error while parsing JSON: no ip found for domain" + domain);
            }
            String ip = json.substring(ipIndex + IP_KEY.length(), json.indexOf('}', ipIndex)).replaceAll("[ \\s,\"]", "");
            ipIndex++;
            entryList.add(new DomainEntry(domain, ip));

        }

        return entryList;

    }


}
