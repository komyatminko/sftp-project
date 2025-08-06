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
//		String TargetPath = "/UOBK_BounceMail_ContentRepo/Failed/";
		ChannelSftp channelSftp = sftpUtils.connect();
		String TargetPath = "/UOBK_BounceMail_ContentRepo/MMK_Test/";
		
		//create new folder if it is not exit
		try {
			channelSftp.cd(TargetPath);
		} catch (SftpException e) {
			try {
				channelSftp.mkdir(TargetPath);
				System.out.println("Directory created: " + TargetPath);
			} catch (SftpException ex) {
				ex.printStackTrace();
			}
		}

		boolean isDirExit = sftpUtils.isDirectoryExist(channelSftp, sourcePath);

		//moving and copying file from one folder to another folder on the server
//		if (isDirExit) {
//			System.out.println("Source file directory ( " + sourcePath + " ) is exist. \n");
//			try {
//				Vector<LsEntry> files = channelSftp.ls(sourcePath);
//				if (files.size() > 0) {
//					long currentTimeInMilis = System.currentTimeMillis();
//					System.out.println("Total files: " + files.size());
//					for (LsEntry file : files) {
//						System.out.println("file name: " + file.getFilename());
//						if (file.getFilename().toLowerCase().endsWith(".xml")) {
//
//							System.out.println("xml file: " + sourcePath + file.getFilename());
//							sftpUtils.moveFile(channelSftp, sourcePath + file.getFilename(),
//									TargetPath + currentTimeInMilis + "_" + file.getFilename());
//						}
//						if (file.getFilename().toLowerCase().endsWith(".pdf")) {
//
//							System.out.println("pdf file: " + sourcePath + file.getFilename());
//							sftpUtils.copyFile(channelSftp, sourcePath + file.getFilename(),
//									TargetPath + currentTimeInMilis + "_" + file.getFilename());
//						}
//					}
//				} else {
//					System.out.println("No file exist");
//				}
//			} catch (SftpException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
//				sftpUtils.disconnect();
//			}
//		} else {
//			System.out.println("Given directory ( " + sourcePath + " ) is not exist.");
//		}
		
		//creating nested folder e.g a/b/c
		String pathToCreate = "/UOBK_BounceMail_ContentRepo/MMK_Test/Java/SpringBoot";
		sftpUtils.createNestedDirectory(channelSftp, pathToCreate);
		sftpUtils.disconnect();
	}

}
