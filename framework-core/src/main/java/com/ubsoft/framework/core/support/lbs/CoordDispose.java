package com.ubsoft.framework.core.support.lbs;

public class CoordDispose {
	private static double EARTH_RADIUS = 6378137.0;// 地球半径(米)

	// / <summary>
	// / 角度数转换为弧度公式
	// / </summary>
	// / <param name="d"></param>
	// / <returns></returns>
	private static double radians(double d) {
		return d * Math.PI / 180.0;
	}

	// / <summary>
	// / 弧度转换为角度数公式
	// / </summary>
	// / <param name="d"></param>
	// / <returns></returns>
	private static double degrees(double d) {
		return d * (180 / Math.PI);
	}

	// / <summary>
	// / 计算两个经纬度之间的直接距离
	// / </summary>
	public static double GetDistance(Degree dFrom, Degree dTo) {
		double x, y, distance;
		x = (dTo.getX() - dFrom.getX()) * Math.PI * EARTH_RADIUS * Math.cos(((dTo.getY() + dFrom.getY()) / 2) * Math.PI / 180) / 180;
		y = (dTo.getY() - dFrom.getY()) * Math.PI * EARTH_RADIUS / 180;
		distance = Math.hypot(x, y);
		return distance;
		//
		// double radLat1 = radians(dFrom.getX());
		// double radLat2 = radians(dTo.getX());
		// double a = radLat1 - radLat2;
		// double b = radians(dFrom.getY()) - radians(dTo.getY());
		// double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
		// Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2),
		// 2)));
		// s = s * EARTH_RADIUS;
		// s = Math.round(s * 10000) / 10000;
		// return s;
	}

	// / <summary>
	// / 计算两个经纬度之间的直接距离(google 算法)
	// / </summary>
	public static double GetDistanceGoogle(Degree dFrom, Degree dTo) {
		double radLat1 = radians(dFrom.getX());
		double radLng1 = radians(dFrom.getY());
		double radLat2 = radians(dTo.getX());
		double radLng2 = radians(dTo.getY());
		double s = Math.acos(Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radLng1 - radLng2) + Math.sin(radLat1) * Math.sin(radLat2));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	// public static ResponseMessageNews SendMiniDistance(Degree currentDegree,
	// RequestMessageLocation requestMessage)
	// {
	//
	// List<STORE_EXT> storeList = (List<STORE_EXT>)new
	// StoreLbsCache().TryToGetCache("storeList");
	// double disRound = Convert.ToDouble(WechatMpConfig.DisRound);
	// var responseMessage =
	// ResponseMessageBase.CreateFromRequestMessage<ResponseMessageNews>(requestMessage);
	// if (storeList != null && storeList.Count > 0)
	// {
	// List<STORE_EXT> availableStoreExts = new List<STORE_EXT>();
	// foreach (STORE_EXT store in storeList)
	// {
	// // double a = CoordDispose.GetDistance(currentDegree, new
	// Degree(Convert.ToDouble( dr["location_y"]),Convert.ToDouble(
	// dr["location_x"])));
	// double b = CoordDispose.GetDistanceGoogle(currentDegree,
	// new Degree(Convert.ToDouble(store.LATITUDE),
	// Convert.ToDouble(store.LONGITUDE)));
	// //double bb =
	// if (b < disRound)
	// {
	// store.TEMPDISTANCE = decimal.Round((decimal)(b / 1000), 2);
	// availableStoreExts.Add(store);
	// }
	// }
	// if (availableStoreExts.Count > 0)
	// {
	// availableStoreExts = availableStoreExts.OrderBy(t =>
	// t.TEMPDISTANCE).ToList();
	// foreach (STORE_EXT store in availableStoreExts)
	// {
	// responseMessage.Articles.Add(
	// new Article()
	// {
	// Description = store.STOREDESC,
	// PicUrl = "http://vipmd.lachapelle.cn/LaChaMoCenter/Images/lachalogo.jpg",
	// Title = store.STOREDESC + "(距离" + store.TEMPDISTANCE + "千米 " +
	// store.CONTRACT2 + ")",
	// Url = "http://apis.map.qq.com/uri/v1/marker?marker=coord:" +
	// store.LATITUDE + "," + store.LONGITUDE +
	// ";title:" + store.CONTRACT1 + "(距离" + store.TEMPDISTANCE + "千米)" +
	// ";addr:" + requestMessage.Label
	// }
	// );
	//
	// if (responseMessage.Articles.Count > 7)
	// {
	// break;
	// }
	// }
	// }
	// else
	// {
	// responseMessage.Articles.Add(
	// new Article()
	// {
	// Description = "附近没有可以购买的门店，到我们的网上商城看看吧",
	// PicUrl = "http://vipmd.lachapelle.cn/USWechatFuns/Images/usLogo.jpg",
	// Title = "附近没有符合条件的门店",
	// Url = "http://lachapelle.jd.com"
	// }
	// );
	// }
	//
	// }
	// else
	// {
	// responseMessage.Articles.Add(
	// new Article()
	// {
	// Description = "附近没有可以购买的门店，到我们的网上商城看看吧",
	// PicUrl = "http://vipmd.lachapelle.cn/USWechatFuns/Images/usLogo.jpg",
	// Title = "附近没有符合条件的门店",
	// Url = "http://lachapelle.jd.com"
	// }
	// );
	// }
	// return responseMessage;
	// }
	// / <summary>
	// / 以一个经纬度为中心计算出四个顶点
	// / </summary>
	// / <param name="distance">半径(米)</param>
	// / <returns></returns>
	// public static Degree[] GetDegreeCoordinates(Degree dFrom, double
	// distance)
	// {
	// double dlng = 2 * Math.asin(Math.sin(distance / (2 * EARTH_RADIUS)) /
	// Math.cos(dFrom.getX()));
	// dlng = degrees(dlng);//一定转换成角度数 原PHP文章这个地方说的不清楚根本不正确 后来lz又查了很多资料终于搞定了
	// double dlat = distance / EARTH_RADIUS;
	// dlat = degrees(dlat);//一定转换成角度数
	// return new Degree[] { new Degree(Math.round(dFrom.getX() + dlat,6),
	// Math.round(dFrom.getY() - dlng,6)),//left-top
	// new Degree(Math.round(dFrom.getX() - dlat,6), Math.round(dFrom.getY() -
	// dlng,6)),//left-bottom
	// new Degree(Math.round(dFrom.getX() + dlat,6), Math.round(dFrom.getY() +
	// dlng,6)),//right-top
	// new Degree(Math.round(dFrom.getX() - dlat,6), Math.round(dFrom.getY() +
	// dlng,6)) //right-bottom
	// };
	// }
	public static void main(String[] args) {
		// 济南国际会展中心经纬度：117.11811 36.68484
		// 趵突泉：117.00999000000002 36.66123
		Degree dfrom = new Degree(121.433426, 31.173098);
		Degree dto = new Degree(121.4112, 31.22145);
		System.out.println(GetDistance(dfrom, dto));
		// System.out.println(getDistance("121.433426","31.173098","121.4112","31.22145"));

		// System.out.println(getAround("117.11811", "36.68484", "13000"));
		// 117.01028712333508(Double), 117.22593287666493(Double),
		// 36.44829619896034(Double), 36.92138380103966(Double)

	}
}
