package com.chexiang.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @ClassName: JsonUtil
 * @Description: 对json格式的request和response的处理
 * @author 手慢无
 * @date 2015-09-24
 */
public class JsonUtil {

	private JsonObject request, actual, expect;
	private JsonElement perfix;
	private JsonParser parser = new JsonParser();
	private Gson gson = new Gson();
	private ReportUtil report = ReportUtil.getInstance();
	private Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

	/**
	 * @param <T>
	 * @return T
	 * 
	 *         将实际值json反序列化成需要的类型
	 */
	public <T> T getActual(Class<T> T) {
		return gson.fromJson(actual, T);
	}

	/**
	 * @param <T>
	 * @return T
	 * 
	 *         将实际值json反序列化成需要的类型
	 */
	public <T> T getPerfix(Class<T> T) {
		return (null == perfix) ? null : gson.fromJson(perfix, T);
	}

	/**
	 * @return String:response
	 */
	public String actual2String() {
		return actual.toString();
	}

	/**
	 * @return String:expected
	 */
	public String expect2String() {
		return expect.toString();
	}

	/**
	 * @return String:request
	 */
	public String request2String() {
		return request.toString();
	}

	/**
	 * @Title: getElementFromJson
	 * @Description: 输入键，逐级递归获取值
	 * @param keys
	 * @return String:value
	 * @date 2015-07-29
	 */
	public static String getElementFromJson(JsonObject j, String... keys) {
		// JsonElement e;
		for (String key : keys) {

			if (j.get(key).isJsonObject()) {
				j = j.get(key).getAsJsonObject();
			} else {
				return j.get(key).getAsString();
			}
		}
		return null;
	}

	/**
	 * @param strResponse
	 * 
	 *            设置actual
	 */
	public void setActual(String strResponse) {
		actual = parser.parse(strResponse).getAsJsonObject();
		// 在venus系统中，result会多加一个单引号，使之成为一个字符串，因此需要再次序列化
		if (actual.has("result") && actual.get("result").isJsonPrimitive()) {
			// 分支：存在result，并且result是标量，即，result是字符串
			// 据此可以判断，result被单引号包裹，需要进行再次序列化
			this.actual.add("result", parser.parse(actual.get("result").getAsString()));
		}
		logger.trace("expect response：" + expect2String());
		logger.trace("actual response：" + actual2String());
	}

	/**
	 * @param strExpect
	 * 
	 *            设置expect
	 */
	public void setExpect(String strExpect) {
		expect = parser.parse(strExpect).getAsJsonObject();
	}

	/**
	 * @param strRequest
	 * 
	 *            设置request,perfix
	 */
	public void setRequest(String strRequest) {
		request = parser.parse(strRequest).getAsJsonObject();
		// perfix写在request中，因此将其移除，同时赋值给perfix
		perfix = request.remove("perfix");
		logger.trace("request：" + request2String());
		logger.trace("perfix：" + perfix);
	}

	/**
	 * @Title: json2Map
	 * @Description: JsonObject转Map
	 * @param json
	 * @return map
	 * @date 2015-07-29
	 * 
	 *       该方法已废弃，建议采用Gson.fromJson
	 */
	@Deprecated
	public Map<String, Object> json2Map(JsonObject json) {
		Map<String, Object> map = new HashMap<String, Object>();
		Set<Entry<String, JsonElement>> entrySet = json.entrySet();
		for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext();) {
			Entry<String, JsonElement> entry = iter.next();
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if (value instanceof JsonArray)
				map.put((String) key, json2List((JsonArray) value));
			else if (value instanceof JsonObject)
				map.put((String) key, json2Map((JsonObject) value));
			else
				map.put((String) key, value.getAsString());
		}
		return map;
	}

	/**
	 * @Title: json2List
	 * @Description: JsonArray转List
	 * @param json
	 * @return list
	 * @date 2015-07-29
	 * 
	 *       该方法已废弃，建议采用Gson.fromJson
	 */
	@Deprecated
	public List<Object> json2List(JsonArray json) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < json.size(); i++) {
			Object value = json.get(i);
			if (value instanceof JsonArray) {
				list.add(json2List((JsonArray) value));
			} else if (value instanceof JsonObject) {
				list.add(json2Map((JsonObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

	/**
	 * @Title: responseAssert
	 * @Description: 对response的正确性进行验证
	 * @param void
	 * @return boolean
	 * @date 2015-07-24
	 * 
	 *       采用包含规则，即actual包含expect即可，无需绝对相等
	 */
	public boolean responseAssert() {
		logger.trace("开始执行json比对");
		return isObjContain(actual, expect);
	}

	/**
	 * @Title: isObjContain
	 * @Description: 验证第一个json是否包含第二个json
	 * @param superset:超集,subset:子集
	 * @return boolean
	 * @date 2015-07-24
	 * 
	 *       该方法与isArrayContain相互嵌套递归
	 * 
	 *       规则：超集必须包含子集的所有key，且key相等或包含
	 */
	private boolean isObjContain(JsonObject superset, JsonObject subset) {
		boolean isPass = true;
		// 超集包含子集即可，因此，遍历子集
		// 当且仅当每个子集的元素都能在超集中找到，并相等（或包含）
		// 则判定结果为true
		Iterator<Entry<String, JsonElement>> it = subset.entrySet().iterator();
		Entry<String, JsonElement> entry;
		// 遍历一级子节点
		while (it.hasNext()) {
			entry = it.next();
			logger.trace("节点：" + entry.getKey());
			if (!superset.has(entry.getKey())) {
				// 分支：子集节点在超集中无法找到，直接返回false，跳出方法
				logger.error("实际值不包含" + entry.getKey() + "的节点，比对失败！");
				report.setCheckName(entry.getKey());
				report.fail("实际值不包含" + entry.getKey() + "的节点，比对失败！");
				return false;
			} else if (entry.getValue().isJsonPrimitive() && superset.get(entry.getKey()).isJsonPrimitive()) {
				logger.trace("该节点为标量，执行标量比对");
				report.setCheckName(entry.getKey());
				report.verify(superset.get(entry.getKey()).getAsString(), entry.getValue().getAsString());
				isPass &= superset.get(entry.getKey()).equals(entry.getValue());
				logger.trace("实际值：" + superset.get(entry.getKey()) + "；预期值：" + entry.getValue());
			} else if (entry.getValue().isJsonArray() && superset.get(entry.getKey()).isJsonArray()) {
				logger.trace("该节点为数组，递归调用isArrayContain");
				isPass &= this.isArrayContain(superset.get(entry.getKey()).getAsJsonArray(),
						entry.getValue().getAsJsonArray());
			} else if (entry.getValue().isJsonObject() && superset.get(entry.getKey()).isJsonObject()) {
				logger.trace("该节点为json对象，递归调用isObjContain");
				isPass &= this.isObjContain(superset.get(entry.getKey()).getAsJsonObject(),
						entry.getValue().getAsJsonObject());
			} else {
				logger.trace("预期值与实际值的节点类型不一致，比对失败");
				report.setCheckName(entry.getKey());
				report.fail("预期值与实际值的节点类型不一致，比对失败");
				return false;
			}
		}
		return isPass;
	}

	/**
	 * @Title: isArrayContain
	 * @Description: 验证第一个jsonArray是否包含第二个jsonArray
	 * @param superset:超集,subset:子集
	 * @return boolean
	 * @date 2015-07-24
	 * 
	 *       该方法与isObjContain相互嵌套递归
	 * 
	 *       规则：子集的元素必须在超集中存在，或为超集中某个元素的子集，顺序随意
	 */
	private boolean isArrayContain(JsonArray superset, JsonArray subset) {
		boolean outerPass = true;
		boolean innerPass;
		for (JsonElement subElement : subset) {
			// 外层循环，遍历所有子集节点
			if (subElement.isJsonPrimitive()) {
				// 分支：节点为标量，则直接用contains函数判断数组是否包含
				logger.trace("该元素为标量，直接判断实际值是否存在该元素：" + subElement.toString());
				outerPass &= superset.contains(subElement);
			} else {
				// 分支：节点为数组或对象，则进入内循环
				logger.trace("该元素为json对象或数组");
				innerPass = false;
				for (JsonElement supElement : superset) {
					// 遍历超集元素
					// 当发现超集中有任意一个对象与子集匹配，则即判定成功，用或逻辑
					if (supElement.isJsonObject() && subElement.isJsonObject()) {
						innerPass |= this.isObjContain(supElement.getAsJsonObject(), subElement.getAsJsonObject());
					} else if (supElement.isJsonArray() && subElement.isJsonArray()) {
						innerPass |= this.isArrayContain(supElement.getAsJsonArray(), subElement.getAsJsonArray());
					}
				}
				outerPass &= innerPass;
			}
		}
		return outerPass;
	}
}