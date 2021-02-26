package cn.zmdxd.xddesign.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {

	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
		    String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	//验证手机号是否符合格式
	public static boolean isMobile(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[0-9]))\\d{8}$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 电话号码验证
	 *
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m  = null;
		boolean b  = false;
		// 验证带区号的
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");
		// 验证没有区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}
	
	/*public static void main(String[] args) {
		boolean f = checkMobileNumber("18214512");
		boolean f2 = checkEmail("~aotao0918@outlookcom");
		System.out.println(f);
		System.out.println(f2);
	}
	*/
}
