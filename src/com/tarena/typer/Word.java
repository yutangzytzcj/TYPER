package com.tarena.typer;
/**
 * һ������ĵ���
 * @author Robin
 */
public class Word {
	private String english;
	private String chinese;
	
	public Word() {
	}
	/**
	 * ��һ�� void ��, �޷���ֵ, ����Ϊһ������
	 * @param line
	 */
	public Word(String line) {
		//System.out.println(line);
		if(line==null || line.trim().equals("")){
			throw new RuntimeException("��������Ϊnull");
		}
		line = line.trim();
		int index = line.indexOf(" ");
		if(index<0){
			english = line;
			return;
		}
		english = line.substring(0, index);
		chinese = line.substring(index+1).trim();
	}

	public Word(String english, String chinese) {
		super();
		this.english = english;
		this.chinese = chinese;
	}

	public boolean match(String english){
		if(english==null){
			return false;
		}
		return this.equals(english);
	}
	public int getScore(){
		return english.length()*10; 
	}
	public String getChinese() {
		return chinese;
	}
	public String getEnglish() {
		return english;
	}
	@Override
	public String toString() {
		return english+":"+chinese;
	}
}
