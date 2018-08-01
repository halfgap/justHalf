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

	//���ɵ��˶���
	public FlyingObject nextOne(){				//��������
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
	public void enterAction(){					//�����볡
		enterIndex++;
		if(enterIndex%40 == 0){
			FlyingObject obj = nextOne();		//��ȡ���˶���
			enemies = Arrays.copyOf(enemies, enemies.length+1);		//����
			enemies[enemies.length-1] = obj ;				//�����˶�����ӵ������ݵ�λ��
			
		}	
	}
	
	int shootIndex = 0;
	public void shootAction(){
		shootIndex++;
		if(shootIndex%25 == 0){
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length);    //����ͬʱ��֤˫�����ǵ����ӵ����ܼ�������
			//���������׷��,bs����׷�ӵ�bullets������
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
	
	//��Խ��ĵ��˴��,װ��notOutOfBounds��������
	public void outOfBoundsAction(){
		int index = 0;	//�±�,��Խ����˵ĸ���
		FlyingObject[] notOutOfBounds = new FlyingObject[enemies.length]; //��Խ���������,��ʼ������enemies������ͬ
		for(int i=0;i<enemies.length;i++){
			FlyingObject f = enemies[i];		//��ȡÿһ������
			if(!f.outOfBounds() && !f.isRemove()){
				notOutOfBounds[index] = f;	//����Խ����˼���������
				index++;
			}
		}
		enemies = Arrays.copyOf(notOutOfBounds, index);	//����Խ�����鸴�Ƶ�enemies������,����Ϊindex
		
		
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
					if(f instanceof Life){				//·�ϵ�����
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
						Enemy en = (Enemy)f;  //ǿ������ת��
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
		};//MouseAdapterΪ������,�˴�ʹ�������ڲ���
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
			
		if(state == START){
			Boss b = Boss.getIn();						//BOSS�볡,һ����
			enemies = Arrays.copyOf(enemies, enemies.length+1);		
			enemies[enemies.length-1] = b ;
		}
		Timer timer = new Timer();
		int intervel = 10;
		timer.schedule(new TimerTask(){
			public void run(){
				if(state == RUNNING){
				enterAction();	//�����볡--World
				shootAction();	//�����ӵ�--Hero
				stepAction();	//obj�ƶ�--FlyingObject
				bshootAction();
				outOfBoundsAction();	//Խ����--FlyingObject��ͨ�������������е���,bulletҪ��д
				bulletImpactAction();	//�ӵ��������ײ
				heroImpactAction();     //Ӣ�ۻ��������ײ
				checkGameOverAction();	//�ж���Ϸ����
				}
				repaint();		//�ػ�   �������
			}
		} ,intervel,intervel);		//�����ڲ���
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
		Color c = new Color(255,0,0);		//������ɫ
		g.setColor(c);
		g.setFont(new Font("Tahoma", Font.PLAIN, 24));		//��������
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
		DecoratedFrame frame = new DecoratedFrame();     //DecoratedFrame�ޱ߿�Frame
		World world = new World();
		frame.add(world);
		
		frame.setDefaultCloseOperation(DecoratedFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true); 	//1.���ô��ڿɼ�  2.�������paint����    ֮ǰ��д��paint����
		
		world.action();
	}
}



