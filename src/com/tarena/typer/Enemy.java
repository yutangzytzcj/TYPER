package com.tarena.typer;

import java.util.Random;
/**
 *  ���еĵ���<br>
 *  +----width----+<br> 
 *  |����          |<br>
 *  |int          |<br>
 *  +-------------+<br>
 * ���½��� x,y<br>
 * ���width�� �ַ���С*4<br>
 * �߶�height�� �ַ���С*2<br>
 * 
 */
public class Enemy {
	private Word word;
	private boolean matched;
	//BufferedImage image;
	int x, y, width, height;
	
	private int index = 0;
	private int speed;
	
	public Enemy(Word word) {
		this.word = word;
		width = Math.max(Typer.FONT_SIZE/2 * word.getEnglish().length(), Typer.FONT_SIZE*2);
		height =  Typer.FONT_SIZE * 2;
		Random random = new Random();
		x = random.nextInt(Typer.WIDTH-width);
		y = 0;
		speed = random.nextInt(9)+3;
	}
	/**
	 * ֻ��û��ƥ����ĵ��ʲſ���ƥ��
	 * @param word
	 * @return
	 */
	public boolean sameWord(String word){
		if(! matched){
			return matched = this.word.getEnglish().equals(word);
		}
		return false;
	} 
	
	public boolean shootBy(Bullet bullet){
		return  bullet.enemy.equals(this) && bullet.x > x && bullet.x<x+width && bullet.y>y-height && bullet.y<y;
	}
	
	public void step(){
		index++;
		if(index%speed==0)
			y++;
	}
	public boolean isMatched() {
		return matched;
	}
	public boolean outOfBounds(){
		return y>Typer.HEIGHT;
	}
	
	public Word getWord() {
		return word;
	}
	
	public int getScore() {
		return word.getScore();
	}
	@Override
	public String toString() {
		return word.toString();
	}
}
