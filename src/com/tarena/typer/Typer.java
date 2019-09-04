package com.tarena.typer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Typer extends JPanel {
	/** ����ͼƬ����ѡ�� 1 ���� 0 */
	public static final int THEME = 1;
	
	public static final int WIDTH = 805;
	public static final int HEIGHT = 550;
	
	public static final String COURSE_PATH = "dic";
	 
	private int state;
	private static final int START=0;
	private static final int RUNNING=1;
	private static final int PAUSE=2;
	private static final int GAMEOVER=3;
	
	private int score;
	
	private static BufferedImage logo ;
	private static BufferedImage bg ;
	private static BufferedImage bullet ;
	private static BufferedImage pause ;
	private static BufferedImage gameover ;
	private static BufferedImage start ;

	/** ��ѡ�γ��б� */
	private CourseStage[] courseStateList;//= new ArrayList<CourseStage>();

	/** ��ǰѡ���Ŀγ� */
	private CourseStage currentCourse;
	
	/** ���ڴ�ĵ����б�, ÿ���һ��, ���߳���һ����ɾ��һ��, ��������б�һ���ǿγ̵����б�ĸ���! */
	private LinkedList<Word> words;
	/** û�д��еĵ����б� */
	private List<Word> missing = new ArrayList<Word>();
	
	/** ��������ĵ��� */
	private Set<Enemy> enemies = new HashSet<Enemy>();
	private Set<Bullet> bullets = new HashSet<Bullet>();

	/** ���ֻ�����, ����ǰ�û����������, ��Ϊ�ж��̲߳����������, ����ʹ��StringBuffer,  */
	private StringBuffer line;
	
	/** ˢ�¶�ʱ�� */
	private Timer timer;
	/** ����ʱ����� */
	private int timerInterval = 1000/120;
	
	public static final int FONT_SIZE = 16;
	public static final int FONT_COLOR = 0xeeeeee;//��ɫ 
	
	public Typer() {
		line = new StringBuffer();
		//line.append("Buffered");
		loadCourseStage();
		//createCourseStagePanel();
		try {
			logo = ImageIO.read(getClass().getResource(THEME+"typer.png"));
			bg = ImageIO.read(getClass().getResource(THEME+"bg.png"));
			bullet = ImageIO.read(getClass().getResource(THEME+"bullet.png"));
			pause = ImageIO.read(getClass().getResource(THEME+"pause.png"));
			start = ImageIO.read(getClass().getResource(THEME+"start.png"));
			gameover = ImageIO.read(getClass().getResource(THEME+"gameover.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCourseStage(){
		try{
			File dir = new File(COURSE_PATH);
			File[] files = dir.listFiles();
			Arrays.sort(files);
			courseStateList = new CourseStage[files.length];
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				//System.out.println(file); 
				courseStateList[i] = new CourseStage(file, i);
			}
			currentCourse = courseStateList[0];
		}catch(IOException e){
			e.printStackTrace();
			throw new  RuntimeException(e);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(bg, 0, 0, null);
		paintEnemies(g);
		paintBullets(g);
		paintText(g);
		paintState(g);
	}
	
	private void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAMEOVER:
			g.drawImage(gameover, 0, 0, null);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
			
			int x = 220+1;
			int y = 170+1;
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("����: "+score, x, y);
			y+=48;
			g.drawString("����: "+(currentCourse.getWords().size()-missing.size()), x, y);
			y+=48;
			g.drawString("���: "+(missing.size()), x, y);
			y+=48;
			g.drawString("���: "+(currentCourse.getMaxScore()), x, y);
			y+=48;
			g.drawString("[S] ���¿�ʼ", x, y);
			
			x = 220;
			y = 170;
			g.setColor(Color.BLACK);
			g.drawString("����: "+score, x, y);
			y+=48;
			g.drawString("����: "+(currentCourse.getWords().size()-missing.size()), x, y);
			y+=48;
			g.drawString("���: "+(missing.size()), x, y);
			y+=48;
			g.drawString("���: "+(currentCourse.getMaxScore()), x, y);
			y+=48;
			g.drawString("[S] ���¿�ʼ", x, y);
			
			break;
		}
	}
	/** ���Ʒ�����Ϣ */
	private void paintText(Graphics g) {
		int x = 18;
		int y = 36;
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.gray);
		g.drawString("SCORE:"+score, x+1, y+1);
		g.setColor(new Color(0xeeeeee));
		g.drawString("SCORE:"+score, x, y);
		
		//�����û�������Ϣ
		x = 270;
		y = 475;
		String s = this.line.toString();
		font = new Font(Font.SERIF, Font.BOLD, 35);
		g.setFont(font);
		g.setColor(Color.gray);
		g.drawString(s, x+1, y+1);
		g.setColor(Color.black);
		g.drawString(s, x, y);
	}

	private  void paintBullets(Graphics g) {
		synchronized (bullets) {
			Font font = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
			g.setFont(font);
			g.setColor(Color.white);
			for (Bullet  b : bullets) {
				g.drawImage(bullet, b.x, b.y, null);
				//g.drawString(b.enemy.getWord().getChinese(), b.x+25, b.y+FONT_SIZE+8);
			}
		}
	}

	private void paintEnemies(Graphics g) {
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
		synchronized (enemies) {
			for (Enemy e : enemies) {
				Word word = e.getWord();
				g.setFont(font);
				//������Ӱ
				g.setColor(Color.gray);
				g.drawString(word.getEnglish(), e.x+1, e.y+1 );
				g.setColor(new Color(FONT_COLOR));
				g.drawString(word.getChinese(), e.x, e.y - FONT_SIZE);
				if(e.isMatched()){
					g.setColor(Color.red);
				}
				g.drawString(word.getEnglish(), e.x, e.y );
				//g.drawImage(e.image, e.x, e.y, null); 
				String input = line.toString();
				g.setColor(Color.yellow);
				if( !e.isMatched() &&  word.getEnglish().startsWith(input)){
					g.drawString(input, e.x, e.y);
				}
				//g.drawString(e.toString(), e.x, e.y);
			}
		}
	}

	public void action() {
		state = START;
		startAction();
		timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(state==RUNNING){
					enemyEnterAction();
					shootBulletAction();
					enemyStepAction();
					bulletStep(); 
					score += bangBangAction();
					outBoundAction();
					if(words.isEmpty() && enemies.isEmpty()){
						state = GAMEOVER;
					}
				}
				//repaint();
			}
		}, 0, timerInterval);
		
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				repaint();
			}
		}, 0, 1000/24);
		
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				char ch = e.getKeyChar();
				switch(state){
				case RUNNING:
					runningKeyAction(key, ch);
					break;
				case PAUSE:
					pauseKeyAction(key);
					break;
				case GAMEOVER:
					gameoverKeyAction(key);
					break;
				}
			}
			

		});
		this.setFocusable(true);
		this.requestFocus();
	}
	
	protected void gameoverKeyAction(int key) {
		if(key==KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		if(key == KeyEvent.VK_S){
			state = START;
			startAction();
			return;
		}
	}

	private void startAction() {
		currentCourse = (CourseStage) JOptionPane.showInputDialog(this, "�γ̽׶�",
				"ѡ��", JOptionPane.PLAIN_MESSAGE, new ImageIcon(logo),
				courseStateList, currentCourse);
		if (currentCourse == null) {
			System.exit(0);
		}
		state = RUNNING;
		this.score = 0;
		this.bullets.clear();
		this.enemies.clear();
		this.index = 0;
		this.line = new StringBuffer();
		this.missing.clear();
		words = new LinkedList<Word>(currentCourse.getWords());
		Collections.shuffle(words);
		//System.out.println(words);

	}

	protected void pauseKeyAction(int key) {
		if(key == KeyEvent.VK_C){
			state = RUNNING;
			return;
		}
		
		if(key==KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}

	private void runningKeyAction(int key, char ch) {
		if(key == KeyEvent.VK_F1){
			state = PAUSE;
			return;
		}
		
		if(key==KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		if(key == KeyEvent.VK_BACK_SPACE && line.length()>0){
			//System.out.println(line); 
			line.deleteCharAt(line.length()-1);
			return;
		}				
		
		//System.out.println(key + ","+KeyEvent.VK_BACK_SPACE); 
		if(Character.isLetterOrDigit(ch) || ch=='-' || ch=='_' || ch=='$' || ch=='.'){
			//System.out.println(ch);
			line.append(ch);
		}
	}
	
	protected void outBoundAction() {
		for (Iterator<Enemy> i = enemies.iterator(); i.hasNext();) {
			Enemy enemy = (Enemy) i.next();
			if(enemy.outOfBounds()){
				i.remove();
				missing.add(enemy.getWord());
			}
		}
	}

	protected void bulletStep() {
		for (Bullet b : bullets) {
			b.step();
		}
	}

	protected void enemyStepAction() {
		for (Enemy e : enemies) {
			e.step();
		}
	}

	protected int bangBangAction() {
		synchronized (bullets) {
			for (Iterator<Bullet> i = bullets.iterator(); i.hasNext();) {
				Bullet bullet = (Bullet) i.next();
				int score = shootOneEnemy(bullet);
				if(score>0){
					i.remove();
					return score;
				}
			}
		}
		return 0;
	}
	public int shootOneEnemy(Bullet bullet){
		int score = 0;
		for (Iterator<Enemy> i = enemies.iterator(); i.hasNext();) {
			Enemy enemy = (Enemy) i.next();
			if(enemy.shootBy(bullet)){
				i.remove();
				score += enemy.getScore();
				break;
			}
		}
		return score;
	}
	/** ���˽�������� */
	private int index = 0;
	/** ÿ300��ʱ��������һ������ ÿ��ʱ������ 1/60 �� Ҳ���� 5����һ�����ʽ��� */
	private int enemyEnterInterval = 400;
	protected void enemyEnterAction() {
		if(words.isEmpty()){
			return;
		}
		synchronized (enemies) {
			if(index++%enemyEnterInterval==0){
				Word word = words.removeFirst();
				enemies.add(new Enemy(word));
			}
		}
	}

	protected void shootBulletAction() {
		String word = line.toString();
		synchronized (enemies) {
			for (Enemy enemy : enemies) {
				if(enemy.sameWord(word)){
					line = new StringBuffer();
					bullets.add(new Bullet(enemy));
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("��Ϸ��ʼ");
		JFrame frame = new JFrame("���  [F1]pause");
		frame.setSize(WIDTH, HEIGHT);
		Typer typer = new Typer();
		frame.add(typer);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(logo);
		frame.setResizable(false);
		frame.setVisible(true);
	     
		typer.action();
		System.out.println("game over");
		 
		while(true) {
			System.out.println(new Date());
		}
	
	}
}
