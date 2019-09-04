package com.tarena.typer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 课程阶段, 没有课程阶段对应一个 */
public class CourseStage {
	
	public static final String ENCODING = "GBK";
	
	private String name;
	private int index;
	private String comment;
	/** 全部打中的最高分 */
	private int maxScore;
	
	private List<Word> words = new ArrayList<Word>();
	
	public CourseStage() {
	}
	
	public CourseStage(File file, int index) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
		//第一行是课程阶段
		name = in.readLine().trim();
		comment = in.readLine().trim();
		String line;
		while((line = in.readLine())!=null){
			//System.out.println(line);
			if(line.trim().equals("")){
				continue;
			}
			Word word = new Word(line);
			maxScore +=word.getScore();
			words.add(word);
		}
		in.close();
		this.index = index;
	}
	
	public void shuffle(){
		Collections.shuffle(words);
	}
	
	public List<Word> getWords() {
		return words;
	}
	public int getMaxScore() {
		return maxScore;
	}
	
	public String getName() {
		return name;
	}
	
	public String getComment() {
		return comment;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		return (index+1) + ". "+ name;
	}
}
