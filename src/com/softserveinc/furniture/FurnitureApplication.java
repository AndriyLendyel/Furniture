package com.softserveinc.furniture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Application;

public class FurnitureApplication extends Application {

	private List<FurnitureListItem> items = new ArrayList<FurnitureListItem>(
			Arrays.asList(
					new FurnitureListItem(R.drawable.picture1, "Picture", "VaticanMuseumFrame.obj"),
					new FurnitureListItem(R.drawable.chair, "Chair", "stuhl.obj"),
					new FurnitureListItem(R.drawable.furnitura_xena, "Bookshelves", "furniture_xena.obj"),
					new FurnitureListItem(R.drawable.modern_shelves, "Modern shelves", "Modern-Shelves.obj"),
					new FurnitureListItem(R.drawable.glass_drawer, "Glass drawer", "GlassDrawer.obj"),
					new FurnitureListItem(R.drawable.rack, "Rack", "Rack.obj"),
					new FurnitureListItem(R.drawable.kitchen_furniture,	"Kitchen furniture", "Kitchen_furniture.obj"),
					new FurnitureListItem(R.drawable.sofa1, "Sofa", "sofa.obj"),
					new FurnitureListItem(R.drawable.wooden_chair, "Wooden chair", "Wooden_Chair.obj"),
					new FurnitureListItem(R.drawable.chaise_orange, "Chair", "chaiseOrange.obj"),
					new FurnitureListItem(R.drawable.table, "Table", "table.obj"),
					new FurnitureListItem(R.drawable.table, "LOGO", "armchair.obj"),
					new FurnitureListItem(R.drawable.table, "LOGO", "mini_estereo_cycles_baked.obj"),
					new FurnitureListItem(R.drawable.table, "LOGO", "Testcube.obj")));

	public List<FurnitureListItem> getItemsList() {
		return items;
	}
}
