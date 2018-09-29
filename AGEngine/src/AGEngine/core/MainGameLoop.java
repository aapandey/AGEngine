package AGEngine.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/* Inspiration and explicit code from following sources:
 * https://processing.org/tutorials/eclipse/
 * http://learningprocessing.com/examples/chp23/example-23-03-Rectangle
 * https://processing.org/reference/keyCode.html
 * https://www.journaldev.com/1289/copyonwritearraylist-java
 *  */
public class MainGameLoop extends PApplet{
	
	// Holder for 5 different rectangles 
	List<GameObject> rectangles = new CopyOnWriteArrayList<GameObject>();
	
	GameObject player;
	
	// Array of boolean values that signify keys
	//boolean [] keys = new boolean[128];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("AGEngine.core.MainGameLoop");
	}
	
	public void settings(){
		size(500, 500);
    }

    public void setup(){
    	stroke(0);
    	
    	for(int i = 0; i < 5; i++) {
    		rectangles.add(new GameObject(this));
    	}
    	
    	// Initialize player object
    	player = new Player(this, new PVector(5, 5), 10);
    	
    	rectangles.get(0).createShape("Rectangle", 100, 100, 50, 50);
    	rectangles.get(0).setColor(255, 0, 0, 255);
    	rectangles.get(1).createShape("Rectangle", 350, 150, 50, 100);
    	rectangles.get(1).setColor(0, 255, 0, 255);
    	rectangles.get(2).createShape("Rectangle", 30, 400, 250, 40);
    	rectangles.get(2).setColor(0, 0, 255, 255);
    	rectangles.get(3).createShape("Rectangle", 400, 300, 100, 75);
    	rectangles.get(3).setColor(255, 255, 0, 255);
    	rectangles.get(4).createShape("Rectangle", 10, 200, 100, 100);
    	rectangles.get(4).setColor(0, 255, 255, 255);
    	
    	player.createShape("Rectangle", 250, 250, 60, 40);
    	player.setColor(100, 100, 150, 255);
    	// Pass rectangles as references
    	((Player) player).set_gameObjects(rectangles);
    	
    	frameRate(60);
    }

    public void draw(){
    	background(255);
    	
    	for(int i = 0; i < rectangles.size(); i++) {
    		rectangles.get(i).display();
    	}
    	
    	((Player) player).run();
    }
    
    public void keyPressed() {
    	if (key == CODED) {
    	    if (keyCode == UP) {
    	      ((Player) player).set_up(true);
    	    }
    	    if (keyCode == DOWN) {
    	    	((Player) player).set_down(true);
    	    }
    	    if (keyCode == LEFT) {
    	    	((Player) player).set_left(true);
    	    } 
    	    if (keyCode == RIGHT) {
    	    	((Player) player).set_right(true);
    	    } 
    	}
    	if(key == ' ') {
    		((Player) player).set_up(true);
    	}
    }
    	 
    public void keyReleased() {
    	if (key == CODED) {
    	    if (keyCode == UP) {
    	      ((Player) player).set_up(false);
    	    }
    	    if (keyCode == DOWN) {
    	    	((Player) player).set_down(false);
    	    }
    	    if (keyCode == LEFT) {
    	    	((Player) player).set_left(false);
    	    } 
    	    if (keyCode == RIGHT) {
    	    	((Player) player).set_right(false);
    	    } 
    	}
    	if(key == ' ') {
    		((Player) player).set_up(false);
    	}
    }

}
