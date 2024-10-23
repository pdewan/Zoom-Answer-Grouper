package com.Chat_Cluster;

import java.io.File;
import java.io.IOException;

public class Testing {
public static void main (String[] args) {
	File aFile1 = new File("C:\\Users\\Prasun\\Desktop\\OriginalFile.txt");

	File aFile2 = new File("C:\\Users\\Prasun\\Desktop\\MergedFile.txt");
	try {
		java.awt.Desktop.getDesktop().edit(aFile1);
		java.awt.Desktop.getDesktop().edit(aFile2);
//		Runtime.getRuntime().exec("edit " + aFile1.getCanonicalPath());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
