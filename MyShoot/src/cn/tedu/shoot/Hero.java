package cn.tedu.shoot;
import java.awt.image.BufferedImage;
public class Hero extends FlyingObject{
	private static BufferedImage[] images;
	static{
		images = new BufferedImage[2];
		images[0] = loadImage("hero0.png");
		images[1] = loadImage("hero1.png");
	}
	private int life;
	private int doubleFire;
	
	public Hero(){
		super(97,124,300,800);
		life = 3;
		doubleFire = 100;
	}
	
	public void moveTo(int x,int y){
		this.x = x-this.width/2;
		this.y = y-this.height/2;
	}
	public void step(){
		
	}
	//实现飞机在两张图片间的切换
	int index = 0;
	public BufferedImage getImage(){
		if(isLife()){
			return images[index++%images.length];
		}
		return null;
	}
	/*						index=0
	 * 10ms 返回images[0]		index=1
	 * 20ms 返回images[1]		index=2
	 * 30ms 返回images[0]		index=3
	 * 40ms 返回images[1]		index=4
	 */
	
	
	//生成子弹对象
	public Bullet[] shoot(){
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire>150){
			Bullet[] bs = new Bullet[11];
			bs[0] = new Bullet(this.x-4*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x-3*xStep,this.y-yStep);
			bs[2] = new Bullet(this.x-2*xStep,this.y-2*yStep);
			bs[3] = new Bullet(this.x-1*xStep,this.y-2*yStep);
			bs[4] = new Bullet(this.x+1*xStep,this.y-2*yStep);
			bs[5] = new Bullet(this.x+2*xStep,this.y-2*yStep);
			bs[6] = new Bullet(this.x+3*xStep,this.y-2*yStep);
			bs[7] = new Bullet(this.x+4*xStep,this.y-2*yStep);
			bs[8] = new Bullet(this.x+5*xStep,this.y-2*yStep);
			bs[9] = new Bullet(this.x+6*xStep,this.y-2*yStep);
			bs[10] = new Bullet(this.x+7*xStep,this.y-2*yStep);
			doubleFire-=5;
			return bs;
		}else if(doubleFire>100&&doubleFire<=150){
			Bullet[] bs = new Bullet[4];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			bs[2] = new Bullet(this.x+1*xStep,this.y-2*yStep);
			bs[3] = new Bullet(this.x+3*xStep,this.y-2*yStep);
			doubleFire-=3;
			return bs;
		}  else if(doubleFire>50&&doubleFire<=150){
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire-=2;
			return bs;
		}else{
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}
	}
	
	public void addLife(){
		life++;   //由于数据私有,  使用公开方法
	}
	
	public void addDoubleFire(){
		doubleFire+=40;
	}
	
	public int getLife(){
		return life;
	}
	public int getdoubleFire(){
		return doubleFire;
	}
	public int costLife(){
		return life--;
	}
	public int clearDoubleFire(){
		return doubleFire=0;
	}

}
