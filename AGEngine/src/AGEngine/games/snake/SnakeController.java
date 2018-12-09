package AGEngine.games.snake;

import java.util.ArrayList;

import processing.core.PApplet;

/* Inspiration and explicit code from following sources:
 * https://processing.org/tutorials/eclipse/
 * http://learningprocessing.com/examples/chp23/example-23-03-Rectangle
 * https://processing.org/reference/keyCode.html
 * https://www.openprocessing.org/sketch/106774
 *  */

public class SnakeController extends PApplet {
	
	Snake snake;
	Food food;
	int highScore;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("AGEngine.games.snake.SnakeController");
	}

	public void setup()
	{
		size(1000, 600);
		frameRate(14);
		snake = new Snake(this);
		food = new Food(this);
		rectMode(CENTER);
		textAlign(CENTER, CENTER);
		highScore = 0;
	}

	public void draw()
	{
		background(250, 250, 250);
		drawScoreboard();
	  
		snake.move();
		snake.display();
		food.display();
	  
		if( dist(food.xpos, food.ypos, snake.xpos.get(0), snake.ypos.get(0)) < snake.sidelen )
		{
			food.reset();
			snake.addLink();
		}
	  
		if(snake.len > highScore)
		{
			highScore = snake.len;
		}
	}

	public void keyPressed()
	{
		if(key == CODED){
			if(keyCode == LEFT){
				snake.dir = "left";
			}
			if(keyCode == RIGHT){
				snake.dir = "right";
			}
			if(keyCode == UP){
				snake.dir = "up";
			}
			if(keyCode == DOWN){
				snake.dir = "down";
			}
		}
	}

	void drawScoreboard()
	{
	  fill(250, 0, 250);
	  textSize(65);
	  text( "Snake Game", width/2, 80);
	  fill(250, 0, 250);
	  textSize(20);
	  
	  // draw score board
	  stroke(179, 140, 198);
	  fill(255, 0 ,255);
	  rect(90, 70, 160, 80);
	  fill(118, 22, 167);
	  textSize(17);
	  text( "Score: " + snake.len, 70, 50);
	  
	  fill(118, 22, 167);
	  textSize(17);
	  text( "High Score: " + highScore, 70, 70);
	}
}

	class Food{
	  
	  // define variables
	  float xpos, ypos;
	  PApplet p;
	
	  //constructor
	  Food(PApplet parent)
	  {
		  p = parent;
	      xpos = p.random(100, p.width - 100);
	      ypos = p.random(100, p.height - 100);
	  }

	 void display(){
		 p.fill(190,0,100);
	     p.ellipse(xpos, ypos,17,17);
	 }
	 
	 void reset(){
	    xpos = p.random(100, p.width - 100);
	    ypos = p.random(100, p.height - 100);
	 }  
	 
	}

	class Snake{
	  
	  //define variables
	  int len;
	  float sidelen;
	  String dir; 
	  ArrayList <Float> xpos, ypos;
	  PApplet p;
	  
	  // constructor
	  Snake(PApplet parent)
	  {
		  p = parent;
	      len = 1;
	      sidelen = 17;
	      dir = "right";
	      xpos = new ArrayList<Float>();
	      ypos = new ArrayList();
	      xpos.add(p.random(p.width));
	      ypos.add(p.random(p.height));
	  }
	  
	  void move(){
		  for(int i = len - 1; i > 0; i = i -1 ){
			  xpos.set(i, xpos.get(i - 1));
			  ypos.set(i, ypos.get(i - 1));  
		  } 
		  
		  if(dir == "left"){
			  xpos.set(0, xpos.get(0) - sidelen);
		  }
		  if(dir == "right"){
			  xpos.set(0, xpos.get(0) + sidelen);
		  }
		  if(dir == "up"){
			  ypos.set(0, ypos.get(0) - sidelen);
		  }
		  if(dir == "down"){
			  ypos.set(0, ypos.get(0) + sidelen);
		  }
		  xpos.set(0, (xpos.get(0) + p.width) % p.width);
		  ypos.set(0, (ypos.get(0) + p.height) % p.height);
	   
	    // check if hit itself and if so cut off the tail
		  if( checkHit() == true){
			  len = 1;
			  float xtemp = xpos.get(0);
			  float ytemp = ypos.get(0);
			  xpos.clear();
			  ypos.clear();
			  xpos.add(xtemp);
	      	  ypos.add(ytemp);
		  }
	  }

	  void display()
	  {
		  for(int i = 0; i < len; i++)
		  {
			  p.stroke(179, 140, 198);
			  p.fill(100, 0, 100, p.map(i-1, 0, len-1, 250, 50));
			  p.rect(xpos.get(i), ypos.get(i), sidelen, sidelen);
		  }  
	  }

	  void addLink()
	  {
		  xpos.add(xpos.get(len-1) + sidelen);
		  ypos.add(ypos.get(len-1) + sidelen);
		  len++;
	  }
	  
	  boolean checkHit()
	  {
		  for(int i = 1; i < len; i++){
			  if( p.dist(xpos.get(0), ypos.get(0), xpos.get(i), ypos.get(i)) < sidelen){
				  return true;
			  } 
		  } 
		  return false;
	  } 
	}
