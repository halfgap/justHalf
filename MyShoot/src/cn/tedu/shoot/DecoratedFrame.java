package cn.tedu.shoot;
//无边框JFrame
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;

public class DecoratedFrame extends JFrame { 
public DecoratedFrame() { 
this.getContentPane().add(new JLabel("Just a test.")); 
this.setUndecorated(true); // 去掉窗口的装饰 
this.getRootPane().setWindowDecorationStyle(JRootPane.NONE); //采用指定的窗口装饰风格 
this.setSize(300,150); 
} 
public static void main(String[] args) { 
JFrame frame = new DecoratedFrame(); 
frame.setVisible(true); 
} 
}
