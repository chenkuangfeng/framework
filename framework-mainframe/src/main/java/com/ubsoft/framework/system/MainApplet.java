package com.ubsoft.framework.system;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.JApplet;

import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public class MainApplet extends JApplet {
	public void init() {

		this.setJMenuBar(null);
		getContentPane().setLayout(new BorderLayout());
		try {
			getContentPane().add(new MainFrame(this), BorderLayout.CENTER);
		} catch (Exception e) {
			MessageBox.showException(e);
		}
	}

	/**
	 * Gets the uRL.
	 * 
	 * @param filename
	 *            the filename
	 * @return the uRL
	 */
	public URL getURL(String filename) {
		URL codeBase = this.getCodeBase();
		URL url = null;

		try {
			url = new URL(codeBase, filename);
			System.out.println(url);
		} catch (java.net.MalformedURLException e) {
			System.out.println("Error: badly specified URL");
			return null;
		}

		return url;
	}

}
