package com.ubsoft.framework.core.support.lbs;


public class test {
	 private final static double PI = 3.14159265358979323; // 圆周率
	    private final static double R = 6371229; // 地球的半径

	    public static double getDistance(double longt1, double lat1, double longt2,double lat2) {
	        double x, y, distance;
	        x = (longt2 - longt1) * PI * R
	                * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
	        y = (lat2 - lat1) * PI * R / 180;
	        distance = Math.hypot(x, y);
	        return distance;
	    }

     
    public static void main(String[] args) {
        //济南国际会展中心经纬度：117.11811  36.68484
        //趵突泉：117.00999000000002  36.66123
       System.out.println(getDistance(121.433426,31.173098,121.4112,31.22145));
         
       // System.out.println(getAround("117.11811", "36.68484", "13000"));
        //117.01028712333508(Double), 117.22593287666493(Double),
        //36.44829619896034(Double), 36.92138380103966(Double)
         
    }
     

}
