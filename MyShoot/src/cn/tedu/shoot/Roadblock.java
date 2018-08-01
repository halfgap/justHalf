package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Roadblock extends FlyingObject implements Enemy,Life{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){
			images[i] = loadImage("road"+i+".png");
		}
	}
	private int speed;
	private int speedx;
	private int hp;

	
	public Roadblock(){
		super(150,150);
		speed = 1;
		speedx = 2;
		hp = 10;

	
	}
	public void step(){
		if(this.y<350){
		y+=speed;
		x+=speedx;
		}
		if(x >= World.WIDTH-this.width||x<= 0){
			speedx = -speedx;
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
	
	public int getScore(){
		return 1;
	}

	public int costLife(){
		return hp--;
	}
	public int getHp(){
		return hp;
	}
	public void goDead(){
		state = DEAD;
	}
}
