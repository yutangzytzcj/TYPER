package com.tarena.typer;

import java.util.Random;
/**
 *  飞行的敌人<br>
 *  +----width----+<br> 
 *  |整数          |<br>
 *  |int          |<br>
 *  +-------------+<br>
 * 左下角是 x,y<br>
 * 宽度width是 字符大小*4<br>
 * 高度height是 字符大小*2<br>
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
	 * 只有没有匹配过的单词才可以匹配
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
