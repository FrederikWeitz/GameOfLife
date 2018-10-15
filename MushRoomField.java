package somejavafx;

import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MushRoomField {

	int width = 0;
	int height = 0;
	
	int xMax = 0;
	int yMax = 0;
	int radius;
	
	public Mushroom[][] mushroomField;
	
	Canvas canvas;
	GraphicsContext gc;
	
	public MushRoomField() {
		
	}
	
	public void setFieldCoordinates(int w, int h) {
		width = w;
		height = h;
		
		initField();
		createField();
	}
	
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
		gc = this.canvas.getGraphicsContext2D();
	}
	
	public void initField() {
		mushroomField = new Mushroom[width][height];
		
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				mushroomField[i][j] = new Mushroom();
			}
		}
		
		Mushroom actMushroom;
		Mushroom bindMushroom;
		
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				actMushroom = mushroomField[i][j];
				
				//up
				bindMushroom = mushroomField[(i+width+0)%width][(j+height-1)%height];
				actMushroom.up = bindMushroom.getMushroomSet();
				
				//upright
				bindMushroom = mushroomField[(i+width+1)%width][(j+height-1)%height];
				actMushroom.upright = bindMushroom.getMushroomSet();
				
				// right
				bindMushroom = mushroomField[(i+width+1)%width][(j+height-0)%height];
				actMushroom.right = bindMushroom.getMushroomSet();
				
				// downright
				bindMushroom = mushroomField[(i+width+1)%width][(j+height+1)%height];
				actMushroom.downright = bindMushroom.getMushroomSet();
				
				// down
				bindMushroom = mushroomField[(i+width+0)%width][(j+height+1)%height];
				actMushroom.down = bindMushroom.getMushroomSet();
				
				// downleft 
				bindMushroom = mushroomField[(i+width-1)%width][(j+height+1)%height];
				actMushroom.downleft = bindMushroom.getMushroomSet();
				
				// left
				bindMushroom = mushroomField[(i+width-1)%width][(j+height-0)%height];
				actMushroom.left = bindMushroom.getMushroomSet();
				
				// upleft
				bindMushroom = mushroomField[(i+width-1)%width][(j+height-1)%height];
				actMushroom.upleft = bindMushroom.getMushroomSet();
			}
		}
	}
	
	public void createField() {
		if (canvas!=null) {
			
			xMax = (int)(canvas.getWidth()+1)/width;
			yMax = (int)(canvas.getHeight()+1)/height;
			radius = xMax/2;
			canvas.setWidth(width*xMax);
			canvas.setHeight(height*yMax);
			
			gc.setFill(Color.ANTIQUEWHITE);
			gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					mushroomField[i][j].setAppearance(i*xMax, j*yMax, radius);
				}
			}
		}
	}
	
	public void randomizeField() {
		Random random = new Random();
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if (random.nextInt(10)<5) {
					mushroomField[i][j].setMushroom();
				} else {
					mushroomField[i][j].deleteMushroom();
				}
				mushroomField[i][j].paint(gc);
			}
		}
	}
	
	public void nextStep() {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				mushroomField[i][j].hasToChange();
			}
		}
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				mushroomField[i][j].paint(gc);
			}
		}
	}
	
	public void addMushrooms(int i) {
		Random random = new Random();
		int x, y;
		int j = 200;
		while (i>0 && j>0) {
			j--;
			x = random.nextInt(width);
			y = random.nextInt(height);
			if(!mushroomField[x][y].getMushroomSet().getValue()) {
				mushroomField[x][y].setMushroom();
				mushroomField[x][y].paint(gc);
				i--;
				j = 200;
			}
		}
	}
	
}
