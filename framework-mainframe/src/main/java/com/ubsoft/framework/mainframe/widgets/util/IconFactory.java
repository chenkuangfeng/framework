package com.ubsoft.framework.mainframe.widgets.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconFactory {

	private static Map<String, ImageIcon> cache = new HashMap<String, ImageIcon>();

	
	public static Image getImage(String imgPath)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(ClassLoader.getSystemResource(imgPath));
		} catch (IOException e)
		{
			System.out.println("图片路径找不到: "+imgPath);
			//e.printStackTrace();
		}
		return img;
	}
	public static ImageIcon getImageIcon(String imgPath)
	{
		ImageIcon icon = null;
		try
		{
			 URL url = ClassLoader.getSystemResource(imgPath);			
			icon = new ImageIcon(ClassLoader.getSystemResource(imgPath));
			return icon;
		}
		catch (Exception e)
		{
			System.out.println("找不到图片： " +imgPath );
		}
		return icon;
	}
}
