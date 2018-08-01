package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Bullte extends FlyingObject{
	private static BufferedImage image;
	static{
		image=loadImage("enemybullet.png");
	}
	
	
	private int step;
	public Bullte(int x,int y){
		super(10,25);
		this.x = x;
		this.y = y;
		step = 4;
	}
    public void step(){
		y+=step;
	}
    public BufferedImage getImage(){
	if(isLife()){
		return image;
	}
	if(isDead()){
		state = REMOVE;
	}
	return null;
}
    public boolean outOfBounds(){
		return this.y>=World.HEIGHT;
	}
    
}
