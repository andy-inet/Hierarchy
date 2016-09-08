package mj.android.hierarchy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter implements Filterable {

	public static final int LEVEL_INDENT = 32;

	private LayoutInflater mLayoutInflater;
	private ArrayList<Pair> hierarchyArray;

	private ArrayList<Item> originalItems;
	private LinkedList<Item> openItems;
	private List<Item> mResults;
	private String filterText;

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
//					List<Books> books = findBooks(mContext, constraint.toString());
					List<Item> res = new LinkedList<Item>();
// Assign the data to the FilterResults
					filterResults.values = res;
					filterResults.count = res.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					mResults = (List<Item>) results.values;
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}};

		return filter;
	}

	private class Pair {
		Item item;
		int level;
		
		Pair (Item item, int level) {
			this.item = item;
			this.level = level;
		}
	}
	


	public ListAdapter (Context ctx, ArrayList<Item> items) {  
		mLayoutInflater = LayoutInflater.from(ctx);
		originalItems = items;
		
		hierarchyArray = new ArrayList<Pair>();
		openItems = new LinkedList<Item>();
		mResults = new ArrayList<Item>();

		generateHierarchy();		
	}  
	    
	@Override
	public int getCount() {
		return hierarchyArray.size();
	}

	@Override
	public Object getItem(int position) {
		return hierarchyArray.get(position).item;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)  
			convertView = mLayoutInflater.inflate(R.layout.row, null);             
		TextView title = (TextView)convertView.findViewById(R.id.title);
		
		Pair pair = hierarchyArray.get(position);
		
		title.setText(pair.item.getTitle());
		title.setCompoundDrawablesWithIntrinsicBounds(pair.item.getIconResource(), 0, 0, 0);
		title.setPadding(pair.level * LEVEL_INDENT, 0, 0, 0);
		return convertView;  
	}
	
	public void clickOnItem (int position) {
		
		Item i = hierarchyArray.get(position).item;
		if (!closeItem(i))
			openItems.add(i);

		generateHierarchy();
		notifyDataSetChanged();
	}
	
	private boolean closeItem (Item i) {
		if (openItems.remove(i)) {
			for (Item c : i.getChilds())
				closeItem(c);
			return true;
		}
		return false;
	}
	
	private void generateHierarchy() {
		hierarchyArray.clear();
		generateList(originalItems, 0);
	}

	private void generateList(ArrayList<Item> items, int level) {
		boolean yes;
		for (Item i : items) {
			if (filterText==null)
				yes=true;
			else if (filterText.trim().length()==0)
				yes=true;
			else
			  yes=i.getTitle().toLowerCase(Locale.getDefault()).contains(filterText);

			if ((!yes) && (i.getChilds().size()>0))
				yes=isExistsChildForFilter(i);

			if (yes) {
				hierarchyArray.add(new Pair(i, level));
				if (openItems.contains(i))
					generateList(i.getChilds(), level + 1);
			}
		}
	}

	private boolean isExistsChildForFilter(Item item) {
		for (Item i : item.getChilds()) {
			if (i.getTitle().toLowerCase(Locale.getDefault()).contains(filterText))
				return true;
			else if (i.getChilds().size()>0)
				if(isExistsChildForFilter(i))
					return true;
		}
		return false;
	}

	public void filter(String charText) {
		filterText=charText.toLowerCase(Locale.getDefault());
		generateHierarchy();

/*
			charText = charText.toLowerCase(Locale.getDefault());
		mDataset = new ArrayList<String>();
		if (charText.length() == 0) {
			mDataset.addAll(mCleanCopyDataset);
		} else {
			for (String item : mCleanCopyDataset) {
				if (item.toLowerCase(Locale.getDefault()).contains(charText)) {
					mDataset.add(item);
				}
			}
		}
*/
		notifyDataSetChanged();
	}

}