package cn.tedu.shoot;

import java.awt.image.BufferedImage;


public class Boss extends FlyingObject implements Life,Enemy{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){
			images[i] = loadImage("boss"+i+".png");
		}
		}
	private int xspeed;
	private int yspeed;
	private int life;

	public Boss(){
		super(185,120);
		xspeed = 1;
		yspeed = 1;
		life = 100;
	}
	public static Boss inTo;
	
	public static Boss getIn(){
        if(null==inTo){
        	inTo = new Boss();
        }
        return inTo;
    }
	
	public void step(){
		if(y<200){
			y+=yspeed;
		}
		x+=xspeed;
		if(x<=0 || x>=World.WIDTH-this.width){
			xspeed = -xspeed;
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
		return 3;
	}
	
	public int costLife(){
		return life--;
	}
	public int getHp(){
		return life;
	}
	public void goDead(){
		state = DEAD;
	}

}

