package cn.tedu.shoot;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
public class World extends JPanel {
	public static final int WIDTH = 700;
	public static final int HEIGHT = 1050;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int  state = START;
	
	private static BufferedImage start ;
	private static BufferedImage pause;
	private static BufferedImage gameover;
	static{
		start = FlyingObject.loadImage("start.png");
		pause = FlyingObject.loadImage("pause.png");
		gameover = FlyingObject.loadImage("gameover.png");
	}
	
	private Sky sky = new Sky();
	private Hero hero = new Hero();
	private FlyingObject[] enemies = {};    			 
	private Bullet[] bullets = {};	

	//生成敌人对象
	public FlyingObject nextOne(){				//工厂方法
		Random rand = new Random();
		int type = rand.nextInt(100);
		if(type<2){
			return new Boss();
		}else if(type<15){
			return new Bee();
		}else if(type<50){
			return new Airplane();
		}else if(type<80){
			return new BigAirplane();
		}else if(type<90){
			return new Roadblock();
		}else{
			return new Meteorite();
		}
		
	}
	
	int enterIndex = 0;
	public void enterAction(){					//敌人入场
		enterIndex++;
		if(enterIndex%40 == 0){
			FlyingObject obj = nextOne();		//获取敌人对象
			enemies = Arrays.copyOf(enemies, enemies.length+1);		//扩容
			enemies[enemies.length-1] = obj ;				//将敌人对象添加到给扩容的位置
			
		}	
	}
	
	int shootIndex = 0;
	public void shootAction(){
		shootIndex++;
		if(shootIndex%25 == 0){
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length);    //扩容同时保证双倍或是单倍子弹都能加入数组
			//两个数组的追加,bs数组追加到bullets数组中
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);  
		}
	}
	
	public void stepAction(){
		sky.step();
		for(int i=0;i<enemies.length;i++){
			enemies[i].step();	
		}
		for(int i=0;i<bullets.length;i++){
			bullets[i].step();
		}
	}
	
	//将越界的敌人打包,装入notOutOfBounds的数组中
	public void outOfBoundsAction(){
		int index = 0;	//下标,不越界敌人的个数
		FlyingObject[] notOutOfBounds = new FlyingObject[enemies.length]; //不越界敌人数组,初始长度与enemies数组相同
		for(int i=0;i<enemies.length;i++){
			FlyingObject f = enemies[i];		//获取每一个敌人
			if(!f.outOfBounds() && !f.isRemove()){
				notOutOfBounds[index] = f;	//将不越界敌人加入数组中
				index++;
			}
		}
		enemies = Arrays.copyOf(notOutOfBounds, index);	//将不越界数组复制到enemies数组中,长度为index
		
		
		index = 0;
		Bullet[] notOutBs = new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds() && !b.isRemove()){
				notOutBs[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(notOutBs, index);
		

	}
	
	
	
	
	private int score = 0;
	public void bulletImpactAction(){
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];
			for(int j=0;j<enemies.length;j++){
				FlyingObject f = enemies[j];
				if(b.isLife() && f.isLife() && f.hit(b)){
					if(f instanceof Life){				//路障的生命
						Life l = (Life)f;
						if(l.getHp()>0){
							l.costLife();
						}else{
							l.goDead();
						}
					}else{
						f.goDead();
					}
					b.goDead();
					
					if(f instanceof Enemy){
						Enemy en = (Enemy)f;  //强制类型转换
						score += en.getScore();
					}
					if(f instanceof Award){
						Award a = (Award)f;
						int type = a.getAwardType();
						switch(type){
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire();
							break;
						case Award.LIFE:
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}
	
	int bshootindex=0;
	public void bshootAction(){
		bshootindex++;
		if(bshootindex%40==0){
			for(int i=0;i<enemies.length;i++){
				if(enemies[i] instanceof Roadblock&&enemies[i].isLife()){
					Roadblock be=(Roadblock) enemies[i];
			Random rand=new Random();
			int type=rand.nextInt(8);
			if(type<2){
			Bullte b=be.bshoot();
			
			enemies=Arrays.copyOf(enemies,enemies.length+1);
			enemies[enemies.length-1]=b;
			if(b.outOfBounds()){
				b.goDead();
			}
			}
				}
				if(enemies[i] instanceof Boss&&enemies[i].isLife()){
					Boss be=(Boss) enemies[i];
			Random rand=new Random();
			int type=rand.nextInt(8);
			if(type<4){
			Bullte b=be.bshoot();
			
			enemies=Arrays.copyOf(enemies,enemies.length+1);
			enemies[enemies.length-1]=b;
			if(b.outOfBounds()){
				b.goDead();
			}
			}
				}
	}
		}
	}
	
	
	public void heroImpactAction(){
		for(int i=0;i<enemies.length;i++){
			FlyingObject f = enemies[i];
			if(f.isLife() && hero.hit(f) && hero.isLife()){
				if(f instanceof Boss){
					Boss b =(Boss)f;
					b.costLife();
					hero.costLife();
					hero.clearDoubleFire();
					break;
				}else{
				f.goDead();
				hero.costLife();
				hero.clearDoubleFire();
				break;
				}
			}
		}
	}
	
	public void checkGameOverAction(){
		if(hero.getLife()<=0){
			state = GAME_OVER;
		}
	}
	
	public void action(){
		MouseAdapter l = new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
			if(state == RUNNING){
				int x = e.getX();
				int y = e.getY();
				hero.moveTo(x, y);
				}	
			}
			public void mouseClicked(MouseEvent e){
				switch(state){
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					sky = new Sky();
					hero = new Hero();
					enemies = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			public void mouseExited(MouseEvent e){
				if(state==RUNNING){
					state = PAUSE;
				}
			}
			public void mouseEntered(MouseEvent e){
				if(state==PAUSE){
					state = RUNNING;
				}
			}
		};//MouseAdapter为抽象类,此处使用匿名内部类
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
			
		if(state == START){
			Boss b = Boss.getIn();						//BOSS入场,一次性
			enemies = Arrays.copyOf(enemies, enemies.length+1);		
			enemies[enemies.length-1] = b ;
		}
		Timer timer = new Timer();
		int intervel = 10;
		timer.schedule(new TimerTask(){
			public void run(){
				if(state == RUNNING){
				enterAction();	//敌人入场--World
				shootAction();	//发射子弹--Hero
				stepAction();	//obj移动--FlyingObject
				bshootAction();
				outOfBoundsAction();	//越界检查--FlyingObject普通方法适用于所有敌人,bullet要重写
				bulletImpactAction();	//子弹与敌人碰撞
				heroImpactAction();     //英雄机与敌人碰撞
				checkGameOverAction();	//判断游戏结束
				}
				repaint();		//重画   无需理解
			}
		} ,intervel,intervel);		//匿名内部类
	}
	
	public void paint(Graphics g){
		sky.paintObject(g);
		hero.paintObject(g);
		for(int i=0;i<enemies.length;i++){
			enemies[i].paintObject(g);
		}
		for(int i=0;i<bullets.length;i++){
			bullets[i].paintObject(g); 
		}
		Color c = new Color(255,0,0);		//设置颜色
		g.setColor(c);
		g.setFont(new Font("Tahoma", Font.PLAIN, 24));		//设置字体
		g.drawString("SCORE: "+score,10,25);
		g.drawString("LIFE: "+hero.getLife(),10,45);
		g.drawString("DOUBLEFIRE:"+hero.getdoubleFire(), 10,65);
		switch(state){
			case START:
				g.drawImage(start,150,50,null);
				break;
			case PAUSE:
				g.drawImage(pause, 250,400, null);
				break;
			case GAME_OVER:
				g.drawImage(gameover, 250,400, null);
				break;
		}
		for(int i=0;i<enemies.length;i++){
			FlyingObject f = enemies[i];
			if(f instanceof Boss && f.isLife()){
				Boss b = (Boss)f;
				Graphics2D g2=(Graphics2D) g;
				Rectangle2D r2=new Rectangle2D.Double(b.x, b.y-b.height/2, b.width, 20);
				g2.setColor(Color.YELLOW);
				g2.draw(r2);
				Rectangle2D r=new Rectangle2D.Double(b.x+1, b.y-b.height/2+1, b.width*((double)b.getHp()/100)-1, 19);
				g2.setColor(Color.RED);
				g2.fill(r);
			}
		}
		


	}
	
	public static void main(String[] args) {
		DecoratedFrame frame = new DecoratedFrame();     //DecoratedFrame无边框Frame
		World world = new World();
		frame.add(world);
		
		frame.setDefaultCloseOperation(DecoratedFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true); 	//1.设置窗口可见  2.尽快调用paint方法    之前重写了paint方法
		
		world.action();
	}
}



