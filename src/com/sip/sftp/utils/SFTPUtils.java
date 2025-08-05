package com.sip.sftp.utils;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtils {

	String sftpHost = "192.168.3.109";
	int sftpPort = 22;
    String sftpUser = "rpauser";
    String privateKeyPath = "C:\\SFTP KEY FILE\\UTradeX_ppk\\puttykeygen_UTradeX_4096.ppk";

    private Session session;
    private ChannelSftp channelSftp;

    public ChannelSftp connect() {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);

            session = jsch.getSession(sftpUser, sftpHost, sftpPort);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("Session connected successfully.");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;

            System.out.println("SFTP Channel connected successfully.");
            return channelSftp;

        } catch (Exception e) {
            e.printStackTrace();
            disconnect(); 
            return null;
        }
    }

    public void disconnect() {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.exit();
            System.out.println("SFTP channel closed.");
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
            System.out.println("SSH session disconnected.");
        }
    }
    
    public boolean isDirectoryExist(ChannelSftp channelSftp, String path) {
    	
    	try {
			channelSftp.cd(path);
			return true;
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			return false;
		}
    	
    }
    
    
    
}
