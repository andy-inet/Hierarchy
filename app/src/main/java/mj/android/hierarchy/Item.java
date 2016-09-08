package mj.android.hierarchy;

import java.util.ArrayList;

public interface Item {
	public int getID();
	public String getTitle();
	public int getIconResource();
	public ArrayList<Item> getChilds();
}
