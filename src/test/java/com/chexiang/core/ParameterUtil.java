package com.chexiang.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

/**
 * @ClassName: ParameterUtil
 * @Description: 对字符串或外部文本进行处理，参数绑定等
 * @author 手慢无
 * @date 2015-09-24
 */
public class ParameterUtil {

	private Random random;

	protected Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	public ParameterUtil() {
		random = new Random();
	}

	/**
	 * @Title: subsitute
	 * @Description: 原始字符串数据绑定
	 * @param origin：原始字符串,root：插值数据源
	 * @return String 绑定后的字符串
	 * @date 2015-08-27
	 */
	public String subsitute(String origin, JsonObject root) {
		StringBuffer buffer = new StringBuffer();
		// group(0)取值包含${}的整个字符串，group(1)取值${}内部字符串
		// 匹配替换，全都包含${}
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(origin);
		while (matcher.find()) {
			// group(1)取${}内部变量名
			// 替换整个${}标记
			matcher.appendReplacement(buffer, getValueFromRoot(matcher.group(1), root));
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	public String subsitute(String origin) {
		return subsitute(origin, null);
	}

	/**
	 * @Title: getValueFromRoot
	 * @Description: 插值替换的具体实现
	 * @param origin：interpolate：变量/函数名；root：插值数据源
	 * @return String value
	 * @date 2015-09-11
	 */
	private String getValueFromRoot(String interpolate, JsonObject root) {
		// 正则式：匹配括号内部分
		Pattern pattern = Pattern.compile("(.*?)\\((.*?)\\)");
		Matcher matcher = pattern.matcher(interpolate);
		if (matcher.find()) {
			// 分支1：参数是方法体
			try {
				// 用正则表达式进行分离，group(1)提取方法名，group(2)提取参数
				return (String) getClass().getDeclaredMethod(matcher.group(1), String.class).invoke(this,
						matcher.group(2));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				logger.error("反射执行方法体出错");
				e.printStackTrace();
				return null;
			}
		} else {
			// 分支2：参数是对象属性
			return JsonUtil.getElementFromJson(root, interpolate.split("\\."));
			// return getValueFromRoot(interpolate, root);
		}

	}

	/**
	 * @Title: RandCharAndNum
	 * @Description: 随机字符串生成器
	 * @param origin：length:长度;int:模式;excludes:排除
	 * @return String
	 * @date 2015-08-28
	 * 
	 *       flag:-1:大写字母,0:数字,1:大写字母和数字,9:中文,其他:数字和大小写字母;
	 * 
	 *       实现原理：
	 * 
	 *       1.在asc码表中，数字位于[48,57]区域,大写字母位于[65,90]区域,小写字母位于[97,122]区域，中文位于[19968
	 *       ,40892]区域
	 * 
	 *       2.random随机数为连续区间的平均分布，现重新定义码表坐标，规定数字、大写、小写、中文分别位于[0,9]/[10,35]/[36,
	 *       61]/[62 ,20986]
	 * 
	 *       3.分段函数Asc2Char:实现连续区间码表和真实ascii码表之间的映射
	 */
	public String RandCharAndNum(int length, int flag, char... excludes) {
		// Arrays.sort(excludes);
		String val = "";
		int asc = 0;

		for (int i = 0; i < length; i++) {
			do {
				switch (flag) {
				case -1:
					// 大写
					// [10,36)即[10,35]
					asc = this.random.nextInt(26) + 10;
					break;
				case 0:
					// 仅数字
					// [0,9]
					asc = this.random.nextInt(10);
					break;
				case 1:
					// 数字和大写字母
					// [0,35]
					asc = this.random.nextInt(36);
					break;
				case 9:
					// 汉字
					// [62 ,20986]
					asc = this.random.nextInt(20925) + 62;
					break;
				default:
					// 数字和大小写字母
					// [0,61]
					asc = this.random.nextInt(62);
					break;
				}
				// 如果生成的数字在排除列表中，则重新抽样，对外层for计数器不影响
			} while (Arrays.asList(excludes).contains(Asc2Char(asc)));
			// 字符串拼接
			val += (char) Asc2Char(asc);
		}
		return val;
	}

	String RandCharAndNum(String args) {
		String[] paras = args.split(",");
		return RandCharAndNum(Integer.parseInt(paras[0]), Integer.parseInt(paras[1]));
	}

	/**
	 * @Title: Asc2Char
	 * @Description: 分段映射函数
	 * @param x：自变量
	 * @return int 应变量
	 * @date 2015-08-28
	 * 
	 *       将自变量区间[0,9]/[10,35]/[36,61]/[62
	 *       ,20986]一一映射到[48,57]/[65,90]/[97,122]/[19968 ,40892]
	 */
	private int Asc2Char(int x) {
		if (x < 10) {
			return x + 48;
		} else if (x >= 10 && x < 36) {
			return x + 55;
		} else if (x >= 36 && x < 62) {
			return x + 61;
		} else {
			return x + 19906;
		}
	}
}
