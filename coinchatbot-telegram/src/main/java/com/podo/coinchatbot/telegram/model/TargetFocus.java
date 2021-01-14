package com.podo.coinchatbot.telegram.model;

import com.podo.coinchatbot.core.Lang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TargetFocus {
	UP("이상", "MORE"),
	DOWN("이하", "LESS");

	private final String kr;
	private final String en;

	public String kr(){
		return kr;
	}

	public String en(){
		return en;
	}

	public String getStr(Lang lang) {
		switch(lang) {
		case KR : return kr;
		case EN: return en;
		}

		return "";
	}

}
