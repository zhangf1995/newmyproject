package com.myproject.core.utils;

import com.myproject.core.consts.ICodes;
import com.myproject.exception.BisException;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StrUtils {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdffull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Map<String, String> snPrefixMap = new HashMap<String, String>();
	private static final Map<String,Map<Integer, AtomicInteger>> snIncreaseMaps = new HashMap<>();
	static{
		
		snPrefixMap.put("FinanceExpenseServiceImpl", "E");
		snPrefixMap.put("FinanceIncomeServiceImpl", "F");
		snPrefixMap.put("FinanceStatementServiceImpl", "G");
		
		snPrefixMap.put("StockInServiceImpl", "P");
		snPrefixMap.put("StockOutServiceImpl", "Q");
		snPrefixMap.put("StockTransferServiceImpl", "T");
		snPrefixMap.put("StockShipServiceImpl", "W");
		
		snPrefixMap.put("PurchaseServiceImpl", "C");
		snPrefixMap.put("PurchaseOrderServiceImpl", "A");
		snPrefixMap.put("PurchaseReturnServiceImpl", "D");
		snPrefixMap.put("PurchaseSupplyServiceImpl", "B");
		
		snPrefixMap.put("SaleServiceImpl", "S");
		snPrefixMap.put("SaleOrderServiceImpl", "U");
		snPrefixMap.put("SaleReturnServiceImpl", "R");
		snPrefixMap.put("RefundRecordServiceImpl", "K");
		snPrefixMap.put("ProductionServiceImpl", "I");
	}
	
	public static String[] splitToString(String str) {
		return splitToString(str, ",");
	}
	public static Long[] splitToLong(String str) {
		return splitToLong(str, ",");
	}

	public static String[] splitToString(String str, String separator) {
		if (null == str || "".equals(str))
			return null;
		String[] strArr = str.split(separator);
		return strArr;
	}
	
	public static Long[] splitToLong(String str, String separator) {
		if (null == str || "".equals(str))
			return null;
		String[] strArr = str.split(separator);
		Long[] longArr = new Long[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			longArr[i] = Long.parseLong(strArr[i]);
		}
		return longArr;
	}
	
	public static String getRandomString(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);
			sb.append(str.charAt(number));
		}
		return sb.toString();

	}

	public static String getComplexRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	
	public static String convertPropertiesToHtml(String properties){
		//1:容量:6:32GB_4:样式:12:塑料壳
		StringBuilder sBuilder = new StringBuilder();
		String[] propArr = properties.split("_");
		for (String props : propArr) {
			String[] valueArr = props.split(":");
			sBuilder.append(valueArr[1]).append(":").append(valueArr[3]).append("<br>");
		}
		return sBuilder.toString();
	}
    
    /**
     * 获取8位数字和大小写字母组成的唯一ID
     * @return
     */
    public static String getId(){
    	String timeSeq = TimeSeqUtils.getTimeSeq();
    	String id = EncodeNumberUtils.encode36(Long.valueOf(timeSeq));
    	return id;
    }
    
    public static long getIdLong(){
    	return SnowflakeIdWorker.me().nextId();
    }
    
    
    /**
     * 获取以2开头的9位数字和大小写字母的组合字符串作为唯一标识
     * @return
     */
    public static String getDna(){
    	String timeSeq = TimeSeqUtils.getTimeSeq();
    	return "2"+EncodeNumberUtils.encode36(Long.valueOf(timeSeq));
    }
    
    
    /**
     * 获取9位的数字作为单号，首字母识别不同单号
     * E = 支出单号
     * F = 收入单号
     * G = 对账单号
     * @return
     */
    public static String getSn(String firstLetter){
    	Date now = new Date();
    	String nowDate = sdf.format(now);
    	int days = getTimeDiffDays("2018-07-27", nowDate);
    	
    	Map<Integer, AtomicInteger> snIncreaseMap = snIncreaseMaps.get(firstLetter);
    	if(snIncreaseMap == null){
    		snIncreaseMap = new ConcurrentHashMap<Integer, AtomicInteger>();
    		snIncreaseMaps.put(firstLetter, snIncreaseMap);
    	}
    	
    	AtomicInteger atomicInteger = snIncreaseMap.get(days);
    	if(atomicInteger==null){ //不存在该天的序号
    		int size = snIncreaseMap.size();
    		if (size>0) { //正常进入下一天，初始化序号为1
    			snIncreaseMap.clear();//清空原记录号
    			atomicInteger =  new AtomicInteger(0);
    			snIncreaseMap.put(days,atomicInteger);
			}else{ //系统启动后第一次加载，有可能是重启，所以起始号段要特殊处理
				//号码段分配规则 ： 0~7点之间(1500单 平均每分钟3.125 )  8~18(7000 平均每分钟10.6) 19~23(1500单 平均每分钟5单)
				int minutes = now.getMinutes();// 0~59
				int hours = now.getHours(); //0~23
				int totalMinuts = hours * 60 + minutes;
				
				int section1 = 0;
				int section2 = 0;
				int section3 = 0;
				
				if(totalMinuts > (8*60+11*60)){ //在第三段时间中
					section1 = 480;
					section2 = 660;
					section3 = totalMinuts - section1 - section2;
				}else if(totalMinuts > (8*60)){ //在第二段时间中
					section1 = 480;
					section2 = totalMinuts - section1;
					section3 = 0;
				}else{
					section1 = totalMinuts;
					section2 = 0;
					section3 = 0;
				}
				
				int initValue = (int)(3.125*section1 + 10.6*section2 + 5*section3) + 1;
				atomicInteger =  new AtomicInteger(initValue);
				snIncreaseMap.put(days, atomicInteger);
			}
    	}
    	
    	//获取序号
    	int seq = atomicInteger.getAndIncrement() % 10000;
    	String seqStr = ("000"+seq);
    	seqStr = seqStr.substring(seqStr.length() - 4);
    	return firstLetter + (1000+days) + seqStr;
    }
    public static String getSn(){
    	String firstLetter = "";
    	StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    	String className = stackTrace[2].getClassName();
    	className = className.substring(className.lastIndexOf(".")+1);
    	firstLetter = snPrefixMap.get(className);
    	if(StringUtils.isBlank(firstLetter)){
    		throw BisException.me().setCode(ICodes.FAILED);
    	}
    	return getSn(firstLetter);
    }
   
    //获取两个日期的天数差
    public static int getTimeDiffDays(String fromDate,String toDate){
    	int days = Integer.MIN_VALUE;
    	try {
    		long from = sdf.parse(fromDate).getTime();
    		long to = sdf.parse(toDate).getTime();
    		days = (int) ((to - from)/(1000 * 60 * 60 * 24));
    		if (days < 0) {
				days = -1 * days;
			}
		} catch (Exception e) {
		}
    	return days;
    }
    
    /**
     * 取消数字小数点后无效的0
     * @param src
     * @return
     */
    public static String trimZero(BigDecimal src){
    	String s = src.toString();
    	if(s.indexOf(".") > 0){
    		  //正则表达
    		  s = s.replaceAll("0+?$", "");//去掉后面无用的零
    		  s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
    	}
    	return s;
    }

	/**
	 *
	 * @param fileAllName	文件名称：123.png
	 * @param split	分隔符
	 * @return	后缀名	png
	 */
	public static String getPrefix(String fileAllName,String split){
		return fileAllName.substring(fileAllName.lastIndexOf(split)+1);
	}

	/**
	 * 获取速记码
	 * @param str
	 * @return
	 */
	public static String getShortCode(String str){
		String shortCode="";
		try {
			shortCode = PinyinHelper.getShortPinyin(str);
		} catch (PinyinException e1) {
			e1.printStackTrace();
		}

		if (shortCode.length() > 10) shortCode = shortCode.substring(0, 10);
		return shortCode;
	}

	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			new BigDecimal(str);
		} catch (Exception e) {
			return false;//异常 说明包含非数字。
		}
		return true;
	}
    
    public static void main(String[] args) throws ParseException {
		for (int i = 0; i < 1000; i++) {
			System.out.println(getDna());
		}
	}
}
