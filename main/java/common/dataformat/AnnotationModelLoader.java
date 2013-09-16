
package common.dataformat;

import common.dataformat.annotation.FixedLengthRecord;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.SeperatedTextMessage;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * User: zhanrui
 * Date: 13-9-8
 */
public class AnnotationModelLoader {
    private Set<Class<? extends Annotation>> annotations;

    public AnnotationModelLoader() {
        annotations = new LinkedHashSet<Class<? extends Annotation>>();
        annotations.add(SeperatedTextMessage.class);
        annotations.add(FixedLengthRecord.class);
        annotations.add(Link.class);
    }

    public Set<Class<?>> loadModels(String... packageNames) throws Exception {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        for (String pkg : packageNames) {
            find(pkg, classes);
        }
        return classes;
    }

    private void find(String packageName, Set<Class<?>> classes) throws Exception {
        packageName = packageName.replace('.', '/');

        /*
                if (!packageName.endsWith("/")) {
                    packageName = packageName + "/";
                }
        */
        Enumeration<URL> urls = this.getClass().getClassLoader().getResources(packageName);
        while (urls.hasMoreElements()) {
            URL url = null;
            url = urls.nextElement();
            String urlPath = url.getFile();
            urlPath = URLDecoder.decode(urlPath, "UTF-8");

            if (urlPath.startsWith("file:")) {
                try {
                    urlPath = new URI(url.getFile()).getPath();
                } catch (URISyntaxException e) {
                    //TODO
                }

                if (urlPath.startsWith("file:")) {
                    urlPath = urlPath.substring(5);
                }
            }

            //TODO JAR 或 osgi bundle 的情况

            File file = new File(urlPath);
            if (file.isDirectory()) {
                loadImplementationsInDirectory(packageName, file, classes);
            } else {
                //TODO  非本地文件方式暂不支持
                throw new RuntimeException("Urlpath is not supported.");
            }
        }
    }

    private void loadImplementationsInDirectory(String parent, File location, Set<Class<?>> classes) throws ClassNotFoundException {
        File[] files = location.listFiles();
        StringBuilder builder;

        for (File file : files) {
            builder = new StringBuilder(100);
            String name = file.getName();
            if (name != null) {
                name = name.trim();
                builder.append(parent).append("/").append(name);
                String packageOrClass = parent == null ? name : builder.toString();

                if (file.isDirectory()) {
                    loadImplementationsInDirectory(packageOrClass, file, classes);
                } else if (name.endsWith(".class")) {
                    String externalName = packageOrClass.substring(0, packageOrClass.indexOf('.')).replace('/', '.');
                    Class<?> type = this.getClass().getClassLoader().loadClass(externalName);
                    //判断是否符合报文类注解
                    if (matches(type)) {
                        classes.add(type);
                    }
                }
            }
        }
    }

    private boolean matches(Class<?> type) {
        if (type == null) {
            return false;
        }
        for (Class<? extends Annotation> annotation : annotations) {
            if (hasAnnotation(type, annotation)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnnotation(AnnotatedElement elem, Class<? extends Annotation> annotationType) {
        if (elem.isAnnotationPresent(annotationType)) {
            return true;
        }
        for (Annotation a : elem.getAnnotations()) {
            for (Annotation meta : a.annotationType().getAnnotations()) {
                if (meta.annotationType().getName().equals(annotationType.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
