package org.example.sftpclient;

import org.example.sftpclient.json.MinimalJsonParser;
import org.example.sftpclient.model.DomainEntry;
import org.example.sftpclient.service.DomainEntryService;
import org.example.sftpclient.sftp.SftpConfiguration;
import org.example.sftpclient.sftp.SftpConnector;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        SftpConfiguration configuration = new SftpConfiguration(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        SftpConnector connector = new SftpConnector(configuration);
        MinimalJsonParser jsonParser = new MinimalJsonParser();
        DomainEntryService domainEntryService = new DomainEntryService();

        try {

            connector.connect();

            String json = connector.readFile("upload/addresses.json");

            System.out.println(json);

            List<DomainEntry> entryList = jsonParser.parseDomainEntries(json);

            System.out.println(entryList);


        } catch (Exception e) {
            System.err.println("Exception occurred: " + e + e.getMessage());
        } finally {
            connector.disconnect();
        }
    }



}