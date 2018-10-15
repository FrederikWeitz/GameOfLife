package somejavafx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mushroom {

	SimpleBooleanProperty up;
	SimpleBooleanProperty upright;
	SimpleBooleanProperty right;
	SimpleBooleanProperty downright;
	SimpleBooleanProperty down;
	SimpleBooleanProperty downleft;
	SimpleBooleanProperty left;
	SimpleBooleanProperty upleft;
	
	private int x;
	private int y;
	private int radius;
	SimpleBooleanProperty setMushroom = new SimpleBooleanProperty();
	private boolean toSetMushroom;
	
	Canvas canvas;
	
	public Mushroom() {
		up = null;
		upright = null;
		right = null;
		downright = null;
		down = null;
		downleft = null;
		left = null;
		upleft = null;
		
		x = 0;
		y = 0;
		radius = 0;
		setMushroom.setValue(false);
		toSetMushroom = false;
	}
	
	public void setAppearance(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void hasToChange() {
		int z = 0;
		if (up!=null) {
			z = (up.getValue()) ? z+1 : z;
		}
		if (upright!=null) {
			z = (upright.getValue()) ? z+1 : z;
		}
		if (right!=null) {
			z = (right.getValue()) ? z+1 : z;
		}
		if (downright!=null) {
			z = (downright.getValue()) ? z+1 : z;
		}
		if (down!=null) {
			z = (down.getValue()) ? z+1 : z;
		}
		if (downleft!=null) {
			z = (downleft.getValue()) ? z+1 : z;
		}
		if (left!=null) {
			z = (left.getValue()) ? z+1 : z;
		}
		if (upleft!=null) {
			z = (upleft.getValue()) ? z+1 : z;
		}
		
		if (z==2 || z==3) {
			toSetMushroom = setMushroom.getValue() ? true : ((z==3) ? true : false);
		} else {
			toSetMushroom = false;
		}
	}

	public void setMushroom() {
		toSetMushroom = true;
	}
	
	public void deleteMushroom() {
		toSetMushroom = false;
	}
	
	public void paint(GraphicsContext gc) {
		if (toSetMushroom) {
			gc.setFill(Color.GREEN);
			gc.fillOval(x,y,2*radius,2*radius);
			setMushroom.setValue(true);
		} else {
			gc.setFill(Color.ANTIQUEWHITE);
			gc.fillOval(x,y,2*radius,2*radius);
			setMushroom.setValue(false);
		}
	}
	
	public SimpleBooleanProperty getMushroomSet() {
		return setMushroom;
	}
}
