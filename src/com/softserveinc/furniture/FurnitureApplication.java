package com.softserveinc.furniture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Application;

public class FurnitureApplication extends Application {

	private List<FurnitureListItem> items = new ArrayList<FurnitureListItem>(
			Arrays.asList(
					new FurnitureListItem(R.drawable.logo, "Logo", "Logo___v7.obj"),
					new FurnitureListItem(R.drawable.armchair, "Armchair", "armchair.obj"),
					new FurnitureListItem(R.drawable.stereo, "Stereo", "mini_estereo_cycles_baked.obj"),
					new FurnitureListItem(R.drawable.wash_mash, "Washing machine", "clothes_washing_machine_internal.obj"),
					new FurnitureListItem(R.drawable.air_conditioning, "Air conditioning", "internal-air-conditioning.obj"),
					new FurnitureListItem(R.drawable.lavatory, "Lavatory", "Lavatory.obj"),
					//new FurnitureListItem(R.drawable.statue, "Statue", "Statue.obj"),
					new FurnitureListItem(R.drawable.basketball, "Basketball", "Basketball.obj")));
				
	public List<FurnitureListItem> getItemsList() {
		return items;
	}
}
