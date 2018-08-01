package cn.tedu.shoot;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
public abstract class FlyingObject {
	//��ȡͼƬʱ��Ҫ����״̬,״̬һ�����Ϊ����,�����state������ʾ��ǰ״̬
	public static final int LIFE = 0;
	public static final int DEAD = 1;
	public static final int REMOVE = 2;
	protected int state = LIFE;   //��ǰ״̬Ĭ��ΪLIFE
	
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	//ΪӢ�ۻ�,���,�ӵ��ṩ�Ĺ��췽��
	public FlyingObject(int width,int height,int x,int y){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	//ΪС�л�,��л�,С�۷��ṩ�Ĺ��췽��
	public FlyingObject(int width,int height){
		this.width = width;
		this.height = width;
		Random rand = new Random();
		x = rand.nextInt(World.WIDTH - this.width);
		y = -this.height;
	}
	//��ȡͼƬ�ļ�  ��Ϊһ��,�ҷ����Ĳ������������ض�������޹�,���Ϊ��̬����(��ͨ����)
	public static BufferedImage loadImage(String fileName){		
		try{
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	//�������ƶ�
	public abstract void step();
	
	//��ȡͼƬ,��Ϊ���ǲ�һ����,���Ϊ���󷽷�
	public abstract BufferedImage getImage();
	//��Ʒ������ж�ת̬
	public boolean isLife(){
		return state == LIFE;
	}
	public boolean isDead(){
		return state == DEAD;
	}
	public boolean isRemove(){
		return state == REMOVE;
	}
	
	//��ͼ��
	public void paintObject(Graphics g){
		g.drawImage(getImage(),x,y,null);
	}
	
	public boolean outOfBounds(){
		return this.y>=World.HEIGHT;
	}
	
	
	//��ײ���
	public boolean hit(FlyingObject other){
		int x1 = this.x-other.width;
		int x2 = this.x+this.width;
		int y1 = this.y-other.height;
		int y2 = this.y+this.height;			//�����һ������,���������ж��ӵ������ײ,����Ӣ�ۻ������ײ
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
