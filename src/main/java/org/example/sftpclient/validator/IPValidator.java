package org.example.sftpclient.validator;

public class IPValidator {

    private static final String IP_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public boolean isValidIp(String ip) {
        return ip != null && ip.matches(IP_PATTERN);
    }


}
