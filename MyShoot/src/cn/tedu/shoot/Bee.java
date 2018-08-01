package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){
			images[i] = loadImage("bee"+i+".png");
		}
	}
	private int xSpeed;
	private int ySpeed;
	private int awardType;
	
	public Bee(){
		super(60,50);
		Random rand = new Random();
		xSpeed = 5;
		ySpeed = 4;
		awardType=rand.nextInt(2);  //0到1之间的随机数
	}
	public void step(){
		y+=ySpeed;
		x+=xSpeed;
		if(x >= World.WIDTH-this.width||x<= 0){
			xSpeed = -xSpeed;
			//xSpeed*=-1;
		}
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
	
	public int getAwardType(){
		return awardType;
	}
}
