package AGEngine.objects;

import java.awt.Shape;

public interface ICollidable {
	public boolean isColliding(ICollidable gObject);
	public Shape getGameObjectShape();
}
