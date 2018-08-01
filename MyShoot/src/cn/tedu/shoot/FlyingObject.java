package cn.tedu.shoot;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
public abstract class FlyingObject {
	//获取图片时需要考虑状态,状态一般设计为常量,再设计state变量表示当前状态
	public static final int LIFE = 0;
	public static final int DEAD = 1;
	public static final int REMOVE = 2;
	protected int state = LIFE;   //当前状态默认为LIFE
	
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	//为英雄机,天空,子弹提供的构造方法
	public FlyingObject(int width,int height,int x,int y){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	//为小敌机,大敌机,小蜜蜂提供的构造方法
	public FlyingObject(int width,int height){
		this.width = width;
		this.height = width;
		Random rand = new Random();
		x = rand.nextInt(World.WIDTH - this.width);
		y = -this.height;
	}
	//读取图片文件  行为一致,且方法的操作仅与参数相关而与对象无关,设计为静态方法(普通方法)
	public static BufferedImage loadImage(String fileName){		
		try{
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	//飞行物移动
	public abstract void step();
	
	//获取图片,行为都是不一样的,设计为抽象方法
	public abstract BufferedImage getImage();
	//设计方法来判断转态
	public boolean isLife(){
		return state == LIFE;
	}
	public boolean isDead(){
		return state == DEAD;
	}
	public boolean isRemove(){
		return state == REMOVE;
	}
	
	//画图像
	public void paintObject(Graphics g){
		g.drawImage(getImage(),x,y,null);
	}
	
	public boolean outOfBounds(){
		return this.y>=World.HEIGHT;
	}
	
	
	//碰撞检测
	public boolean hit(FlyingObject other){
		int x1 = this.x-other.width;
		int x2 = this.x+this.width;
		int y1 = this.y-other.height;
		int y2 = this.y+this.height;			//设计了一个区域,该区域内判定子弹与敌人撞,或者英雄机与敌人撞
		int x = other.x;
		int y = other.y;
		return x>=x1&&x<=x2&&y>=y1&&y<=y2;
	}
    
	public void goDead(){
		state = DEAD;
	}
	
	public Bullte bshoot(){
		Bullte bs=new Bullte(this.x+this.width/2,this.y+100);
	
		return bs;
}
}
