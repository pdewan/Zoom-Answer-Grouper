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

public class AllSubfoldersFinder implements ChildrenFileFinder{

	@Override
	public List<File> findChildren(File aParentFolder) {
//		File aParentFolder = ExtensibleMain.getZoomChatDirectory().toFile();
		List<File> retVal = new ArrayList();
        if (!aParentFolder.exists() || !aParentFolder.isDirectory()) {
            System.out.println("The specified path is not a valid directory.");
            return retVal;
        }
       return  Arrays.asList(aParentFolder.listFiles());     
	}
}

