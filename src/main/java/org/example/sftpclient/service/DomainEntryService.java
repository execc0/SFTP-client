package org.example.sftpclient.service;

import org.example.sftpclient.exception.SFTPClientException;
import org.example.sftpclient.model.DomainEntry;

import java.util.*;

public class DomainEntryService {

    private final Map<String, String> domainToIpMap = new HashMap<>();
    private final Map<String, String> ipToDomainMap = new HashMap<>();
    private final Map<String, String> domainToIpTreeMap = new TreeMap<>();
    private static final String IP_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public void init(List<DomainEntry> entryList) {

        for (DomainEntry entry: entryList) {
            domainToIpMap.put(entry.getDomain(), entry.getIp());
            ipToDomainMap.put(entry.getIp(), entry.getDomain());
            domainToIpTreeMap.put(entry.getDomain(), entry.getIp());
        }

    }

    public List<DomainEntry> getAllEntriesSorted() {
        List<DomainEntry> entryList = new ArrayList<>();
        for (Map.Entry<String, String> entry: domainToIpTreeMap.entrySet()) {
            entryList.add(new DomainEntry(entry.getKey(), entry.getValue()));
        }
        return entryList;

    }

    public void addEntry(DomainEntry entry) {

        if (!entry.getIp().matches(IP_PATTERN)) {
            throw new SFTPClientException("The input ip: " + entry.getIp() + " is not a valid ip address");
        }
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
