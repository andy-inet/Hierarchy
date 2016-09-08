package mj.android.hierarchy;

import java.util.ArrayList;

public class ListItem implements Item {

	private int id;
	private String title;
	private ArrayList<Item> childs;
	
	public ListItem (int id, String title) {
		this.id = id;
		this.title = title;
		childs = new ArrayList<Item>();
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public ArrayList<Item> getChilds() {
		return childs;
	}
	
	@Override
	public int getIconResource() {
		if (childs.size() > 0)
			return R.drawable.folder;
		return R.drawable.file;
	}

	public void addChild (Item item) {
		childs.add(item);
	}
}
