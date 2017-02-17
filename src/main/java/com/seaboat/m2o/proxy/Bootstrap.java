package com.seaboat.m2o.proxy;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.mysql.ConnectionLogHandler;
import com.seaboat.m2o.proxy.mysql.MysqlConnectionFactory;
import com.seaboat.m2o.proxy.mysql.RegisterHandler;
import com.seaboat.net.reactor.Acceptor;
import com.seaboat.net.reactor.ReactorPool;
import com.seaboat.net.reactor.connection.ConnectionEventHandler;
import com.seaboat.net.reactor.handler.Handler;

/**
 * 
 * <pre><b>m2o proxy bootstrap.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class Bootstrap {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Bootstrap.class);
	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private static String acceptorName = M2OConfig.getInstance().server
			.getAcceptorName();
	private static String host = M2OConfig.getInstance().server.getHost();
	private static int port = M2OConfig.getInstance().server.getPort();

	public static void main(String[] args) {
		try {
			LOGGER.info("m20-proxy is starting up ......");
			Handler handler = new NetHandler();
			ReactorPool reactorPool = new ReactorPool(Runtime.getRuntime()
					.availableProcessors(), handler);
			Acceptor acceptor = new Acceptor(reactorPool, acceptorName, host,
					port);
			acceptor.addConnectionEventHandler(new RegisterHandler());
			acceptor.addConnectionEventHandler(new ConnectionLogHandler());
			acceptor.setConnectionFactory(new MysqlConnectionFactory());
			acceptor.start();
			LOGGER.info("m2o-proxy has started up successfully.");
			while (true) {
				Thread.sleep(300 * 1000);
			}
		} catch (Throwable e) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			LOGGER.error(sdf.format(new Date()) + " launch error", e);
			System.exit(-1);
		}
	}
}
