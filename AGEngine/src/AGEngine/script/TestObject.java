package AGEngine.script;

/* Inspiration and explicit code from following sources:
 * Code shared in Class Moodle
 *  */
public class TestObject {
	public static int GUID = 0;

	public int ID;
	public int x;
	public int y;

	public TestObject() {
		ID = ++GUID;
		x = 0;
		y = 0;
	}

	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public String toString() {
		return "TestObject " + ID + " has position (" + x + ", " + y + ")";
	}
}
