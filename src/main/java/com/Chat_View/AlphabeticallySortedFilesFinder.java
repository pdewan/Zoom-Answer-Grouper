package com.Chat_View;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.Chat_Cluster.ExtensibleMain;

public class AlphabeticallySortedFilesFinder implements ChildrenFileFinder{


	@Override
	public List<File> findChildren(File aParentFolder) {
		List<File> retVal = new ArrayList();
		if (!aParentFolder.exists() || !aParentFolder.isDirectory()) {
            System.out.println("The specified path is not a valid directory.");
            return retVal;
        }

        // Get all the files in the folder
        File[] files = aParentFolder.listFiles(File::isFile);

        if (files == null || files.length == 0) {
            System.out.println("No files found in the directory.");
            return retVal;
        }

        // Sort the files by their name in ascending order
        Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        return Arrays.asList(files);

//        // Get the file with the lowest name
//        File lowestFile = files[0];
//
//        System.out.println("File with the lowest name: " + lowestFile.getName());
	}
    public static void main(String[] args) {
        File aParentFolder = new File("path/to/your/folder"); // Replace with the actual folder path

        if (!aParentFolder.exists() || !aParentFolder.isDirectory()) {
            System.out.println("The specified path is not a valid directory.");
            return;
        }

        // Get all the files in the folder
        File[] files = aParentFolder.listFiles(File::isFile);

        if (files == null || files.length == 0) {
            System.out.println("No files found in the directory.");
            return;
        }

        // Sort the files by their name in ascending order
        Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        // Get the file with the lowest name
        File lowestFile = files[0];

        System.out.println("File with the lowest name: " + lowestFile.getName());
    }
}

