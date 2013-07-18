package startup;

import apps.fis.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppsStartManager {

    private static final Logger logger = LoggerFactory.getLogger(AppsStartManager.class);
    private static Map<String, ApplicationContext> contextMap;

    private AppsStartManager() {
    }

    public static void init() throws IOException {
        logger.info("apps springContext初始化开始.......");
        contextMap = new HashMap<String, ApplicationContext>();
        String initFileNames = common.utils.PropertyManager.getProperty("register.init.filename");
        String[] files = initFileNames.split(",");
        for(String fileName : files) {
            if(fileName.contains("applicationContext")) {
                logger.info("Loading...: " + fileName);
                String key = fileName.substring(0, fileName.indexOf("_"));
                ApplicationContext appContext = new ClassPathXmlApplicationContext(fileName);
                contextMap.put(key, appContext);
            }
        }
        logger.info("AppsStartManager初始化完成。");
    }

    public static Object getBean(String appKey, String beanId) {
        return contextMap.get(appKey).getBean(beanId);
    }
}
