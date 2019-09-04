package com.tarena.typer;

public class Bullet {
	int x, y;
	Enemy enemy;
	public Bullet(Enemy enemy) {
		this.enemy = enemy;
		this.x = enemy.x + 10;
		y = Typer.HEIGHT;
	}
	
	public void step(){
		y--;
	}
}
