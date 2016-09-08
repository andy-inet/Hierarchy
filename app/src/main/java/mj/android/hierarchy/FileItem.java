package mj.android.hierarchy;

import java.io.File;
import java.util.ArrayList;

public class FileItem implements Item {
	
	File file;
	ArrayList<Item> childs;
	
	public FileItem (File f) {
		file = f;
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public String getTitle() {
		return file.getName();
	}

	@Override
	public int getIconResource() {
		if (file.isDirectory()) {
			if (getCountChilds() > 0)
				return R.drawable.folder;
			return R.drawable.empty_folder;
		}
		return R.drawable.file;
	}

	@Override
	public ArrayList<Item> getChilds() {
		if (childs != null)
			return childs;
		
		childs = new ArrayList<Item>();
		
		File[] files = file.listFiles();

		if (files != null) {
			for (File f : files) 
				childs.add(new FileItem(f));
		}
	    
	    return childs;
	}

	private int getCountChilds() {
		
		if (childs != null)
			return childs.size();
		
		File[] files = file.listFiles();
		if (files == null)
			return 0;
		return files.length;
	}
}
