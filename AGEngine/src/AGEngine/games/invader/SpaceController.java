package AGEngine.games.invader;

import java.util.ArrayList;

import AGEngine.games.invader.SpaceController.Bullet;
import AGEngine.games.invader.SpaceController.Enemy;
import processing.core.PApplet;
import processing.core.PConstants;

/* Inspiration and explicit code from following sources:
 * https://processing.org/tutorials/eclipse/
 * http://learningprocessing.com/examples/chp23/example-23-03-Rectangle
 * https://processing.org/reference/keyCode.html
 * https://www.journaldev.com/1289/copyonwritearraylist-java
 * https://www.openprocessing.org/sketch/57710/#
 *  */

public class SpaceController extends PApplet {
	int pixelsize = 6;
	int gridsize  = pixelsize * 7 + 5;
	Player player;
	
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	int direction = 1;
	boolean incy = false;

	public void setup() {
	    background(0);
	    noStroke();
	    fill(255);
	    size(1000, 900);
	    player = new Player(this);
	    createEnemies();
	}
	
	public static void main(String[] args) {
		PApplet.main("AGEngine.games.invader.SpaceController");
	}

	public void draw() {
	    background(0);

	    player.draw();

	    for (int i = 0; i < bullets.size(); i++) {
	        Bullet bullet = (Bullet) bullets.get(i);
	        bullet.draw();
	    }

	    for (int i = 0; i < enemies.size(); i++) {
	        Enemy enemy = (Enemy) enemies.get(i);
	        if (enemy.outside() == true) {
	            direction *= (-1);
	            incy = true;
	            break;
	        }
	    }

	    for (int i = 0; i < enemies.size(); i++) {
	        Enemy enemy = (Enemy) enemies.get(i);
	        if (!enemy.alive()) {
	            enemies.remove(i);
	        } 
	        else {
	            enemy.draw();
	        }
	    }

	    incy = false;
	}

	void createEnemies() {
	    for (int i = 0; i < width/gridsize/2; i++) {
	        for (int j = 0; j <= 5; j++) {
	            enemies.add(new Enemy(this, i*gridsize, j*gridsize));
	        }
	    }
	}

	class SpaceShip {
	    int x, y;
	    String sprite[];
	    PApplet p;
	    
	   public SpaceShip(PApplet parent){
	    	p = parent;
	    }

	    public void draw() {
	        updateObj();
	        drawSprite(x, y);
	    }

	    public void drawSprite(int xpos, int ypos) {
	        for (int i = 0; i < sprite.length; i++) {
	            String row = (String) sprite[i];

	            for (int j = 0; j < row.length(); j++) {
	                if (row.charAt(j) == '1')
	                    p.rect(xpos+(j * pixelsize), ypos+(i * pixelsize), pixelsize, pixelsize);
	            }
	        }
	    }

	    public void updateObj() {
	    }
	}

	class Player extends SpaceShip {
	    boolean canShoot = true;
	    int shootdelay = 0;

	    public Player(PApplet parent) {
	    	super(parent);
	        x = width/gridsize/2;
	        y = height - (10 * pixelsize);
	        sprite    = new String[5];
	        sprite[0] = "0010100";
	        sprite[1] = "0110110";
	        sprite[2] = "1111111";
	        sprite[3] = "1111111";
	        sprite[4] = "0111110";
	    }

	    public void updateObj() {
	        if (p.keyPressed && p.keyCode == PConstants.LEFT) x -= 5;
	        if (p.keyPressed && p.keyCode == PConstants.RIGHT) x += 5;
	        if (p.keyPressed && p.keyCode == PConstants.CONTROL && canShoot) {
	            bullets.add(new Bullet(p, x, y));
	            canShoot = false;
	            shootdelay = 0;
	        }

	        shootdelay++;
	        if (shootdelay >= 20) {
	            canShoot = true;
	        }
	    }
	}

	class Enemy extends SpaceShip {
	    public Enemy(PApplet parent, int xpos, int ypos) {
	    	super(parent);
	        x = xpos;
	        y = ypos;
	        sprite    = new String[5];
	        sprite[0] = "1011101";
	        sprite[1] = "0101010";
	        sprite[2] = "1111111";
	        sprite[3] = "0101010";
	        sprite[4] = "1000001";
	    }

	    public void updateObj() {
	        if (frameCount%30 == 0) x += direction * gridsize;
	        if (incy == true) y += gridsize / 2;
	    }

	    public boolean alive() {
	        for (int i = 0; i < bullets.size(); i++) {
	            Bullet bullet = (Bullet) bullets.get(i);
	            if (bullet.x > x && bullet.x < x + 7 * pixelsize + 5 && bullet.y > y && bullet.y < y + 5 * pixelsize) {
	                bullets.remove(i);
	                return false;
	            }
	        }

	        return true;
	    }

	    public boolean outside() {
	        if (x + (direction*gridsize) < 0 || x + (direction*gridsize) > width - gridsize) {
	            return true;
	        } 
	        else {
	            return false;
	        }
	    }
	}

	class Bullet{
	    int x, y;
	    PApplet p;

	    public Bullet(PApplet parent, int xpos, int ypos) {
	    	p = parent;
	        x = xpos + gridsize/2 - 4;
	        y = ypos;
	    }

	    public void draw() {
	        p.rect(x, y, pixelsize, pixelsize);
	        y -= pixelsize;
	    }
	}
}
