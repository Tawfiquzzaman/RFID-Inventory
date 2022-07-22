package cn.pda.serialport;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Tools {

	//byte转hex
	public static String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() < 2) {
			hex = "0" + hex;
		}
		return hex;
	}

	//byte[]转hex
	public static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if(hex.length() < 2){
				sb.append(0);
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	/**
	 * Bytes change to HexString
	 * @param b
	 * @param size
	 * @return string 
	 */
		public static String Bytes2HexString(byte[] b, int size) {
		    String ret = "";
		    for (int i = 0; i < size; i++) {
		      String hex = Integer.toHexString(b[i] & 0xFF);
		      if (hex.length() == 1) {
		        hex = "0" + hex;
		      }
		      ret += hex.toUpperCase();
		    }
		    return ret;
		  }
		
		public static byte uniteBytes(byte src0, byte src1) {
		    byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
		    _b0 = (byte)(_b0 << 4);
		    byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
		    byte ret = (byte)(_b0 ^ _b1);
		    return ret;
		  }
		
		/**
		 * HexString change to Bytes
		 * @param src
		 * @return
		 */
		public static byte[] HexString2Bytes(String src) {
			int len = src.length() / 2;
			byte[] ret = new byte[len];
			byte[] tmp = src.getBytes();

			for (int i = 0; i < len; i++) {
				ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
			return ret;
		}
		
		/**
		 * Byte[] change to int
		 * @param bytes
		 * @return int 
		 */
		public static int bytesToInt(byte[] bytes)
		{
			int addr = bytes[0] & 0xFF;
			addr |= ((bytes[1] << 8) & 0xFF00);
			addr |= ((bytes[2] << 16) & 0xFF0000);
			addr |= ((bytes[3] << 25) & 0xFF000000);
			return addr;

		}

		/**
		 * Int change to byte[]
		 * @param i
		 * @return byte[]
		 */
		public static byte[] intToByte(int i)
		{
			byte[] abyte0 = new byte[4];
			abyte0[0] = (byte) (0xff & i);
			abyte0[1] = (byte) ((0xff00 & i) >> 8);
			abyte0[2] = (byte) ((0xff0000 & i) >> 16);
			abyte0[3] = (byte) ((0xff000000 & i) >> 24);
			return abyte0;
		}
		
		/**
		 * System time now (Format:yyyy-MM-dd HH:mm:ss)
		 * @return string
		 */
		public static String getTime(){
			String model = "yyyy-MM-dd HH:mm:ss";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat(model);
			String dateTime = format.format(date);
			return  dateTime;
		}

	public static String[] getHexStringArray(byte[] b) {
		String[] hexStrings = new String[b.length];
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexStrings[i] = hex;
		}
		return hexStrings;
	}

}
