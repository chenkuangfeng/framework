/**
 * �������������
 */
package com.ubsoft.framework.rpc.server.boot;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.container.Container;
import com.ubsoft.framework.core.conf.AppConfig;

/**
 * Main. (API, Static, ThreadSafe)
 * 
 * @author william.liangf
 */
public class Main {
	private static volatile boolean running = true;
	static {
		//加载日志
		String path = Main.class.getClassLoader().getResource("conf/log4j.properties").getPath();		
		AppConfig.initLog4j(path);
		
	}

	protected static Logger logger = Logger.getLogger(Main.class);;
	private static Container container = new ServerContainer();

	public static void main(String[] args) {
		try {

			long startTime=System.currentTimeMillis();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						container.stop();
					} catch (Throwable t) {
						logger.error(t.getMessage());
					}
					synchronized (Main.class) {
						running = false;
						Main.class.notify();
					}
				}

			});
			container.start();
			long endTime=System.currentTimeMillis();
			int s=(int)((endTime-startTime)/1000);
			logger.info("Server startup in "+s+"s");

		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error(e.getMessage());

			System.exit(1);
		}
		synchronized (Main.class) {
			while (running) {
				try {
					Main.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}
}