package org.example.sftpclient.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntry that = (DomainEntry) o;
        return Objects.equals(domain, that.domain) && Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, ip);
    }
}
