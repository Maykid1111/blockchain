package org.fisco.bcos.asset.client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigInteger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class UiViewController extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Boolean ispain=false;
	Web3Contoller client = new Web3Contoller();
	public UiViewController() {
		super("client");
		this.setSize(600, 600);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ispain=false;
		init(this.getGraphics());
		this.setVisible(true);
	    try {
			client = new Web3Contoller();
			client.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void addTitleImage() {
		BufferedImage img =null;
		
		try {
			img = ImageIO.read(new File("./src/main/resources/111111.PNG"));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		JLabel jlabel = new JLabel(new ImageIcon(img));
		jlabel.setBounds(0, 0, img.getWidth(), img.getHeight());
		getContentPane().add(jlabel);
	}
	
	@SuppressWarnings("deprecation")
	void addBackGround() {
		((JPanel)this.getContentPane()).setOpaque(false);
	    ImageIcon img = new ImageIcon("./src/main/resources/backgr.jpg"); //添加图片
	    JLabel background = new JLabel(img); 
	    this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
	    background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
	    
	}
	
	void addGetUserBillFromUser() {
		JPanel jpanel = new JPanel();
		jpanel.setOpaque(false);
		jpanel.setLayout(new FlowLayout());
		
		JLabel jlabel=new JLabel("搜索账单:");
		jlabel.setForeground(Color.white); 
		jpanel.add(jlabel);
		
		JTextField jtextfield = new JTextField();
		jtextfield.setForeground(Color.white); 
		jtextfield.setOpaque(false);
		jtextfield.setText("//输入名称");								
		jtextfield.setFont(new Font("黑体",Font.PLAIN,15));  
		jtextfield.setColumns(15);
		jpanel.add(jtextfield);
			
		JButton jbutton = new JButton("确定");
		jpanel.add(jbutton);
		
		this.add(jpanel);
		
		JPanel jpanel_ = new JPanel();
		jpanel_.setOpaque(false);
				
		JLabel jlabel_ = new JLabel("结果： ");
		jlabel_.setForeground(Color.white); 
		jlabel_.setOpaque(false);
		jpanel_.add(jlabel_);
		
		JTextField jlabel_1 = new JTextField();
		jlabel_1.setForeground(Color.white); 
		jlabel_1.setText("");								
		jlabel_1.setFont(new Font("黑体",Font.PLAIN,15));  
		jlabel_1.setColumns(15);
		jlabel_1.setOpaque(false);
		jpanel_.add(jlabel_1);

		jbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String string = new String();
				string = client.getUserBillMoney(jtextfield.getText());
				String string_ = new String();
				//string_ = client.getUserBillFromUser(jtextfield.getText());
				string_=jtextfield.getText();
				System.out.println("user: "+string_+" account: "+string);
				jlabel_1.setText("结果：user: "+string_+" account: "+string);
			}
		});
		
		this.add(jpanel_);
	}
	
	void addPayback() {
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new FlowLayout());
		jpanel.setOpaque(false);
		
		JLabel jlabel=new JLabel("偿还: ");
		jlabel.setForeground(Color.white); 
		jlabel.setOpaque(false);
		jpanel.add(jlabel);
		
		JTextField jtextfield = new JTextField();
		jtextfield.setForeground(Color.white); 
		jtextfield.setText("//输入名字");
		jtextfield.setForeground(Color.white);    	
		jtextfield.setOpaque(false);            	
		jtextfield.setColumns(15);
		jpanel.add(jtextfield);
		
		JButton jb = new JButton("确定");
		jb.setOpaque(false);
		
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.payback(jtextfield.getText());
				System.out.println("Pay back successfully!");
			}
		});
		
		jpanel.add(jb);
		this.add(jpanel);
	}
	
	public void init(Graphics g) {
		this.setLayout(new GridLayout(7,1));
		
		addTitleImage();
		addTransfer();
		addPayback();
		addGetUserBillFromUser();
		addDeploy();
		addBackGround(); 
	}
	
	void addDeploy() {
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new FlowLayout());
		jpanel.setOpaque(false);
		
		JButton jbutton = new JButton("部署合约");
		jbutton.setOpaque(false);
		
		jbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Deploying......");
				client.deployAsset();
			}
		});
		
		jpanel.add(jbutton);
		this.add(jpanel);
	}
	
	void addTransfer() {
		JPanel jpanel = new JPanel();
		jpanel.setOpaque(false);	
		
		JLabel jlabel1 = new JLabel("用户: ");
		jlabel1.setForeground(Color.white); 
		jlabel1.setOpaque(false);
		JTextField jtextfield1 = new JTextField("//输入名称");
		jtextfield1.setForeground(Color.white); 
		jtextfield1.setOpaque(false);
		jtextfield1.setColumns(10);
		
		jpanel.add(jlabel1);
		jpanel.add(jtextfield1);
		
		JLabel jlabel2 = new JLabel("给用户: ");
		jlabel2.setForeground(Color.white); 
		jlabel2.setOpaque(false);
		
		JTextField jtextfield2 = new JTextField("//输入名称");
		jtextfield2.setForeground(Color.white); 
		jtextfield2.setOpaque(false);
		jtextfield2.setColumns(10);
		
		jpanel.add(jlabel2);
		jpanel.add(jtextfield2);
		
		JLabel jlabel3 = new JLabel("转账数值: ");
		jlabel3.setForeground(Color.white); 
		jlabel3.setOpaque(false);
		
		JTextField jtextfield3 = new JTextField("//输入数值");
		jtextfield3.setForeground(Color.white); 
		jtextfield3.setOpaque(false);
		jtextfield3.setColumns(10);
		
		jpanel.add(jlabel3);
		jpanel.add(jtextfield3);
		
		JButton jbutton = new JButton("确定");
		
		jbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Transfer "+jtextfield3.getText()+" from "+jtextfield1.getText()+" to "+jtextfield2.getText());
				client.tranBillsFromToNumber(jtextfield1.getText(), jtextfield2.getText(),new BigInteger(jtextfield3.getText()));
			}
		});
		
		jpanel.add(jbutton);
		this.add(jpanel);
	}
	
	
	public static void main(String[] args) {
		new UiViewController();
	}
}
