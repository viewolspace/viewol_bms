package com.youguu.shiro.service.impl;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class INI4j {

	/**
	 * 用linked hash map 来保持有序的读取
	 */
	final LinkedHashMap<String, LinkedHashMap<String, String>> coreMap = new LinkedHashMap<>();
	/**
	 * 当前Section的引用
	 */
	String currentSection = null;

	public INI4j(File file) throws FileNotFoundException {
		this.init(new BufferedReader(new FileReader(file)));
	}

	public INI4j(String path) throws FileNotFoundException {
		this.init(new BufferedReader(new FileReader(path)));
	}

	public INI4j(ClassPathResource source) throws IOException {
		this(source.getFile());
	}

	void init(BufferedReader bufferedReader) {
		try {
			read(bufferedReader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IO Exception:" + e);
		}
	}

	void read(BufferedReader reader) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			parseLine(line);
		}
	}

	void parseLine(String line) {
		line = line.trim();
		// 此部分为注释
		if (line.matches("^\\#.*$")) {
			return;
		} else if (line.matches("^\\[\\S+\\]$")) {
			String section = line.replaceFirst("^\\[(\\S+)\\]$", "$1");
			addSection(section);
		} else if (line.matches("^\\S+=.*$")) {
			int i = line.indexOf("=");
			String key = line.substring(0, i).trim();
			String value = line.substring(i + 1).trim();
			addKeyValue(currentSection, key, value);
		}
	}


	void addKeyValue(String currentSection, String key, String value) {
		if (!coreMap.containsKey(currentSection)) {
			return;
		}
		Map<String, String> childMap = coreMap.get(currentSection);
		childMap.put(key, value);
	}

	void addSection(String section) {
		if (!coreMap.containsKey(section)) {
			currentSection = section;
			LinkedHashMap<String, String> childMap = new LinkedHashMap<String, String>();
			coreMap.put(section, childMap);
		}
	}

	public String get(String section, String key) {
		if (coreMap.containsKey(section)) {
			return get(section).containsKey(key) ? get(section).get(key) : null;
		}
		return null;
	}


	public Map<String, String> get(String section) {
		return coreMap.containsKey(section) ? coreMap.get(section) : null;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> get() {
		return coreMap;
	}

}

