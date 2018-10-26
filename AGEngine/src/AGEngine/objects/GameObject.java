package AGEngine.objects;

import java.io.Serializable;

import processing.core.PVector;

public abstract class GameObject implements Serializable{
	
	protected PVector color;
	protected PVector position;
	protected PVector size;
	protected boolean visible;
	
	public enum Tag{
		PLAYER, STATIC_PLATFORM, DYNAMIC_PLATFORM, OTHER, DEATH_ZONE, OTHER_PLAYER
	}
	
	public Tag objectTag;

	public int objectID;

	public PVector getPosition() { 
		return position; 
	}
	
	public PVector getSize() {
		return size;
	}
	
	public PVector getColor() { 
		return color; 
	}
	
	public int getObjectID() { 
		return objectID; 
	}
	
	public Tag getObjectTag() {
		return objectTag;
	}

	public void setColor(PVector color) {
		this.color = color;
	}
	
	public void setPosition(PVector position) {
		this.position = position;
	}
	
	public void setSize(PVector size) {
		this.size = size;
	}

	public void setObjectTag(Tag objectTag) {
		this.objectTag = objectTag;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}

	public boolean isVisible() {
		return visible;
	}
}
