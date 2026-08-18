package org.example.sftpclient.sftp;

import com.jcraft.jsch.*;
import org.example.sftpclient.exception.SFTPClientException;

import java.io.*;
import java.util.Properties;

public class SftpConnector {

    private final String pathToFile;
    private final SftpConfiguration config;
    private Session session;
    private ChannelSftp channel;

    public SftpConnector(String pathToFile, SftpConfiguration config) {
        this.pathToFile = pathToFile;
        this.config = config;
    }


    public void connect() {

        try {
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
        } catch (JSchException e) {
            throw new SFTPClientException("The attempt to establish a connection has failed: " + e.getMessage());
        }
    }


    public String readFile() {
        try (InputStream in = channel.get(pathToFile);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                bytesRead = in.read(buffer);
                if (bytesRead == -1) break;
                out.write(buffer, 0, bytesRead);
            }
            return out.toString("UTF-8");
        } catch (Exception e) {
            throw new SFTPClientException("An exception has occurred during a file read: " + e.getMessage());
        }
    }


    public void writeFile(String content)  {
        try (InputStream in = new ByteArrayInputStream(content.getBytes())) {
            channel.put(in, pathToFile, ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            throw new SFTPClientException("An error has occurred during the saving of a file: " + e.getMessage());
        }
    }


    public void disconnect() {
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
