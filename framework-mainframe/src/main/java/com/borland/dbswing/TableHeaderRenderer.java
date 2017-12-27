package com.borland.dbswing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public class TableHeaderRenderer
  implements TableCellRenderer, Serializable
{
  private transient boolean alreadyInitialized = false;
  transient Object previousValue;
  transient JComponent renderer;

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
	 // MessageBox.showInfo("ffff");
    if ((this.alreadyInitialized) && (this.previousValue.equals(value))) {
    	  JPanel headerPanel=new JPanel();
    		 headerPanel.setLayout(new BorderLayout());
    		 headerPanel.add(renderer,BorderLayout.CENTER);
    		 headerPanel.add(new JTextField(),BorderLayout.NORTH);
      return headerPanel;
    }

    if (value == null) {
      if ((this.renderer == null) || ((this.renderer instanceof JList))) {
        this.renderer = new JLabel();
      }
      else {
        ((JLabel)this.renderer).setText("");
      }
    }
    else if ((value instanceof String[])) {
      if ((this.renderer == null) || ((this.renderer instanceof JLabel))) {
        this.renderer = new JList((String[])value);
      }
      else {
        ((JList)this.renderer).setListData((String[])value);
      }
      ((JList)this.renderer).setVisibleRowCount(((String[])value).length);
    }
    else
    {
      String text = value.toString();

      if (text.indexOf('\n') != -1)
      {
        String line = text;
        int lines = 0;
        int offset = 0;

        while ((offset = line.indexOf('\n')) != -1) {
          lines++;
          line = line.substring(offset + 1);
        }
        String[] textLines = new String[lines + 1];
        line = text;
        lines = 0;
        while ((offset = line.indexOf('\n')) != -1) {
          textLines[(lines++)] = line.substring(0, offset);
          line = line.substring(offset + 1);
        }
        textLines[lines] = line;

        if ((this.renderer == null) || ((this.renderer instanceof JLabel))) {
          this.renderer = new JList(textLines);
        }
        else {
          ((JList)this.renderer).setListData(textLines);
        }
        ((JList)this.renderer).setVisibleRowCount(textLines.length);
      }
      else if ((this.renderer == null) || ((this.renderer instanceof JList))) {
        this.renderer = new JLabel(text);
      }
      else {
        ((JLabel)this.renderer).setText(text);
      }

    }

    if (table != null) {
      JTableHeader header = table.getTableHeader();
      if (header != null) {
        this.renderer.setForeground(header.getForeground());
        this.renderer.setBackground(header.getBackground());
        this.renderer.setFont(header.getFont());
      }
    }

    this.renderer.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    this.renderer.setOpaque(true);
    this.previousValue = value;
    this.alreadyInitialized = true;
    JPanel headerPanel=new JPanel();
	 headerPanel.setLayout(new BorderLayout());
	 headerPanel.add(renderer,BorderLayout.CENTER);
	 headerPanel.add(new JTextField(8),BorderLayout.NORTH);
	 
    return headerPanel;
  }
}