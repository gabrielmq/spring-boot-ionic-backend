package br.com.cursomc.sbinc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URLUtils {

	public static String decodeParam(String param) {
		try {
			return URLDecoder.decode(param, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String param) {
		return Arrays.asList(param.split(","))
				.stream().map( in -> Integer.parseInt(in) )
				.collect(Collectors.toList());
	}
}
