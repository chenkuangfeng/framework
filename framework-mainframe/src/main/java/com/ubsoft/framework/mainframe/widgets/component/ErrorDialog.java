package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.dialog.StandardDialogPane;
import com.jidesoft.swing.JideBoxLayout;

public class ErrorDialog  extends StandardDialog{

	 public JComponent _detailsPanel;
	 
	 String detail=null;
	 String title =null; 
	    public ErrorDialog(String title,String detail){	    		    	
	    	 super((Frame) null, "异常信息");
	    	 this.detail=detail;
			  this.title=title;
	    }
	  
	    @Override
	    public JComponent createBannerPanel() {
	        return null;
	    }

	    public JComponent createDetailsPanel() {
	    	JTextArea detailText= new JTextArea(detail);
	    	
	    	detailText.setRows(15);
	        JLabel label = new JLabel("详细信息:");
	     
	        JPanel panel = new JPanel(new BorderLayout(6, 6));
	        JScrollPane spane= new JScrollPane(detailText);
	        spane.setPreferredSize(new Dimension(450,250));
	        panel.add(spane);
	        panel.add(label, BorderLayout.BEFORE_FIRST_LINE);
	        label.setLabelFor(detailText);
	        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	        return panel;
	    }

	    @Override
	    protected StandardDialogPane createStandardDialogPane() {
	        DefaultStandardDialogPane dialogPane = new DefaultStandardDialogPane() {
	            @Override
	            protected void layoutComponents(Component bannerPanel, Component contentPanel, ButtonPanel buttonPanel) {
	                setLayout(new JideBoxLayout(this, BoxLayout.Y_AXIS));
	                if (bannerPanel != null) {
	                    add(bannerPanel);
	                }
	                if (contentPanel != null) {
	                    add(contentPanel);
	                }
	                add(buttonPanel, JideBoxLayout.FIX);
	                _detailsPanel = createDetailsPanel();
	                add(_detailsPanel, JideBoxLayout.VARY);
	                _detailsPanel.setVisible(false);
	            }
	        };
	        return dialogPane;
	    }

	    @Override
	    public JComponent createContentPanel() {
	        JPanel panel = new JPanel(new BorderLayout(5, 5));
	        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
	       
	        JLabel titleLabel = new JLabel(title);
	        titleLabel.setPreferredSize(new Dimension(450,10));
	        
	        titleLabel.setIcon(IconFactory.getImageIcon("icon/error.png"));
	        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

	        panel.add(titleLabel, BorderLayout.CENTER);
	        return panel;
	    }

	    @Override
	    public ButtonPanel createButtonPanel() {
	        ButtonPanel buttonPanel = new ButtonPanel();
	        JButton closeButton = new JButton();
	        JButton detailButton = new JButton();
	        detailButton.setMnemonic('D');
	        closeButton.setName(OK);
	        buttonPanel.addButton(closeButton, ButtonPanel.AFFIRMATIVE_BUTTON);
	        buttonPanel.addButton(detailButton, ButtonPanel.OTHER_BUTTON);

	        closeButton.setAction(new AbstractAction("关闭") {
	            public void actionPerformed(ActionEvent e) {
	                setDialogResult(RESULT_AFFIRMED);
	                setVisible(false);
	                dispose();
	            }
	        });

	        detailButton.setAction(new AbstractAction("详细信息 >>") {
	            public void actionPerformed(ActionEvent e) {
	                if (_detailsPanel.isVisible()) {
	                    _detailsPanel.setVisible(false);
	                    putValue(Action.NAME, "详细信息 <<");
	                    pack();
	                }
	                else {
	                    _detailsPanel.setVisible(true);
	                    putValue(Action.NAME, "<< 详细信息");
	                    pack();
	                }
	            }
	        });
	        setDefaultCancelAction(closeButton.getAction());
	        setDefaultAction(closeButton.getAction());
	        getRootPane().setDefaultButton(closeButton);
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        buttonPanel.setSizeConstraint(ButtonPanel.NO_LESS_THAN); // since the checkbox is quite wide, we don't want all of them have the same size.
	        return buttonPanel;
	    }

}
