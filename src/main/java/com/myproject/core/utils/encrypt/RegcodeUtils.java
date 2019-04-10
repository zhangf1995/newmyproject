package com.myproject.core.utils.encrypt;

import com.myproject.core.utils.FileOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.Key;
import java.util.Map;

public class RegcodeUtils {
	private static String PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqEMp1Giv7xWyIbNd3WnaR+n220JKwkHJf/90gcx8qbfYrObAsZuKCwn6pySaEOWADxkwJmXTAsEH4WpuC9CMjzGxY6cVBZdnh7mH6x7dvMqWGgsXNcw6ZKWjHIVyTvCR34n18ufSBdj018VqztXd8DYbIDQ5ky9NKZEXLDSm5tHbVLtW52ks7IDCP7We9di/7P936KM4RDHzMNvTwbgy8DBGnOf9EInsKL9iupIlwwUl+MCVCW/n3x6m/V8IYFaUvqQIBDkSwcchl4HUf6oefm77wKDRDm51jYTeim830mc53rt2pjXM/DhwbK90vdX5IEeD7n2uD/v900Ii0tuYVQIDAQAB";
	private static final long BASE_TIME = 1545982090499L;
	/**
	 * 校验注册码
	 * 
	 * @param code
	 *            注册码
	 * @param serverId
	 *            设备ID
	 * @param version
	 *            软件版本
	 * @return SUCCESS or Others
	 */
	public static String validateReginfo(String code, String serverId, String version) {
		try {
			if(StringUtils.isBlank(code)){
				return "License Needed[请上传授权文件]";
			}
			Key publicKey = SecurityUtil.RsaUtil.getPublicKey(PUB_KEY);
			// 验证注册文件内容
			String decrypt = SecurityUtil.RsaUtil.decrypt(code.trim(), publicKey).trim();
			String[] infoArr = decrypt.split(",");
			String _serverId = infoArr[0];
			String expireMills = infoArr[1];
			String _version = infoArr[2];
			if (!_serverId.equals(serverId)) {
				return "Invalid Equipment Code[设备码不匹配]";
			}
			if (!_version.equals(version)) {
				return "Invalid Version[版本号不匹配]";
			}
			Long curMills = System.currentTimeMillis();
			if ((curMills-BASE_TIME) > Long.valueOf(expireMills)) {
				return "Expired License[授权已过期]";
			}
			
			Long leftMills = Long.valueOf(expireMills) - (curMills-BASE_TIME);
			long leftDays =(long)(leftMills / (24*60*60*1000L));
			if (leftDays <= 7) {
				return "WARN:License will expire in "+leftDays+" days[友情提示：授权即将在"+leftDays+"天后过期]";
			}
			
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
	}

	private static String getMACAddress() throws Exception {
		InetAddress ia = InetAddress.getLocalHost();
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}

		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase().replaceAll("-", "");
	}

	/**
	 * 获取设备ID
	 * 
	 * @return
	 */
	public static String getServerID() {
		Map<String, String> map = System.getenv();
		String PROCESSOR_IDENTIFIER = "";
		try {
			PROCESSOR_IDENTIFIER = map.get("PROCESSOR_IDENTIFIER");
		} catch (Exception e) {
		}

		String PROCESSOR_REVISION = "";
		try {
			PROCESSOR_REVISION = map.get("PROCESSOR_REVISION");
		} catch (Exception e) {
		}

		String COMPUTERNAME = "";
		try {
			COMPUTERNAME = map.get("COMPUTERNAME");
		} catch (Exception e) {
		}
		String OS = "";
		try {
			OS = map.get("OS");
		} catch (Exception e) {
		}

		String PROCESSOR_ARCHITECTURE = "";
		try {
			PROCESSOR_ARCHITECTURE = map.get("PROCESSOR_ARCHITECTURE");
		} catch (Exception e) {
		}
		String macAddr = "";
		try {
			macAddr = getMACAddress();
		} catch (Exception e) {
		}
		return MD5.getMD5(
				macAddr + PROCESSOR_IDENTIFIER + PROCESSOR_REVISION + COMPUTERNAME + OS + PROCESSOR_ARCHITECTURE)
				.substring(8, 24).toUpperCase();
	}
	
	public static String getCodeInfo(){
		String localAppDataDir =  System.getenv().get("LOCALAPPDATA");
		String allUsersProfile =  System.getenv().get("ALLUSERSPROFILE");
		String javaHome = System.getProperties().getProperty("java.home");
		String SEPARATOR = System.getProperty("file.separator");
		String codeInfo = "";
		
		if(StringUtils.isNotBlank(localAppDataDir)){
			localAppDataDir+=SEPARATOR+"6A896M22F18146E4A1U6CCM7795E90D3";
			try {
				File codeFile = new File(localAppDataDir);
				if(codeFile.exists()){
					codeInfo = FileOperation.readTxtFile(codeFile);
					if(StringUtils.isNotBlank(codeInfo)){
						return codeInfo;
					}
				}
				
			} catch (Exception e) {
			}
		}
		if(StringUtils.isNotBlank(allUsersProfile)){
			allUsersProfile+=SEPARATOR+"SLUE792KKL3479OML83LJ";
			try {
				File codeFile = new File(allUsersProfile);
				if(codeFile.exists()){
					codeInfo = FileOperation.readTxtFile(codeFile);
					if(StringUtils.isNotBlank(codeInfo)){
						return codeInfo;
					}
				}
				
			} catch (Exception e) {
			}
		}
		if(StringUtils.isNotBlank(javaHome)){
			javaHome+=SEPARATOR+"bin"+SEPARATOR+"javawm.dll";
			try {
				File codeFile = new File(javaHome);
				if(codeFile.exists()){
					codeInfo = FileOperation.readTxtFile(codeFile);
					if(StringUtils.isNotBlank(codeInfo)){
						return codeInfo;
					}
				}
				
			} catch (Exception e) {
			}
		}
		
		return codeInfo;
	}
	
	public static void setCodeInfo(byte[] regData) {
		String localAppDataDir =  System.getenv().get("LOCALAPPDATA");
		String allUsersProfile =  System.getenv().get("ALLUSERSPROFILE");
		String javaHome = System.getProperties().getProperty("java.home");
		String SEPARATOR = System.getProperty("file.separator");
		if(StringUtils.isNotBlank(localAppDataDir)){
			localAppDataDir+=SEPARATOR+"6A896M22F18146E4A1U6CCM7795E90D3";
			try {
				File codeFile = new File(localAppDataDir);
				FileCopyUtils.copy(regData, codeFile);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(allUsersProfile)){
			allUsersProfile+=SEPARATOR+"SLUE792KKL3479OML83LJ";
			try {
				File codeFile = new File(allUsersProfile);
				FileCopyUtils.copy(regData, codeFile);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotBlank(javaHome)){
			javaHome+=SEPARATOR+"bin"+SEPARATOR+"javawm.dll";
			try {
				File codeFile = new File(javaHome);
				FileCopyUtils.copy(regData, codeFile);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}