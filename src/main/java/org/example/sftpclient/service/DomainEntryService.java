package org.example.sftpclient.service;

import org.example.sftpclient.exception.SFTPClientException;
import org.example.sftpclient.model.DomainEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DomainEntryService {

    private final Map<String, String> domainToIpMap = new HashMap<>();
    private final Map<String, String> ipToDomainMap = new HashMap<>();
    private final Map<String, String> domainToIpTreeMap = new TreeMap<>();

    public void init(List<DomainEntry> entryList) {

        for (DomainEntry entry: entryList) {
            domainToIpMap.put(entry.getDomain(), entry.getIp());
            ipToDomainMap.put(entry.getIp(), entry.getDomain());
            domainToIpTreeMap.put(entry.getDomain(), entry.getIp());
        }

    }

    public Map<String, String> getAllEntriesSorted() {
        return this.domainToIpTreeMap;
    }

    public void addEntry(DomainEntry entry) {

        if (domainToIpMap.containsKey(entry.getDomain())) {
            throw new SFTPClientException("Can not add an already existing domain to the file");
        }
        if (ipToDomainMap.containsKey(entry.getIp())) {
            throw new SFTPClientException("Can not add an already existing IP to the file");
        }
        domainToIpMap.put(entry.getDomain(), entry.getIp());
        ipToDomainMap.put(entry.getIp(), entry.getDomain());
        domainToIpTreeMap.put(entry.getDomain(), entry.getIp());

    }

    public String findByDomain(String domain) {
        if (!domainToIpMap.containsKey(domain)) {
            throw new SFTPClientException("Can not find the ip with the specified domain: " + domain);
        }
        return domainToIpMap.get(domain);
    }

    public String findByIp(String ip) {
        if (!ipToDomainMap.containsKey(ip)) {
            throw new SFTPClientException("Can not find the domain with the specified ip: "+ ip);
        }
        return ipToDomainMap.get(ip);
    }

    public void deleteByIp(String ip) {

        String domain = findByIp(ip);
        ipToDomainMap.remove(ip);
        domainToIpMap.remove(domain);
        domainToIpTreeMap.remove(domain);

    }

    public void deleteByDomain(String domain) {

        String ip = findByDomain(domain);
        domainToIpMap.remove(domain);
        ipToDomainMap.remove(ip);
        domainToIpTreeMap.remove(domain);

    }


}
