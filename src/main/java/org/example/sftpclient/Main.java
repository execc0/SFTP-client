package org.example.sftpclient;

import org.example.sftpclient.exception.SFTPClientException;
import org.example.sftpclient.json.MinimalJsonParser;
import org.example.sftpclient.menu.ConsoleMenu;
import org.example.sftpclient.model.DomainEntry;
import org.example.sftpclient.service.DomainEntryService;
import org.example.sftpclient.sftp.SftpConfiguration;
import org.example.sftpclient.sftp.SftpConnector;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        SftpConfiguration configuration = new SftpConfiguration(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        SftpConnector connector = new SftpConnector(configuration);
        MinimalJsonParser jsonParser = new MinimalJsonParser();
        DomainEntryService domainEntryService = new DomainEntryService();

        try (Scanner sc = new Scanner(System.in)) {

            connector.connect();
            String json = connector.readFile("upload/addresses.json");
            List<DomainEntry> entryList = jsonParser.parseDomainEntries(json);
            domainEntryService.init(entryList);
            System.out.println("Successfully connected to the server and extracted the file");

            ConsoleMenu menu = new ConsoleMenu(sc, domainEntryService);

            menu.run();

        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        } finally {
            connector.disconnect();
        }

    }

}