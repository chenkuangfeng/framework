package com.borland.dbswing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import com.borland.dx.dataset.CustomPaintSite;

public class TableFastStringRenderer extends JComponent
  implements TableCellRenderer, CustomPaintSite, Serializable
{
  private static final long serialVersionUID = 6759774116392137742L;
  private String d;
  private int f = 2;
  private int _ = 0;
  private int a;
  private Insets b;
  private Border Z;
  private int V = 0;
  private int e = 0;
  private Border c = new EmptyBorder(1, 2, 1, 2);
  private int X;
  private Color W;
  private Color Y;
  private Insets U;
  private Font T;

  public TableFastStringRenderer()
  {
    setDefaultMargins(new Insets(2, 2, 2, 2));
  }

  public Component getTableCellRendererComponent(JTable paramJTable, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
  {
    if (this.W == null)
      setForeground(paramBoolean1 ? paramJTable.getSelectionForeground() : paramJTable.getForeground());
    if (this.Y == null)
      setBackground(paramBoolean1 ? paramJTable.getSelectionBackground() : paramJTable.getBackground());
    setDefaultFont(paramJTable.getFont());
    if (paramBoolean2)
    {
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      if ((paramJTable.isCellEditable(paramInt1, paramInt2)) && ((paramJTable instanceof JdbTable)))
      {
        JdbTable localJdbTable = (JdbTable)paramJTable;
        if (localJdbTable.getEditableFocusedCellForeground() != null)
          setDefaultForeground(localJdbTable.getEditableFocusedCellForeground());
        if (localJdbTable.getEditableFocusedCellBackground() != null)
          setDefaultBackground(localJdbTable.getEditableFocusedCellBackground());
      }
    }
    else
    {
      setForeground(paramBoolean1 ? paramJTable.getSelectionForeground() : this.W);
      setBackground(paramBoolean1 ? paramJTable.getSelectionBackground() : this.Y);
      setBorder(this.c);
    }
    setValue(paramObject);
    return this;
  }

  public void setDefaultForeground(Color paramColor)
  {
    this.W = paramColor;
    setForeground(paramColor);
  }

  public void setDefaultBackground(Color paramColor)
  {
    this.Y = paramColor;
    setBackground(paramColor);
  }

  public void setDefaultAlignment(int paramInt)
  {
    this.X = paramInt;
    C(paramInt);
  }

  private void C(int paramInt)
  {
    setHorizontalAlignment(DBUtilities.convertJBCLToSwingAlignment(paramInt, true));
    setVerticalAlignment(DBUtilities.convertJBCLToSwingAlignment(paramInt, false));
  }

  public void setDefaultFont(Font paramFont)
  {
    this.T = paramFont;
    setFont(paramFont);
  }

  public void setDefaultMargins(Insets paramInsets)
  {
    this.U = paramInsets;
    setItemMargins(paramInsets);
  }

  public void reset()
  {
    setForeground(this.W);
    setBackground(this.Y);
    setFont(this.T);
    C(this.X);
    setItemMargins(this.U);
  }

  public void setAlignment(int paramInt)
  {
    this.a = paramInt;
    C(paramInt);
  }

  public void setItemMargins(Insets paramInsets)
  {
    setMargins(paramInsets);
  }

  public boolean isTransparent()
  {
    return false;
  }

  public int getAlignment()
  {
    return this.a;
  }

  public Insets getItemMargins()
  {
    return this.b;
  }

  public Component getSiteComponent()
  {
    return this;
  }

  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.V = paramInt3;
    this.e = paramInt4;
  }

  public void setValue(Object paramObject)
  {
    this.d = (paramObject == null ? "" : paramObject.toString());
  }

  public void setHorizontalAlignment(int paramInt)
  {
    this.f = paramInt;
  }

  public void setVerticalAlignment(int paramInt)
  {
    this._ = paramInt;
  }

  public void setMargins(Insets paramInsets)
  {
    this.b = paramInsets;
  }

  public void setBorder(Border paramBorder)
  {
    this.Z = paramBorder;
  }

  public Border getBorder()
  {
    return this.Z;
  }

  public void paint(Graphics paramGraphics)
  {
    Font localFont = paramGraphics.getFont();
    Color localColor = paramGraphics.getColor();
    paramGraphics.setFont(getFont());
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics(getFont());
    int i;
    switch (this.f)
    {
    case 1:
    case 2:
    case 3:
    default:
      i = this.b.left;
      break;
    case 0:
      i = (this.V - localFontMetrics.stringWidth(this.d)) / 2;
      break;
    case 4:
      i = this.V - localFontMetrics.stringWidth(this.d) - this.b.right;
    }
    int j;
    switch (this._)
    {
    case 1:
    case 2:
    default:
      j = this.b.top;
      break;
    case 0:
      j = (this.e - localFontMetrics.getHeight()) / 2;
      break;
    case 3:
      j = this.e - localFontMetrics.getHeight() - this.b.bottom;
    }
    j += localFontMetrics.getLeading() + localFontMetrics.getAscent();
    paramGraphics.setColor(getBackground());
    paramGraphics.fillRect(0, 0, this.V, this.e);
    if (this.d != null)
    {
      paramGraphics.setColor(getForeground());
      paramGraphics.drawString(this.d, i, j);
    }
    this.Z.paintBorder(this, paramGraphics, 0, 0, this.V, this.e);
    paramGraphics.setFont(localFont);
    paramGraphics.setColor(localColor);
  }
}