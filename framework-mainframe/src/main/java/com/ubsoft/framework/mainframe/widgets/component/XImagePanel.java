package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.ubsoft.framework.mainframe.widgets.util.SwingUtil;

/**
 * 可设置背景图片的JPanel，提供了三种显示背景图片的方式：居中、平铺和拉伸。
 * 未设置背景图片的情况下，同JPanel。
 * 
 */
public class XImagePanel  extends JPanel{
	 private static final long serialVersionUID = -6914280571042537095L;

	    /**
	     * 居中
	     */
	    public static final String CENTER = "Center";
	    
	    /**
	     * 平铺
	     */
	    public static final String TILED = "Tiled";

	    /**
	     * 拉伸
	     */
	    public static final String SCALED = "Scaled";

	    /**
	     * 背景图片
	     */
	    private Image image;
	    
	    /**
	     * 背景图片显示模式
	     */
	    private String mode;
	    
	    /**
	     * 背景图片的透明度
	     */
	    private float alpha;
	    
	    /**
	     * 旋转的角度
	     */
	    private double angle;
	    
	    /**
	     * 四个角的圆弧大小
	     */
	    private int[] cornersSize;
	    
	    /**
	     * 是否绘制图片的边框
	     */
	    private boolean showGrid;
	    
	    /**
	     * 图片边框的颜色
	     */
	    private Color gridColor;
	    
	    /**
	     * 保持长宽比
	     */
	    private boolean keepAspectRatio;
	    
	    /**
	     * 填充整个区域
	     */
	    private boolean filledAll;
	    
	    /**
	     * 在Border区域也绘制图像
	     */
	    private boolean filledBorderArea;
	    
	    /**
	     * 背景是否只显示图像
	     */
	    private boolean imageOnly;
	    
	    /**
	     * 当前所呈现的图像
	     */
	    private BufferedImage currentImage;
	    
	    /**
	     * 当前展示的整个区域的图像
	     */
	    private BufferedImage displayImage;

	    /**
	     * 构造一个没有背景图片的JImagePane
	     */
	    public XImagePanel()
	    {
	        this(null, CENTER);
	    }
	    
	    /**
	     * 构造一个具有指定背景图片和指定显示模式的JImagePane
	     * @param image 背景图片
	     * @param mode 背景图片显示模式
	     */
	    public XImagePanel(Image image, String mode)
	    {
	        super();
	        this.image = image;
	        this.mode = mode;
	        this.alpha = 1.0f;
	        this.gridColor = Color.BLACK;
	        this.cornersSize = new int[]{0, 0, 0, 0};
	        super.setOpaque(false);
	    }
	    
	    /**
	     * 设置背景图片
	     * @param image 背景图片
	     */
	    public void setImage(Image image)
	    {
	        this.image = image;
	        this.repaint();
	    }

	    /**
	     * 获取背景图片
	     * @return 背景图片
	     */
	    public Image getImage()
	    {
	        return image;
	    }

	    /**
	     * 设置背景图片显示模式
	     * @param mode 模式名称，取值仅限于JImagePane.TILED、JImagePane.SCALED、JImagePane.CENTRE
	     */
	    public void setMode(String mode)
	    {
	        this.mode = mode;
	        this.repaint();
	    }

	    /**
	     * 获取背景图片显示模式
	     * @return 显示模式
	     */
	    public String getMode()
	    {
	        return mode;
	    }
	    
	    /**
	     * 获取背景图片的透明度
	     * @return 透明度
	     */
	    public float getAlpha()
	    {
	        return alpha;
	    }

	    /**
	     * 设置背景图片的透明度
	     * @param alpha 透明度，0.0f到1.0f之间
	     */
	    public void setAlpha(float alpha)
	    {
	        if(alpha >= 0.0f && alpha <= 1.0f)
	        {
	            this.alpha = alpha;
	            this.repaint();
	        }
	        else
	        {
	            throw new IllegalArgumentException("Invalid alpha:" + alpha);
	        }
	    }
	    
	    /**
	     * 获取旋转的角度
	     * @return 旋转的角度
	     */
	    public double getAngle()
	    {
	        return angle;
	    }

	    /**
	     * 设置旋转的角度
	     * @param angle 旋转的角度
	     */
	    public void setAngle(double angle)
	    {
	        this.angle = angle;
	        this.repaint();
	    }
	    
	    /**
	     * 获取是否绘制图片的边框
	     * @return boolean
	     */
	    public boolean isShowGrid()
	    {
	        return showGrid;
	    }

	    /**
	     * 设置是否绘制图片的边框
	     * @param showGrid 是否绘制边框
	     */
	    public void setShowGrid(boolean showGrid)
	    {
	        this.showGrid = showGrid;
	        this.repaint();
	    }
	    
	    /**
	     * 获取图片边框的颜色
	     * @return 边框颜色
	     */
	    public Color getGridColor()
	    {
	        return gridColor;
	    }

	    /**
	     * 设置图片边框的颜色
	     * @param gridColor 边框颜色
	     */
	    public void setGridColor(Color gridColor)
	    {
	        this.gridColor = gridColor == null? Color.BLACK: gridColor;
	        this.repaint();
	    }
	    
	    /**
	     * 获取在拉伸模式中是否保持长宽比
	     * @return 是否保持长宽比 
	     */
	    public boolean isKeepAspectRatio()
	    {
	        return keepAspectRatio;
	    }

	    /**
	     * 设置在拉伸模式中是否保持长宽比
	     * @param keepAspectRatio 是否保持长宽比 
	     */
	    public void setKeepAspectRatio(boolean keepAspectRatio)
	    {
	        this.keepAspectRatio = keepAspectRatio;
	        this.repaint();
	    }

	    /**
	     * 获取在拉伸模式中且保持长宽比的情况下是否填充整个区域
	     * @return 是否填充整个区域
	     */
	    public boolean isFilledAll()
	    {
	        return filledAll;
	    }

	    /**
	     * 设置在拉伸模式中且保持长宽比的情况下是否填充整个区域（按照图像本身的长宽比与画布的大小，填充时有可能隐藏掉部分图像）
	     * @param filledAll 是否填充整个区域
	     */
	    public void setFilledAll(boolean filledAll)
	    {
	        this.filledAll = filledAll;
	        this.repaint();
	    }
	    
	    /**
	     * 获取是否在Border区域绘制图像
	     * @return 是否在Border区域绘制图像
	     */
	    public boolean isFilledBorderArea()
	    {
	        return filledBorderArea;
	    }

	    /**
	     * 设置是否在Border区域绘制图像
	     * @param filledBorderArea 是否在Border区域绘制图像
	     */
	    public void setFilledBorderArea(boolean filledBorderArea)
	    {
	        this.filledBorderArea = filledBorderArea;
	        this.repaint();
	    }
	    
	    /**
	     * 获取背景是否只显示图像
	     * @return 背景是否只显示图像
	     */
	    public boolean isImageOnly()
	    {
	        return imageOnly;
	    }

	    /**
	     * 设置背景是否只显示图像
	     * @param imageOnly 背景是否只显示图像
	     */
	    public void setImageOnly(boolean imageOnly)
	    {
	        this.imageOnly = imageOnly;
	        this.repaint();
	    }
	    
	    /**
	     * 获取指定角的圆弧大小
	     * @param cornerIndex 角的编号，约定左上角为1，顺时针递增
	     * @return 圆弧大小
	     */
	    public int getCornerSizeAt(int cornerIndex)
	    {
	        if(cornerIndex >= 1 && cornerIndex <= 4)
	        {
	            return cornersSize[cornerIndex - 1];
	        }
	        else
	        {
	            return -1;
	        }
	    }

	    /**
	     * 设置指定角的圆弧大小
	     * @param cornerIndex 角的编号，约定左上角为1，顺时针递增
	     * @param size 圆弧大小
	     */
	    public void setCornerSizeAt(int cornerIndex, int size)
	    {
	        if(cornerIndex >= 1 && cornerIndex <= 4)
	        {
	            cornersSize[cornerIndex - 1] = size;
	            this.repaint();
	        }
	    }
	    
	    /**
	     * 获取图片的合适大小
	     * @return 图片的合适大小
	     */
	    public Dimension getImagePreferredSize()
	    {
	        Dimension size = new Dimension();
	        
	        if(image != null)
	        {
	            double theta = Math.toRadians(angle % 360);
	            int imageWidth = image.getWidth(this);
	            int imageHeight = image.getHeight(this);
	            double frameWidth = Math.abs(Math.cos(theta) * imageWidth) + Math.abs(Math.sin(theta) * imageHeight);
	            double frameHeight = Math.abs(Math.sin(theta) * imageWidth) + Math.abs(Math.cos(theta) * imageHeight);
	            size.setSize(frameWidth, frameHeight);
	        }
	        
	        return size;
	    }
	    
	    /**
	     * 获取当前所呈现的图像
	     * @param allArea 是否为当前展示的整个区域的图像
	     * @return allArea为true时，返回当前UI中呈现的图像；否则返回原始图像经旋转、透明处理之后的图像，此时并未包括呈现时的居中、拉伸、平铺处理
	     */
	    public BufferedImage getCurrentImage(boolean allArea)
	    {
	        return allArea? displayImage: currentImage;
	    }
	    
	    /**
	     * 按照旋转参数构造新的BufferedImage
	     * @return BufferedImage
	     */
	    private BufferedImage createImage()
	    {
	        if(image != null)
	        {
	            Dimension size = getImagePreferredSize();
	            BufferedImage bufferedImage = SwingUtil.getGraphicsConfiguration(this).createCompatibleImage(size.width,
	                            size.height, Transparency.TRANSLUCENT);
	            Graphics2D imageG2d = (Graphics2D)bufferedImage.getGraphics();
	            AffineTransform trans = imageG2d.getTransform();
	            Object oldHint = imageG2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
	            Composite oldComposite = imageG2d.getComposite();
	            double theta = Math.toRadians(angle % 360);
	            double anchorx = size.width / 2.0;
	            double anchory = size.height / 2.0;
	            int x = (size.width - image.getWidth(this)) / 2;
	            int y = (size.height - image.getHeight(this)) / 2;
	            
	            imageG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            imageG2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
	            trans.rotate(theta, anchorx, anchory);
	            imageG2d.setTransform(trans);
	            imageG2d.drawImage(image, x, y, this);
	            trans.rotate(-theta, anchorx, anchory);
	            imageG2d.setTransform(trans);
	            imageG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
	            imageG2d.setComposite(oldComposite);
	            imageG2d.dispose();
	            return bufferedImage;
	        }
	        else
	        {
	            return null;
	        }
	    }

	    /**
	     * 绘制组件
	     * @see javax.swing.JComponent#paintComponent(Graphics)
	     */
	    @Override
	    protected void paintComponent(Graphics g)
	    {
	        Graphics2D g2d = (Graphics2D)g;
	        
	        if(!imageOnly)
	        {
	            Object oldHints = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            g2d.setColor(this.getBackground());
	            g2d.fillPolygon(SwingUtil.createRoundRect(0, 0, this.getWidth(), this.getHeight(), cornersSize[0],
	                            cornersSize[1], cornersSize[2], cornersSize[3]));
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHints);
	        }
	        
	        currentImage = createImage();
	        Insets insets = filledBorderArea? new Insets(0, 0, 0, 0): this.getInsets();
	        int width = this.getWidth() - insets.left - insets.right;
	        int height = this.getHeight() - insets.top - insets.bottom;
	        
	        //如果设置了背景图片且图片区域可见则显示
	        if(currentImage != null && width > 0 && height > 0)
	        {
	            displayImage = SwingUtil.getGraphicsConfiguration(this).createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	            Graphics2D areaG2d = (Graphics2D)displayImage.getGraphics();
	            int imageWidth = currentImage.getWidth(this);
	            int imageHeight = currentImage.getHeight(this);
	            Color color = new Color(gridColor.getRed(), gridColor.getGreen(), gridColor.getBlue(), (int)Math.round(alpha * 255));
	            
	            //居中
	            if(mode.equalsIgnoreCase(CENTER))
	            {
	                int x = (width - imageWidth) / 2;
	                int y = (height - imageHeight) / 2;
	                areaG2d.drawImage(currentImage, x, y, this);
	                
	                if(showGrid)
	                {
	                    areaG2d.setColor(color);
	                    areaG2d.drawRect(x, y, imageWidth - 1, imageHeight - 1);
	                }
	            }
	            //平铺
	            else if(mode.equalsIgnoreCase(TILED))
	            {
	                for(int ix = 0; ix < width; ix += imageWidth)
	                {
	                    for(int iy = 0; iy < height; iy += imageHeight)
	                    {
	                        areaG2d.drawImage(currentImage, ix, iy, this);
	                    }
	                }
	                
	                if(showGrid)
	                {
	                    areaG2d.setColor(color);
	                    int ix = 0, iy = 0;
	                    
	                    for(ix = 0; ix < width; ix += imageWidth)
	                    {
	                        areaG2d.drawLine(ix, 0, ix, height - 1);
	                    }
	                    
	                    for(iy = 0; iy < height; iy += imageHeight)
	                    {
	                        areaG2d.drawLine(0, iy, width - 1, iy);
	                    }
	                    
	                    if(width % imageWidth == 0)
	                    {
	                        areaG2d.drawLine(width - 1, 0, width - 1, height - 1);
	                    }
	                    
	                    if(height % imageHeight == 0)
	                    {
	                        areaG2d.drawLine(0, height - 1, width - 1, height - 1);
	                    }
	                }
	            }
	            //拉伸
	            else if(mode.equalsIgnoreCase(SCALED))
	            {
	                int x = 0, y = 0;
	                
	                if(keepAspectRatio)
	                {
	                    float widthRatio = (float)width / imageWidth;
	                    float heightRatio = (float)height / imageHeight;
	                    float ratio;
	                    
	                    if(filledAll)
	                    {
	                        ratio = Math.max(widthRatio, heightRatio);
	                        imageWidth = (int)(imageWidth * ratio);
	                        imageHeight = (int)(imageHeight * ratio);
	                    }
	                    else
	                    {
	                        ratio = Math.min(widthRatio, heightRatio);
	                        imageWidth = (int)(imageWidth * ratio);
	                        imageHeight = (int)(imageHeight * ratio);
	                        x = (width - imageWidth) / 2;
	                        y = (height - imageHeight) / 2;
	                    }
	                }
	                else
	                {
	                    imageWidth = width;
	                    imageHeight = height;
	                }
	                
	                areaG2d.drawImage(currentImage, x, y, imageWidth, imageHeight, this);
	                
	                if(showGrid)
	                {
	                    areaG2d.setColor(color);
	                    areaG2d.drawRect(x, y, imageWidth - 1, imageHeight - 1);
	                }
	            }
	            
	            areaG2d.dispose();
	            Paint oldPaint = g2d.getPaint();
	            Object oldHints = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            g2d.setPaint(SwingUtil.createTexturePaint(displayImage, this));
	            g2d.translate(insets.left, insets.top);
	            g2d.fillPolygon(SwingUtil.createRoundRect(0, 0, displayImage.getWidth(), displayImage.getHeight(),
	                            cornersSize[0], cornersSize[1], cornersSize[2], cornersSize[3]));
	            g2d.translate(-insets.left, -insets.top);
	            g2d.setPaint(oldPaint);
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHints);
	        }
	        else
	        {
	            displayImage = null;
	        }
	    }
	    
	    /**
	     * 屏蔽父类的setOpaque
	     * 一个很诡异的问题，setOpaque(true)后若背景半透明，则动态改变背景是不会立即刷新的，其子组件改变背景时也会受影响。
	     * 故在构造方法中添加super.setOpaque(false);，然后屏蔽父类的setOpaque使它的opaque属性永远为false。
	     * 原来opaque属性的功能由imageOnly间接代替，该问题同样存在于其他组件中。
	     */
	    @Deprecated
	    public void setOpaque(boolean isOpaque)
	    {}
}
