package com.sip.sftp.main;

import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import com.sip.sftp.utils.SFTPUtils;

public class Reader {
	
	
	public static void main(String[] args) {
		
		SFTPUtils sftpUtils = new SFTPUtils();
		String path = "/UOBK_BounceMail_ContentRepo/HYMTest";
		ChannelSftp channelSftp = sftpUtils.connect();
		
		boolean isDirExit = sftpUtils.isDirectoryExist(channelSftp, path);

		if(isDirExit) {
			System.out.println("Given directory ( " + path + " ) is exist.");
			try {
				Vector<LsEntry> files = channelSftp.ls(path);
				if(files.size() > 0) {
					System.out.println("Total files: "+ files.size());
					for(LsEntry file: files) {
						System.out.println("file name: " + file.getFilename());
					}
				} 
				else {
					System.out.println("No file exist");
				}
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				sftpUtils.disconnect();
			}
		}
		else {
			System.out.println("Given directory ( " + path + " ) is not exist.");
		}
	}

}
