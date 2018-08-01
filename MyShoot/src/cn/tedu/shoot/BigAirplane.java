package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class BigAirplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){
			images[i] = loadImage("bigplane"+i+".png");
		}
	}
	private int speed;
	
	public BigAirplane(){
		super(69,99);
		speed = 3;
	}
	public void step(){
		y+=speed;
	}
	
	int index = 1;
	public BufferedImage getImage(){
		if(isLife()){
			return images[0];
		}else if(isDead()){
			BufferedImage img = images[index++];
			if(index==images.length){
				state = REMOVE;
			}
			return img;
		}
		return null;
	}
	
	public int getScore(){
		return 3;
	}
}
