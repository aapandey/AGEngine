package AGEngine.script;

public class ScriptIntegrationExample {
	static TestObject go1 = new TestObject();
	static TestObject go2 = new TestObject();
	static TestObject go3 = new TestObject();

	public static void main(String[] args) {

		while(true) {

			//  ONE (execute a script)
			ScriptManager.loadScript("AGEngine/src/Scripts/hello_world.js");
			//*/ // ONE

			///*  TWO and THREE (reference an object)
			ScriptManager.loadScript("AGEngine/src/Scripts/print_object.js");

			ScriptManager.bindArgument("game_object", go1);
			ScriptManager.executeScript();
			//*/ // TWO

			///* THREE (reference multiple objects)
			ScriptManager.bindArgument("game_object", go2);
			ScriptManager.executeScript();
			//*/ // THREE

			/* FOUR and FIVE (pass in arguments to function)
			ScriptManager.loadScript("AGEngine/src/Scripts/modify_position.js");

			ScriptManager.bindArgument("game_object", go1);
			ScriptManager.executeScript("5","5");

			ScriptManager.bindArgument("game_object", go2);
			ScriptManager.executeScript("10","15");
			ScriptManager.executeScript("25","-10");
			*/ // FOUR

			///* FIVE (more complicated scripts)
			ScriptManager.bindArgument("game_object", go3);
			ScriptManager.loadScript("AGEngine/src/Scripts/random_position.js");
			for(int i=0; i<5; i++) {
				ScriptManager.executeScript();
			}
			//*/ // FIVE

			/* SIX (more complicated scripts)
			ScriptManager.bindArgument("game_object_1", go1);
			ScriptManager.bindArgument("game_object_2", go2);
			ScriptManager.loadScript("AGEngine/src/Scripts/random_object.js");
			for(int i=0; i<5; i++) {
				ScriptManager.executeScript();
			}
			*/ // SIX

			try { System.in.read(); }
			catch(Exception e) { }
		}

	}
}
