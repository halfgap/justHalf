package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Meteorite extends FlyingObject implements Enemy{
	private static BufferedImage image;
	static{
			image = loadImage("meteorite.png");
	}
	private int speed;
	private int speedx;
	
	public Meteorite(){
		super(50,100);
		speed = 10;
		speedx = 3;
	}
	public void step(){
		y+=speed;
		x+=speedx;
	}
	
	public BufferedImage getImage(){
		if(isLife()){
			return image;
		}else if(isDead()){
			state = REMOVE;
		}
		return null;
	}
	public int getScore(){
		return 5;
	}
}


