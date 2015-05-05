package com.softserveinc.furniture;

public class FurnitureListItem {

	private int imgId;
	private String title;
	private String modelName;
	
	public FurnitureListItem(int imgId, String title, String modelName) {
		this.imgId = imgId;
		this.title= title;
		this.modelName = modelName;
	}

	public String getTitle() {
		return title;
	}

	public int getImageId() {
		return imgId;
	}
	
	public String getModelName() {
		return modelName;
	}


}
