package mj.android.hierarchy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class HierarchyActivity extends Activity {

	ArrayList<Item> items;
	ListAdapter adapter;
	Item item;
	private  EditText editsearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hierarchy);

		LinearLayout llBtn = (LinearLayout) this.findViewById(R.id.ll_Btn);

		Intent intent = getIntent();

//		if (intent.hasCategory("SPR_RESOURCES"))
//			llBtn.setVisibility(View.VISIBLE);
//		else
//			llBtn.setVisibility(View.INVISIBLE);


   		llBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				(intent.hasCategory("SPR_RESOURCES")) ? -2 : 0));


		// Some hierarchy
		items = generateSomeHierarchy();

		// OR read files on sdcard
		//items = readSDCard();


		adapter = new ListAdapter(this, items);

		ListView mList = (ListView) this.findViewById(R.id.listView);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				((ListView)view).setSelection(position);
				adapter.clickOnItem(position);
				item= (Item) adapter.getItem(position);
				TextView tv= (TextView) findViewById(R.id.tv_item);
				tv.setText(item.getTitle());
			}
		});

		editsearch = (EditText) findViewById(R.id.etFilter);
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub
			}
		});


	}

	private ArrayList<Item> readSDCard() {
		FileItem sdcard = new FileItem(Environment.getExternalStorageDirectory());
		return sdcard.getChilds();
	}

	public	void onSelectClick(View view) {
		setResult(item.getID());
		finish();
	}

	public	void onCancelClick(View view) {
		setResult(0);
		finish();
	}

	private ArrayList<Item> generateSomeHierarchy() {
		items = new ArrayList<Item>();
		int r =100;

		ListItem li1 = new ListItem(11, "Детали");
		ListItem li2 = new ListItem(12, "Заготовки");
		ListItem li3 = new ListItem(13, "Комплектующие изделия");
		
		items.add(li1);
		items.add(li2);
		items.add(li3);
		
		ListItem li11 = new ListItem(++r, "Ячейка ВЧ 845");
		ListItem li12 = new ListItem(++r, "Корпус защищенный");
		ListItem li13 = new ListItem(++r, "Пульт сопряжения ПРМИ 1298");
		
		li1.addChild(li11);
		li1.addChild(li12);
		li1.addChild(li13);
		
		ListItem li21 = new ListItem(21, "Болванки");
		ListItem li22 = new ListItem(22, "Пруток");
		ListItem li23 = new ListItem(23, "Штамповка");

		li2.addChild(li21);
		li2.addChild(li22);
		li2.addChild(li23);



		li21.addChild(new ListItem(++r, "Болванка 7001"));
		li21.addChild(new ListItem(++r, "Болванка 5-500"));
		li21.addChild(new ListItem(++r, "Болванка 4"));
		li21.addChild(new ListItem(++r, "Болванка 4а"));

		li22.addChild(new ListItem(++r, "Пруток М6"));
		li22.addChild(new ListItem(++r, "Пруток ГП4Х"));

		li23.addChild(new ListItem(++r, "Штамповка ГП4-08"));
		li23.addChild(new ListItem(++r, "Штамповка ГП4-92"));

		li3.addChild(new ListItem(++r, "Насос"));
		li3.addChild(new ListItem(++r, "Аптечка"));
		li3.addChild(new ListItem(++r, "Набор инструментов"));
		li3.addChild(new ListItem(++r, "Огнетушитель"));


		return items;
	}
}
