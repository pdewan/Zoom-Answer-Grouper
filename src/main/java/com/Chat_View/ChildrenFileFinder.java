package com.Chat_View;

import java.io.File;
import java.util.List;

public interface ChildrenFileFinder {
	List<File> findChildren(File aParentFolder);
}
