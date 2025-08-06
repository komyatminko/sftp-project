package com.sip.sftp.main;

import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import com.sip.sftp.utils.SFTPUtils;

public class Reader {
	
	
	public static void main(String[] args) {
		
		SFTPUtils sftpUtils = new SFTPUtils();
//		String path = "/UOBK_BounceMail_ContentRepo/DoneClone";
		String sourcePath = "/UOBK_BounceMail_ContentRepo/DoneClone/";
		String TargetPath = "/UOBK_BounceMail_ContentRepo/Failed/";
		ChannelSftp channelSftp = sftpUtils.connect();
		
		boolean isDirExit = sftpUtils.isDirectoryExist(channelSftp, sourcePath);

		if(isDirExit) {
			System.out.println("Given directory ( " + sourcePath + " ) is exist.");
			try {
				Vector<LsEntry> files = channelSftp.ls(sourcePath);
				if(files.size() > 0) {
					System.out.println("Total files: "+ files.size());
					for(LsEntry file: files) {
						System.out.println("file name: " + file.getFilename());
						if(file.getFilename().toLowerCase().endsWith(".xml")) {
							
							System.out.println("xml file: " + sourcePath + file.getFilename());
							sftpUtils.moveFile(channelSftp, sourcePath + file.getFilename(), TargetPath + file.getFilename());
						}
						if(file.getFilename().toLowerCase().endsWith(".pdf")) {
							
							System.out.println("pdf file: " + sourcePath + file.getFilename());
							sftpUtils.copyFile(channelSftp, sourcePath + file.getFilename(), TargetPath + file.getFilename());
						}
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
			System.out.println("Given directory ( " + sourcePath + " ) is not exist.");
		}
	}

}
