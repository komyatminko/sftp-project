package com.sip.sftp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

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
			channelSftp = (ChannelSftp) channel;
			channel.connect();

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
			return false;
		}

	}

	public void copyFile(ChannelSftp channelSftp, String sourcePath, String copyTargetPath) throws SftpException {

		//download file to memory
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		channelSftp.get(sourcePath, outputStream);
		
		//copy file to target folder on the sftp server
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		channelSftp.put(inputStream, copyTargetPath);
		
		System.out.println("File copied from " + sourcePath + " to " + copyTargetPath + "\n");
		
	}

	public void moveFile(ChannelSftp channelSftp, String sourcePath, String moveTargetPath) throws SftpException {

		copyFile(channelSftp, sourcePath, moveTargetPath);
		
		channelSftp.rm(sourcePath);
		
		System.out.println("File moved from " + sourcePath + " to " + moveTargetPath + "\n");
		
	}
	
	public void createNestedDirectory(ChannelSftp channelSftp, String directory) {
		
		String [] folders = directory.split("/");
		String currentPath = "";
		
		for(String folder : folders) {
			
			currentPath += "/" + folder;
			try {
				channelSftp.cd(currentPath);
			} catch (SftpException e) {
				try {
					channelSftp.mkdir(currentPath);
					System.out.println(currentPath + " is created");
				} catch (SftpException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public void deleteNestedDirectory(ChannelSftp channelSftp, String path) throws SftpException {
        System.out.println("Listing contents of folder: " + path);
        Vector<LsEntry> entries = channelSftp.ls(path);

        for (LsEntry entry : entries) {
            String fileName = entry.getFilename();

            // Skip current and parent dir references
            if (".".equals(fileName) || "..".equals(fileName)) {
                continue;
            }

            String fullPath = path + "/" + fileName;

            if (entry.getAttrs().isDir()) {
                System.out.println("Found directory: " + fullPath + " — descending into it");
                // Recursive call to delete subfolder
                deleteNestedDirectory(channelSftp, fullPath);
            } else {
                System.out.println("Deleting file: " + fullPath);
                channelSftp.rm(fullPath);
            }
        }

        // After deleting contents, delete the folder itself
        System.out.println("Deleting folder: " + path);
        channelSftp.rmdir(path);
    }


}
