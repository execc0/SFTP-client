package org.example.sftpclient.validator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class IPValidatorTest {

    private final IPValidator ipValidator = new IPValidator();

    @DataProvider(name = "validIPs")
    public Object[][] provideValidIPs() {
        return new Object[][] {
                {"192.168.0.1"},
                {"0.0.0.0"},
                {"255.255.255.255"},
                {"255.254.254.255"},
                {"139.94.64.95"}
        };
    }

    @DataProvider(name = "invalidIPs")
    public Object[][] provideInvalidIPs() {
        return new Object[][]{
                {"256.0.0.0"},
                {".0.0.0.0"},
                {"0..0.0.0"},
                {"in.va.li.d"},
                {""},
                {null}
        };
    }

    @Test(dataProvider = "validIPs")
    public void shouldReturnTrueForValidIPs(String ip) {
        assertTrue(ipValidator.isValidIp(ip));
    }

    @Test(dataProvider = "invalidIPs")
    public void shouldReturnFalseForInvalidIPs(String ip) {
        assertFalse(ipValidator.isValidIp(ip));
    }

}
