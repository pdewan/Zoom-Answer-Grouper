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

public class LatestSubfolderFinder implements ChildrenFileFinder{
//    public static void main(String[] args) {
//    	
////        File zoomChatsFolder = new File("path/to/zoomChatsFolder"); // Replace with the actual path
//        File aParentFolder = ExtensibleMain.getZoomChatDirectory().toFile();
//
//        if (!aParentFolder.exists() || !aParentFolder.isDirectory()) {
//            System.out.println("The specified path is not a valid directory.");
//            return;
//        }
//
//        File latestSubfolder = Arrays.stream(aParentFolder.listFiles(File::isDirectory))
//                .max(Comparator.comparingLong(LatestSubfolderFinder::getCreationTime))
//                .orElse(null);
//
//        if (latestSubfolder != null) {
//            System.out.println("Latest subfolder: " + latestSubfolder.getName());
//        } else {
//            System.out.println("No subfolders found.");
//        }
//    }

    private static long getCreationTime(File file) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class);
            return attrs.creationTime().toMillis();
        } catch (IOException e) {
            e.printStackTrace();
            return Long.MIN_VALUE;
        }
    }

	@Override
	public List<File> findChildren(File aParentFolder) {
//		File aParentFolder = ExtensibleMain.getZoomChatDirectory().toFile();
		List<File> retVal = new ArrayList();
        if (!aParentFolder.exists() || !aParentFolder.isDirectory()) {
            System.out.println("The specified path is not a valid directory.");
            return retVal;
        }

        File latestSubfolder = Arrays.stream(aParentFolder.listFiles(File::isDirectory))
                .max(Comparator.comparingLong(LatestSubfolderFinder::getCreationTime))
                .orElse(null);

        if (latestSubfolder != null) {
            System.out.println("Latest subfolder: " + latestSubfolder.getName());
        } else {
            System.out.println("No subfolders found.");
        }
        retVal.add(latestSubfolder);
//        return latestSubfolder;
        return retVal;
	}
}

