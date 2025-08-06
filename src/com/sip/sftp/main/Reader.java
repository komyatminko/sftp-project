package com.sip.sftp.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

		

		//moving and copying file from one folder to another folder on the server
//		fileMoveAndCopy(sftpUtils, sourcePath, channelSftp, TargetPath);
		
		//creating nested folder e.g a/b/c
//		createNestedDirectory(sftpUtils, channelSftp);
		
		//file upload from local system onto sftp server using Jsch
		uploadFileFromSystemToServer(sftpUtils, channelSftp, TargetPath);
		
		//file download from server to file local system.
//		downloadFromServerToFileSys(sftpUtils, channelSftp, TargetPath);
	}

	private static void uploadFileFromSystemToServer(SFTPUtils sftpUtils, ChannelSftp channelSftp, String TargetPath) {
		String localFilePath = "C:\\Users\\myatminko\\workspace\\PdfFiles Output\\tailwind-css-starter-kit_compress.pdf";
		boolean isDirExit = sftpUtils.isDirectoryExist(channelSftp, TargetPath);
		if (isDirExit) {
			System.out.println("Target file path is exist");
			// upload file
			try {
				channelSftp.cd(TargetPath);
				channelSftp.put(new FileInputStream(localFilePath), "tailwind-css-starter-kit_compress.pdf");
				System.out.println("Uploaded file successfully.");
			} catch (FileNotFoundException | SftpException e) {
				e.printStackTrace();
			} finally {
				sftpUtils.disconnect();
			}
		}
	}
	
	private static void downloadFromServerToFileSys(SFTPUtils sftpUtils, ChannelSftp channelSftp, String sourcePath) {
		String localFilePath = "C:\\Users\\myatminko\\workspace\\PdfFiles Output\\";
		boolean isDirExit = sftpUtils.isDirectoryExist(channelSftp, sourcePath);
		if(isDirExit) {
			
			try {
				Vector<LsEntry> files = channelSftp.ls(sourcePath);
				if (files.size() > 0) {
					for(LsEntry file : files) {
						channelSftp.get(sourcePath+ file.getFilename(), new FileOutputStream(localFilePath+ file.getFilename()));
						System.out.println("File downloaded successfully.");
					}
				}
			} catch (FileNotFoundException | SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				sftpUtils.disconnect();
			}
		}
	}

	private static void createNestedDirectory(SFTPUtils sftpUtils, ChannelSftp channelSftp) {
		String pathToCreate = "/UOBK_BounceMail_ContentRepo/MMK_Test/Java/SpringBoot";
		sftpUtils.createNestedDirectory(channelSftp, pathToCreate);
		sftpUtils.disconnect();
	}

	private static void fileMoveAndCopy(SFTPUtils sftpUtils, String sourcePath, ChannelSftp channelSftp, String TargetPath) {
		boolean isDirExit = sftpUtils.isDirectoryExist(channelSftp, sourcePath);
		if (isDirExit) {
			System.out.println("Source file directory ( " + sourcePath + " ) is exist. \n");
			try {
				Vector<LsEntry> files = channelSftp.ls(sourcePath);
				if (files.size() > 0) {
					long currentTimeInMilis = System.currentTimeMillis();
					System.out.println("Total files: " + files.size());
					for (LsEntry file : files) {
						System.out.println("file name: " + file.getFilename());
						if (file.getFilename().toLowerCase().endsWith(".xml")) {

							System.out.println("xml file: " + sourcePath + file.getFilename());
							sftpUtils.moveFile(channelSftp, sourcePath + file.getFilename(),
									TargetPath + currentTimeInMilis + "_" + file.getFilename());
						}
						if (file.getFilename().toLowerCase().endsWith(".pdf")) {

							System.out.println("pdf file: " + sourcePath + file.getFilename());
							sftpUtils.copyFile(channelSftp, sourcePath + file.getFilename(),
									TargetPath + currentTimeInMilis + "_" + file.getFilename());
						}
					}
				} else {
					System.out.println("No file exist");
				}
			} catch (SftpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				sftpUtils.disconnect();
			}
		} else {
			System.out.println("Given directory ( " + sourcePath + " ) is not exist.");
		}
	}

}
