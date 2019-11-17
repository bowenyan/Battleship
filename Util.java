/**
 * Tools class
 * @author Bowen Yan
 * @version May, 2008
 */

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	private Util() {}
	
	public static void log(String msg) {
		System.out.println(msg);
	}
	
	public static void log(Throwable t, String msg) {
//		t.printStackTrace();
		System.out.println(msg);
	}
	
	/**
	 * remove the hidden chars in the left of string
	 * @param str
	 * @return
	 */
	public static String trimLeft(String str) {
		if(str == null) return null;
		char[] chs = str.toCharArray();
		int i = 0;
		for(char ch : chs) {
			if(ch == ' ' || ch == '\n' || ch == '\t' 
			|| ch == '\b' || ch == '\f' || ch == '\r') {
				i++;
			} else {
				break;
			}
		}
		if(i > 0) str = String.valueOf(chs, i, chs.length - i);
		
		return str;
	}
	
	/**
	 * remove the hidden chars on the right of string
	 * @param str
	 * @return
	 */
	public static String trimRight(String str) {
		if(str == null) return null;
		char[] chs = str.toCharArray();
		int i = chs.length - 1;
		for(; i >= 0 ; i--) {
			char ch = chs[i];
			if(ch != ' ' && ch != '\n' && ch != '\t' 
			&& ch != '\b' && ch != '\f' && ch != '\r') {
				break;
			}
		}
		if(i < chs.length - 1) str = String.valueOf(chs, 0, i + 1);
		
		return str;
	}
	
	/**
	 * check whether the string is integer or not
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("(^-?[1-9]+\\d*$)|0|-0", 0);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * check IP address
	 * @param str
	 * @return
	 */
	public static boolean isIP(String str) {
		Pattern pattern = Pattern.compile("(^((\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]).){3}(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$)|(^((\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]).){5}(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$)", 0);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * get transparent colour
	 * @param c
	 * @param alpha
	 * @return
	 */
	public static Color getTransparentColor(Color c, float alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(),
				(int) (255 * alpha));
	}
}
