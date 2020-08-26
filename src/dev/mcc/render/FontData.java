package dev.mcc.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FontData {
	private HashMap<Character, FontCharData> charDatas = new HashMap<Character, FontCharData>();
	public FontData(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String line;
			while((line = br.readLine()) != null) {
				FontCharData curFontData = new FontCharData();
				char curChar = 0;
				String[] toks1 = line.split("\\s+");
				for(String str : toks1) {
					String[] toks = str.split("\\=");
					if(toks.length == 2) {
						if(toks[0].equals("id")) {
							curChar = (char)Integer.parseInt(toks[1]);
						}else if(toks[0].equals("x")) {
							curFontData.x = Integer.parseInt(toks[1]);
						}else if(toks[0].equals("y")) {
							curFontData.y = Integer.parseInt(toks[1]);
						}else if(toks[0].equals("width")) {
							curFontData.width = Integer.parseInt(toks[1]);
						}else if(toks[0].equals("height")) {
							curFontData.height = Integer.parseInt(toks[1]);
						}else if(toks[0].equals("xoffset")) {
							curFontData.xOffset = Integer.parseInt(toks[1]);
						}else if(toks[0].equals("yoffset")) {
							curFontData.yOffset = Integer.parseInt(toks[1]);
						}else if(toks[0].equals("xadvance")) {
							curFontData.xAdvance = Integer.parseInt(toks[1]);
						}
					}
				}
				if(curChar != 0) {
					charDatas.put(curChar, curFontData);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public FontCharData get(char c) {
		if(charDatas.containsKey(c)) {
			return charDatas.get(c);
		}
		return null;
	}
}
