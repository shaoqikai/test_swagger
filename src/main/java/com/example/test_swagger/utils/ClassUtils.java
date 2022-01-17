package com.example.test_swagger.utils;

import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author shaoqk
 * @create 2022-01-10 11:13
 */
public class ClassUtils {

    private static final String FILE = "file";
    private static final String JAR = "jar";

    public static ArrayList<Class> getAllClassByInterface(Class clazz) throws IOException {
        Assert.notNull(clazz, "参数为空");
        ArrayList<Class> list = new ArrayList<>();
        // 判断是否是一个接口
        if (clazz.isInterface()) {
            ArrayList<Class> allClass = getAllClass(clazz.getPackage().getName());
            // 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己

            for (Class aClass : allClass) {
                // 判断是不是同一个接口
                // isAssignableFrom:判定此 Class 对象所表示的类或接口与指定的 Class
                // 参数所表示的类或接口是否相同，或是否是其超类或超接口
                if (clazz.isAssignableFrom(aClass)) {
                    if (!clazz.equals(aClass)) {
                        // 自身并不加进去
                        list.add(aClass);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 从一个指定路径下查找所有的类
     *
     * @param packagename 包名
     */
    private static ArrayList<Class> getAllClass(String packagename) throws IOException {
        List<String> classNameList = getClassName(packagename);
        ArrayList<Class> list = new ArrayList<>();

        for (String className : classNameList) {
            Class clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException ignored) {

            }
            if (clazz != null) {
                list.add(clazz);
            }
        }
        return list;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) throws IOException {

        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (FILE.equals(type)) {
                String fileSearchPath = url.getPath();
                fileSearchPath = fileSearchPath.substring(0, fileSearchPath.indexOf("/classes"));
                fileNames = getClassNameByFile(fileSearchPath);
            } else if (JAR.equals(type)) {
                JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarUrlConnection.getJarFile();
                fileNames = getClassNameByJar(jarFile);
            } else {
                throw new RuntimeException("file system not support! cannot load MsgProcessor！");
            }
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(String filePath) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<>();
        File file = new File(URLDecoder.decode(filePath, "utf-8"));
        File[] childFiles = file.listFiles();
        assert childFiles != null;
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(getClassNameByFile(childFile.getPath()));
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(JarFile jarFile) {
        List<String> myClassName = new ArrayList<>();
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry jarEntry = entrys.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.endsWith(".class")) {
                entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                myClassName.add(entryName);
            }
        }
        return myClassName;
    }


    public static Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<>(500);
        Class<?> currentClass = cls;
        while (currentClass != null && currentClass != Object.class) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            //获取接口中的所有方法
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
            //获取父类，继续while循环
            currentClass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[0]);
    }

    public static void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                //获取方法的签名，格式是：返回值类型#方法名称:参数类型列表
                String signature = getSignature(currentMethod);
                //检查是否在子类中已经添加过该方法，如果在子类中已经添加过，则表示子类覆盖了该方法，无须再向uniqueMethods集合中添加该方法了
                if (!uniqueMethods.containsKey(signature)) {
                    if (canControlMemberAccessible()) {
                        currentMethod.setAccessible(true);
                    }
                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    public static String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        sb.append(returnType.getName()).append('#');
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }

    /**
     * Checks whether can control member accessible.
     *
     * @return If can control member accessible, it return {@literal true}
     * @since 3.5.0
     */
    public static boolean canControlMemberAccessible() {
        SecurityManager securityManager = System.getSecurityManager();
        if (null != securityManager) {
            securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
        }
        return true;
    }
}
