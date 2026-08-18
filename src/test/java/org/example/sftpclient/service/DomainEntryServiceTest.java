package org.example.sftpclient.service;

import org.example.sftpclient.exception.SFTPClientException;
import org.example.sftpclient.model.DomainEntry;
import org.example.sftpclient.validator.IPValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class DomainEntryServiceTest {

    private DomainEntryService service;
    private final IPValidator ipValidator = new IPValidator();

    @BeforeMethod
    public void setUp() {
        service = new DomainEntryService(ipValidator);
    }

    @DataProvider(name = "validEntries")
    public Object[][] provideValidEntries() {
        return new Object[][] {
                {new DomainEntry("fourth.domain", "0.0.0.0")},
                {new DomainEntry("first.domain", "192.168.0.1")},
                {new DomainEntry("second.domain", "192.168.0.2")},
                {new DomainEntry("third.domain", "10.0.0.1")},
                {new DomainEntry("fifth.domain", "255.255.255.255")}
        };
    }

    @DataProvider(name = "invalidIPEntries")
    public Object[][] provideEntriesWithInvalidIPs() {
        return new Object[][] {
                {new DomainEntry("first.domain", "01.1.1.001")},
                {new DomainEntry("second.domain", "256.255.255.255")},
                {new DomainEntry("third.domain", null)},
                {new DomainEntry("fourth.domain", "0...0..0")},
                {new DomainEntry("fifth.domain", "")}
        };
    }

    @DataProvider(name = "invalidDomainEntries")
    public Object[][] provideEntriesWithInvalidDomains() {
        return new Object[][] {
                {new DomainEntry("", "192.168.0.1")},
                {new DomainEntry(null, "192.168.0.2")},
        };
    }

    @DataProvider(name = "validDomains")
    public Object[][] provideValidDomains() {
        return new Object[][] {
                {"first.domain"},
                {"second.domain"},
                {"third.notadomain"}
        };
    }

    @DataProvider(name = "validIPs")
    public Object[][] provideValidIPs() {
        return new Object[][] {
                {"192.168.0.1"},
                {"0.0.0.0"},
                {"255.255.255.255"}
        };
    }

    @DataProvider(name = "unsortedWithExpected")
    public Object[][] provideUnsortedWithExpected() {
        return new Object[][] {
                {
                        Arrays.asList(
                                new DomainEntry("third.domain", "192.168.0.3"),
                                new DomainEntry("first.domain", "192.168.0.1"),
                                new DomainEntry("second.domain", "192.168.0.2")
                        ),
                        Arrays.asList(
                                new DomainEntry("first.domain", "192.168.0.1"),
                                new DomainEntry("second.domain", "192.168.0.2"),
                                new DomainEntry("third.domain", "192.168.0.3")
                        )
                }
        };
    }

    @Test(dataProvider = "validEntries")
    public void shouldAddAValidEntry(DomainEntry entry) {

        service.addEntry(entry);

        assertEquals(service.findByDomain(entry.getDomain()), entry.getIp());
        assertEquals(service.findByIp(entry.getIp()), entry.getDomain());
    }

    @Test(dataProvider = "invalidIPEntries")
    public void shouldThrowWhenIPIsInvalid(DomainEntry entry) {
        assertThrows(SFTPClientException.class, () -> service.addEntry(entry));
    }

    @Test(dataProvider = "invalidDomainEntries")
    public void shouldThrowWhenDomainIsInvalid(DomainEntry entry) {
        assertThrows(SFTPClientException.class, () -> service.addEntry(entry));
    }

    @Test(dataProvider = "validDomains")
    public void shouldThrowWhenAddingSameDomain(String domain) {
        service.addEntry(new DomainEntry(domain, "192.168.0.1"));
        assertThrows(SFTPClientException.class, () -> service.addEntry(new DomainEntry(domain, "192.168.255.255")));
    }

    @Test(dataProvider = "validIPs")
    public void shouldThrowWhenAddingSameIP(String ip) {
        service.addEntry(new DomainEntry("domain.name", ip));
        assertThrows(SFTPClientException.class, () -> service.addEntry(new DomainEntry("another.domain", ip)));
    }

    @Test(dataProvider = "validEntries")
    public void shouldDeleteByDomainEntry(DomainEntry entry) {
        service.addEntry(entry);
        assertEquals(service.findByDomain(entry.getDomain()), entry.getIp());
        assertEquals(service.findByIp(entry.getIp()), entry.getDomain());

        service.deleteByDomain(entry.getDomain());
        assertThrows(SFTPClientException.class, () -> service.findByDomain(entry.getDomain()));
        assertThrows(SFTPClientException.class, () -> service.findByIp(entry.getIp()));
    }

    @Test(dataProvider = "validEntries")
    public void shouldDeleteByIpEntry(DomainEntry entry) {
        service.addEntry(entry);
        assertEquals(service.findByDomain(entry.getDomain()), entry.getIp());
        assertEquals(service.findByIp(entry.getIp()), entry.getDomain());

        service.deleteByIp(entry.getIp());
        assertThrows(SFTPClientException.class, () -> service.findByDomain(entry.getDomain()));
        assertThrows(SFTPClientException.class, () -> service.findByIp(entry.getIp()));
    }

    @Test(dataProvider = "unsortedWithExpected")
    public void shouldReturnSortedEntries(List<DomainEntry> input, List<DomainEntry> expected) {
        for (DomainEntry entry : input) {
            service.addEntry(entry);
        }

        assertEquals(service.getAllEntriesSorted(), expected);
    }
}
