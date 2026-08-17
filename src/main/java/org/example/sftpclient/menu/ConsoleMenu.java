package org.example.sftpclient.menu;

import org.example.sftpclient.exception.SFTPClientException;
import org.example.sftpclient.model.DomainEntry;
import org.example.sftpclient.service.DomainEntryService;

import java.util.Scanner;

public class ConsoleMenu {

    private final DomainEntryService domainEntryService;
    private final Scanner scanner;

    public ConsoleMenu(Scanner scanner, DomainEntryService domainEntryService) {
        this.domainEntryService = domainEntryService;
        this.scanner = scanner;
    }

    public void run() {

        printMenu();

        boolean running = true;
        while (running) {

            String input = scanner.nextLine();
            try {
                running = handleCommand(input);
                if (running) System.out.println("Choose a new operation: ");
            } catch (SFTPClientException e) {
                System.out.println(e.getMessage() + "\nChoose a new operation: " + "\n");
            }

        }
        System.out.println("Exiting the application...");

    }

    private void printMenu() {

        String message = "***************************************\n" +
                "Choose an operation: \n" +
                "1 - Get a list of domain-ip pairs \n" +
                "2 - Find a domain by ip \n" +
                "3 - Find an ip by domain \n" +
                "4 - Add a domain-ip pair \n" +
                "5 - Delete a domain-ip pair by ip \n" +
                "6 - Delete a domain-ip pair by domain \n" +
                "0 - Print this message \n" +
                "Any other key to exit the application \n" +
                "***************************************\n";
        System.out.println(message);

    }

    private boolean handleCommand(String input) {
        switch (input) {
            case "0": {
                printMenu();
                return true;
            }
            case "1": {
                System.out.println(domainEntryService.getAllEntriesSorted() + "\n");
                return true;
            }
            case "2": {
                System.out.println("Finding a domain-ip pair. Input an ip: ");
                String ip = scanner.nextLine();
                System.out.println(domainEntryService.findByIp(ip) + "\n");
                return true;
            }
            case "3": {
                System.out.println("Finding a domain-ip pair. Input a domain: ");
                String domain = scanner.nextLine();
                System.out.println(domainEntryService.findByDomain(domain) + "\n");
                return true;
            }
            case "4": {
                System.out.println("Adding a domain-ip pair. Input a domain-ip pair in the format: <domain> <ip>");
                String[] entry = scanner.nextLine().split(" ");
                if (entry.length != 2) {
                    throw new SFTPClientException("Invalid amount of arguments passed. The expected amount is 2");
                }
                DomainEntry domainEntry = new DomainEntry(entry[0], entry[1]);
                domainEntryService.addEntry(domainEntry);
                System.out.println("Successfully added a domain-ip pair: " + domainEntry + "\n");
                return true;
            }
            case "5": {
                System.out.println("Deleting an domain-ip pair. Input an ip: ");
                String ip = scanner.nextLine();
                domainEntryService.deleteByIp(ip);
                System.out.println("Successfully deleted an domain-ip pair with ip: " + ip + "\n");
                return true;
            }
            case "6": {
                System.out.println("Deleting an domain-ip pair. Input a domain");
                String domain = scanner.nextLine();
                domainEntryService.deleteByDomain(domain);
                System.out.println("Successfully deleted an domain-ip pair with domain: " + domain + "\n");
                return true;
            }
            default: {
                return false;
            }
        }

    }



}
