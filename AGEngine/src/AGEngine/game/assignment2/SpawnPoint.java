package AGEngine.game.assignment2;

import AGEngine.objects.GameObject;
import processing.core.PVector;

public class SpawnPoint extends GameObject{
	
	public SpawnPoint(PVector position) 
	{
		this.position = new PVector(position.x, position.y);
		visible = false;
	}
	
}
