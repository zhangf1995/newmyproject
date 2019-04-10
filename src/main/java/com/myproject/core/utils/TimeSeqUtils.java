package com.myproject.core.utils;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeSeqUtils {
	private static AtomicInteger increase = new AtomicInteger(1);
    private static int getIncreaseNumber(){
    	int increment = increase.incrementAndGet();
    	if(increment==Integer.MAX_VALUE-10000){
    		increment = increase.getAndSet(1);
    	}
    	return increment;
    }
    
    /**
     * 获取13位的timeseq，如：0090100000000 
     * 5位(年月日) + 4位(毫秒数+时*分*秒) + 4位(随机)
     * @return
     */
    public synchronized static String getTimeSeq(){
    	Calendar now = Calendar.getInstance();
    	int year = now.get(Calendar.YEAR);  
        int month = now.get(Calendar.MONTH) + 1 ;  
        int days = now.get(Calendar.DAY_OF_MONTH);  
        int hours = now.get(Calendar.HOUR_OF_DAY);  
        int minutes = now.get(Calendar.MINUTE);  
        int seconds = now.get(Calendar.SECOND);  
        long mills = now.getTimeInMillis();
        
        
        int yearDiff = year - 2018; //年差
        int monthDiff = yearDiff * 12 + month;//月差  3位
    	
    	String part2 = "00"+monthDiff;
    	part2 = part2.substring(part2.length() - 3);
    	
    	String part3 = "0"+(days);
    	part3 = part3.substring(part3.length() - 2);
    	
    	String part4 = "0000" + (mills + hours * minutes * seconds);
    	part4 = part4.substring(part4.length() - 4);
    	
    	String part5 = "0000" + (getIncreaseNumber()%10000);
    	part5 = part5.substring(part5.length() - 4);
    	
    	return part2 + part3 + part4 + part5;
    }
}
