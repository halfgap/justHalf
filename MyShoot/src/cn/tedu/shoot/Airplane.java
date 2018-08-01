package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Airplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){
			images[i] = loadImage("airplane"+i+".png");
		}
	}
	private int speed;
	private int speedx;
	
	public Airplane(){
		super(49,36);
		speed = 3;
		speedx = 5;
	
	
	}
	public void step(){
		y+=speed;
		x+=speedx;
		if(x >= World.WIDTH-this.width||x<= 0){
			speedx = -speedx;
		}
	}
	
	int index=1;
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
		/*
		 * 							index=1
		 * 10ms img=images[1]		index=2		返回images[1]
		 * 20ms img=images[2]		index=3		返回images[2]
		 * 30ms img=images[3]		index=4		返回images[3]
		 * 40ms img=images[4]		index=5(REMOVE)		返回images[4]
		 * 50ms 返回null
		 */
	}
	
	public int getScore(){
		return 1;
		
	}
	
}
