package org.example.sftpclient.sftp;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.Properties;

public class SftpConnector {

    private final SftpConfiguration config;
    private Session session;
    private ChannelSftp channel;

    public SftpConnector(SftpConfiguration config) {
        this.config = config;
    }

    /**
     * Устанавливает соединение с SFTP-сервером.
     */
    public void connect() throws JSchException {
        JSch jsch = new JSch();

        session = jsch.getSession(config.getUsername(), config.getAddress(), config.getPort());
        session.setPassword(config.getPassword());

        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(properties);

        session.connect(10000); // 10 сек

        Channel rawChannel = session.openChannel("sftp");
        rawChannel.connect(10000);
        channel = (ChannelSftp) rawChannel;
    }

    /**
     * Читает содержимое файла с сервера и возвращает его как строку.
     */
    public String readFile(String remotePath) throws SftpException, IOException {
        try (InputStream in = channel.get(remotePath);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                bytesRead = in.read(buffer);
                if (bytesRead == -1) break;
                out.write(buffer, 0, bytesRead);
            }
            return out.toString("UTF-8");
        }
    }

    /**
     * Записывает строку в файл на сервере (перезаписывает целиком).
     */
    public void writeFile(String remotePath, String content) throws SftpException {
        try (InputStream in = new ByteArrayInputStream(content.getBytes())) {
            channel.put(in, remotePath, ChannelSftp.OVERWRITE);
        } catch (IOException e) {
            throw new SftpException(0, "Ошибка записи файла: " + e.getMessage(), e);
        }
    }

    /**
     * Закрывает соединение.
     */
    public void disconnect() {
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
