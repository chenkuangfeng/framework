package com.ubsoft.framework.system.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ubsoft.framework.core.support.file.FileHelper;

public class LicenseManager {  
	      
	    /** 
	     * serial：由客户提供 
	     * timeEnd：过期时间 
	     */  
	    private static String licensestatic = "serial=568b8fa5cdfd8a2623bda1d8ab7b7b34;" +  
	                                          "timeEnd=1404057600000";  
	       
	    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaa8y9g+ioR2OSs8njT5uCGWm0YmOA1elSu/P5\n"  
	            + "D3XYPCHsUPab74V5Og+NEZeTk9/LtG+jPSpKekTWm67gS2lQYWoygTwnoWsr4woaqNXmWw7L8Ty0\n"  
	            + "LQFTojZgyynvu2RSIJp4c76z6SV/khiP/ireGtt8uzXPswPO5uaPU38tQQIDAQABDFDFDFDFDFDERR";  
	      
	    /** 
	     * RSA算法 
	     * 公钥和私钥是一对，此处只用私钥加密 
	     */  
	    public static final String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJprzL2D6KhHY5KzyeNPm4IZabRi\n"  
	            + "Y4DV6VK78/kPddg8IexQ9pvvhXk6D40Rl5OT38u0b6M9Kkp6RNabruBLaVBhajKBPCehayvjChqo\n"  
	            + "1eZbDsvxPLQtAVOiNmDLKe+7ZFIgmnhzvrPpJX+SGI/+Kt4a23y7Nc+zA87m5o9Tfy1BAgMBAAEC\n"  
	            + "gYAVnlfohEoTHQN0q1TtTNzRhutEhK23gLsMiSGr0Z1G64w4QFF2HT9LbHR25GqbD426QAWNDegY\n"  
	            + "yytN/DesUQJqNXx8vuEuqs7+MQDgKgJqpAx+Fg3Iwsk/SVjq7meaSVGCgPKhtWHJk5oXoRMpsrlT\n"  
	            + "AwUjpdpAZXIIKW3mrqkW0QJBANq4INw6lZlqRFtxT4uzYQYtzmB/nxMnCbL2SQ4ZQ/4CWlQpOnR/\n"  
	            + "mH2JxIBCVtTADFlPM0DWF4aoqykYs9tu2X0CQQC0vgEk8DpkQbh1kgIyBGWCqYSKISTSXia0rbYo\n"  
	            + "FPnzdldgtZVirNGNmiJGL8RPz0YKpZNOg9FLHq/oYXSNFI4VAkAJ4OcbC0pWc4ZC2wtMs/1d2hPI\n"  
	            + "J/t3UfwOKTGDgYCgqFqMEpChUmIAyYgmgtiJI2NrZThbZVAKtPOGF6eH8anBAkAbxkL4wS3H8E1/\n"  
	            + "S7OoqgJLZO9oJpW4+hzqkPM4D5klb58Xzm+pXTNKllAEBx0cwpZZ1n3fh+Qmrg2MIUW+1FTNAkBt\n"  
	            + "WECowLUqW014M96WsFpiof7kjteOBNOjFyxhIbx2eT7//bnrADfq2Xu1/mSedUKrjGr/O+FRi7PO\n"  
	            + "u7WhF6C9DFDFDFDFDFDFER3R34";  
	      
	    public static void genLicense(String licData,String path) throws Exception {  
	        //String source = "568b8fa5cdfd8a2623bda1d8ab7b7b34";  
	     
	    	licData=getMotherboardSN()+"|"+getCPUSerial()+"|"+getMac()+"|"+licData;
	    	
	        byte[] encodedData = RSAUtils.encryptByPrivateKey(licData.getBytes(), privateKey);  
	          
	        Base64Utils.byteArrayToFile(encodedData, path);  
	          
	        //解密  
	        byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);  
	        String target = new String(decodedData);  
	    }  
	      
	    
	    public static String decryptLicense(String path) throws Exception{
	    	 //解密  
	    	byte [] encodedData =FileHelper.fileToByte(path);
	        byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);  
	        String target = new String(decodedData);  
	      
	        return target;
	    }
	   
	    
	    public static String getOsName() {
	        String os = "";
	        os = System.getProperty("os.name");
	        return os;
	    }
	    /**
	     * 获取主板序列号
	     * 
	     * @return
	     */
	    public static String getMotherboardSN() {
	        String result = "";
	        try {
	            File file = File.createTempFile("realhowto", ".vbs");
	            file.deleteOnExit();
	            FileWriter fw = new FileWriter(file);
	 
	            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
	                    + "Set colItems = objWMIService.ExecQuery _ \n"
	                    + "   (\"Select * from Win32_BaseBoard\") \n"
	                    + "For Each objItem in colItems \n"
	                    + "    Wscript.Echo objItem.SerialNumber \n"
	                    + "    exit for  ' do the first cpu only! \n" + "Next \n";
	 
	            fw.write(vbs);
	            fw.close();
	            Process p = Runtime.getRuntime().exec(
	                    "cscript //NoLogo " + file.getPath());
	            BufferedReader input = new BufferedReader(new InputStreamReader(
	                    p.getInputStream()));
	            String line;
	            while ((line = input.readLine()) != null) {
	                result += line;
	            }
	            input.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result.trim();
	    }
	    /**
	     * 获取盘符序列号
	     * @param drive
	     * @return
	     */
	    public static String getHardDiskSN(String drive) {
	        String result = "";
	        try {
	            File file = File.createTempFile("realhowto", ".vbs");
	            file.deleteOnExit();
	            FileWriter fw = new FileWriter(file);
	 
	            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
	                    + "Set colDrives = objFSO.Drives\n"
	                    + "Set objDrive = colDrives.item(\""
	                    + drive
	                    + "\")\n"
	                    + "Wscript.Echo objDrive.SerialNumber"; // see note
	            fw.write(vbs);
	            fw.close();
	            Process p = Runtime.getRuntime().exec(
	                    "cscript //NoLogo " + file.getPath());
	            BufferedReader input = new BufferedReader(new InputStreamReader(
	                    p.getInputStream()));
	            String line;
	            while ((line = input.readLine()) != null) {
	                result += line;
	            }
	            input.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result.trim();
	    }
	 
	    /**
	     * 获取CPU序列号
	     * @return
	     */
	    
	    public static String getCPUSerial() {
	        String result = "";
	        try {
	            File file = File.createTempFile("tmp", ".vbs");
	            file.deleteOnExit();
	            FileWriter fw = new FileWriter(file);
	            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
	                    + "Set colItems = objWMIService.ExecQuery _ \n"
	                    + "   (\"Select * from Win32_Processor\") \n"
	                    + "For Each objItem in colItems \n"
	                    + "    Wscript.Echo objItem.ProcessorId \n"
	                    + "    exit for  ' do the first cpu only! \n" + "Next \n";
	 
	            // + "    exit for  \r\n" + "Next";
	            fw.write(vbs);
	            fw.close();
	            Process p = Runtime.getRuntime().exec(
	                    "cscript //NoLogo " + file.getPath());
	            BufferedReader input = new BufferedReader(new InputStreamReader(
	                    p.getInputStream()));
	            String line;
	            while ((line = input.readLine()) != null) {
	                result += line;
	            }
	            input.close();
	            file.delete();
	        } catch (Exception e) {
	            e.fillInStackTrace();
	        }
	        if (result.trim().length() < 1 || result == null) {
	            result = "无CPU_ID被读取";
	        }
	        return result.trim();
	    }
	    /**
	     * 获取mac地址
	     * @return
	     */
	    public static String getMac() {
	    	String address = "";
	        String os = getOsName();
	        if (os.startsWith("Windows")) {
	            try {
	                String command = "cmd.exe /c ipconfig /all";
	                Process p = Runtime.getRuntime().exec(command);
	                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                String line;
	                while ((line = br.readLine()) != null) {
	                    if (line.indexOf("Physical Address") > 0) {
	                        int index = line.indexOf(":");
	                        index += 2;
	                        address = line.substring(index);
	                        break;
	                    }
	                }
	                br.close();
	                return address.trim();
	            } catch (IOException e) {
	            }
	        } else if (os.startsWith("Linux")) {
	            String command = "/bin/sh -c ifconfig -a";
	            Process p;
	            try {
	                p = Runtime.getRuntime().exec(command);
	                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                String line;
	                while ((line = br.readLine()) != null) {
	                    if (line.indexOf("HWaddr") > 0) {
	                        int index = line.indexOf("HWaddr") + "HWaddr".length();
	                        address = line.substring(index);
	                        break;
	                    }
	                }
	                br.close();
	            } catch (IOException e) {
	            }
	        }
	        address = address.trim();
	        return address;
	    }	    
	    public static void main(String[] args) throws Exception {  
	    	 
	    	genLicense("2018-02-20","D:\\notepad\\lic.lic");  
	    	decryptLicense("D:\\notepad\\lic.lic");
	    }  
	}  
	   

