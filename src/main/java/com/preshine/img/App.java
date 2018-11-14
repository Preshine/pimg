package com.preshine.img;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import java.lang.management.ManagementFactory;
import java.util.EnumSet;

public class App {

    private static final Logger LOGGER = LogManager.getLogger(App.class);


    public static void main(final String[] args) throws Exception {
        Integer port = 8080;
        if (args != null && args.length > 0) {
            if ("-p".equals(args[0])) {
                port = Integer.valueOf(args[1]);
            }
        }

        final Server server = new Server(port);
        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

//        context.addEventListener(new ContextLoaderListener());
//        需要设置上下文参数，否则spring默认读取 WEB-INF/spring.xml， 暂时不知道如何修改参数
//        context.addEventListener(new IntrospectorCleanupListener());

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:spring.xml");
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(new ServletHolder(dispatcherServlet), "/");
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        // 配置Spring字符集过滤器
        context.addFilter(new FilterHolder(characterEncodingFilter),
                "/", EnumSet.allOf(DispatcherType.class));
        context.insertHandler(handler);

        server.start();
        addHook(server);
        server.join();
    }

    /**
     * 注意点
     * 1.不要ShutdownHook Thread 里调用System.exit()方法，否则会造成死循环。
     * 2.如果有非守护线程，只有所有的非守护线程都结束了才会执行hook
     * 3.Thread默认都是非守护线程，创建的时候要注意
     * 4.注意线程抛出的异常，如果没有被捕获都会跑到Thread.dispatchUncaughtException
     *
     * @param Server
     */
    private static void addHook(Server server) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {

                    try {
                        server.stop();
                        String name = ManagementFactory.getRuntimeMXBean().getName();
                        String pid = name.split("@")[0];
                        String os = System.getProperty("os.name");
                        if (os != null && os.startsWith("Windows")){
                            String cmd = "Taskkill /f /IM " + pid;
                            LOGGER.info("exec: " + cmd);
                            Runtime.getRuntime().exec(cmd);
                        }else{
                            String[] cmd ={"sh", "-c", "kill -9 " + pid};
                            LOGGER.info("exec: " + cmd);
                            Runtime.getRuntime().exec(cmd);
                        }
                    } catch (Exception e) {
                        LOGGER.error("pimg server stop ex", e);
                    }
                    LOGGER.info("jvm exit, all service stopped.");

                }, "pimg-shutdown-hook-thread")
        );
    }

}
