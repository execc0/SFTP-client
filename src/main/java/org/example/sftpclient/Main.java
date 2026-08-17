package org.example.sftpclient;

import org.example.sftpclient.sftp.SftpConfiguration;
import org.example.sftpclient.sftp.SftpConnector;


public class Main {
    public static void main(String[] args) {

        SftpConfiguration configuration = new SftpConfiguration(args[0], Integer.parseInt(args[1]), args[2], args[3]);

        SftpConnector connector = new SftpConnector(configuration);

        try {

            connector.connect();

            String file = connector.readFile("upload/addresses.json");

            System.out.println(file);

        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        } finally {
            connector.disconnect();
        }


    }
}