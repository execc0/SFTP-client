package org.example.sftpclient.model;

public class DomainEntry {

    private final String domain;
    private final String ip;

    public DomainEntry(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getIp() {
        return this.ip;
    }

    @Override
    public String toString() {
        return "domain: " + "\"" + getDomain() + "\"" + " ip: " + "\""+ getIp() + "\"";
    }

}
