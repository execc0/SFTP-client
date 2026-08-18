package org.example.sftpclient.sftp;

public class SftpConfiguration {

    private final String username;
    private final String password;
    private final int port;
    private final String address;

    public SftpConfiguration(String address, int port, String username, String password) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.address = address;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPort() {
        return this.port;

    }

    public String getAddress() {
        return this.address;
    }

}
