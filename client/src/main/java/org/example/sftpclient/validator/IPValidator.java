package org.example.sftpclient.validator;

public class IPValidator {

    private static final String IP_STRUCTURE_PATTERN = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";

    public boolean isValidIp(String ip) {
        if (ip == null || !ip.matches(IP_STRUCTURE_PATTERN)) {
            return false;
        }
        for (String octet : ip.split("\\.")) {
            if (octet.length() > 1 && octet.startsWith("0")) {
                return false; // отсекаем ведущие нули
            }
            int value = Integer.parseInt(octet);
            if (value < 0 || value > 255) {
                return false;
            }
        }
        return true;
    }


}
