package org.example.sftpclient;

import org.example.sftpclient.json.MinimalJsonParser;
import org.example.sftpclient.json.MinimalJsonWriter;
import org.example.sftpclient.menu.ConsoleMenu;
import org.example.sftpclient.model.DomainEntry;
import org.example.sftpclient.service.DomainEntryService;
import org.example.sftpclient.sftp.SftpConfiguration;
import org.example.sftpclient.sftp.SftpConnector;
import org.example.sftpclient.validator.IPValidator;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        final String filePath = "upload/addresses.json";

        if (args.length != 4) {
            System.out.println("A wrong number of arguments was passed. The expected number is 4");
        }

        SftpConfiguration configuration = new SftpConfiguration(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        SftpConnector connector = new SftpConnector(filePath, configuration);
        MinimalJsonParser jsonParser = new MinimalJsonParser();
        MinimalJsonWriter jsonWriter = new MinimalJsonWriter();
        DomainEntryService domainEntryService = new DomainEntryService(new IPValidator());

        try (Scanner sc = new Scanner(System.in)) {

            connector.connect();
            String json = connector.readFile();
            List<DomainEntry> entryList = jsonParser.parseDomainEntries(json);
            domainEntryService.init(entryList);
            System.out.println("Successfully connected to the server and extracted the file");

            ConsoleMenu menu = new ConsoleMenu(connector, jsonWriter, sc, domainEntryService);

            menu.run();

        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        } finally {
            connector.disconnect();
        }

    }

}