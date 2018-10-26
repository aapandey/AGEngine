package AGEngine.core;

import java.util.*;

import AGEngine.objects.GameObject;

public abstract class Manager {

	//reference to the game engine
	protected Engine engine;

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	public abstract List<GameObject> sendDataToClient();

	public abstract GameObject sendDataToServer();

	public abstract void getDataFromServer(List<GameObject> dataFromServer);

	public abstract void getDataFromClient(GameObject dataFromClient);
	
}
