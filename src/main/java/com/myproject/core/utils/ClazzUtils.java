package com.myproject.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClazzUtils {
	private static final String CLASS_SUFFIX = ".class";

	private static final String CLASS_FILE_PREFIX = File.separator + "classes" + File.separator;

	private static final String PACKAGE_SEPARATOR = ".";

 /**

  * 查找包下的所有类的名字

  * @param packageName

  * @param showChildPackageFlag 是否需要显示子包内容

  * @return List集合，内容为类的全名

  */

 public static List<String> getClazzName(String packageName, boolean showChildPackageFlag ) {

     List<String> result = new ArrayList<>();

     String suffixPath = packageName.replaceAll("\\.", "/");

     ClassLoader loader = Thread.currentThread().getContextClassLoader();

     try {

         Enumeration<URL> urls = loader.getResources(suffixPath);

         while(urls.hasMoreElements()) {

             URL url = urls.nextElement();

             if(url != null) {

                 String protocol = url.getProtocol();

                 if("file".equals(protocol)) {

                     String path = url.getPath();

                     System.out.println(path);

                     result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));

                 } else if("jar".equals(protocol)) {

                     JarFile jarFile = null;

                     try{

                         jarFile = ((JarURLConnection) url.openConnection()).getJarFile();

                     } catch(Exception e){

                         e.printStackTrace();

                     }

                     if(jarFile != null) {

                         result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));

                     }

                 }

             }

         }

     } catch (IOException e) {

         e.printStackTrace();

     }

     return result;

 }

 
 
 /**
  * 获取某包下所有类
  *
  * @param packageName 包名
  * @param isRecursion 是否遍历子包
  * @return 类的完整名称
  */
 public static Set<String> getClassName(String packageName, boolean isRecursion) {
     Set<String> classNames = null;
     ClassLoader loader = Thread.currentThread().getContextClassLoader();
     String packagePath = packageName.replace(".", "/");

     URL url = loader.getResource(packagePath);
     if (url != null) {
         String protocol = url.getProtocol();
         if (protocol.equals("file")) {
             classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
         } else if (protocol.equals("jar")) {
             JarFile jarFile = null;
             try {
                 jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
             } catch (Exception e) {
                 e.printStackTrace();
             }

             if (jarFile != null) {
                 getClassNameFromJar(jarFile.entries(), packageName, isRecursion);
             }
         }
     } else {
         /*从所有的jar包中查找包名*/
         classNames = getClassNameFromJars(((URLClassLoader) loader).getURLs(), packageName, isRecursion);
     }

     return classNames;
 }

 /**
  * @param jarEntries
  * @param packageName
  * @param isRecursion
  * @return
  */
 private static Set<String> getClassNameFromJar(Enumeration<JarEntry> jarEntries, String packageName, boolean isRecursion) {
     Set<String> classNames = new HashSet<String>();

     while (jarEntries.hasMoreElements()) {
         JarEntry jarEntry = jarEntries.nextElement();
         if (!jarEntry.isDirectory()) {
             /*
              * 这里是为了方便，先把"/" 转成 "." 再判�? ".class" 的做法可能会有bug
              * (FIXME: 先把"/" 转成 "." 再判�? ".class" 的做法可能会有bug)
              */
             String entryName = jarEntry.getName().replace("/", ".");
             if (entryName.endsWith(".class") && !entryName.contains("$") && entryName.startsWith(packageName)) {
                 entryName = entryName.replace(".class", "");
                 if (isRecursion) {
                     classNames.add(entryName);
                 } else if (!entryName.replace(packageName + ".", "").contains(".")) {
                     classNames.add(entryName);
                 }
             }
         }
     }

     return classNames;
 }
 
 /**
  * 从所有jar中搜索该包，并获取该包下�?有类
  *
  * @param urls        URL集合
  * @param packageName 包路�?
  * @param isRecursion 是否遍历子包
  * @return 类的完整名称
  */
 private static Set<String> getClassNameFromJars(URL[] urls, String packageName, boolean isRecursion) {
     Set<String> classNames = new HashSet<String>();

     for (int i = 0; i < urls.length; i++) {
         String classPath = urls[i].getPath();

         //不必搜索classes文件�?
         if (classPath.endsWith("classes/")) {
             continue;
         }

         JarFile jarFile = null;
         try {
             jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
         } catch (IOException e) {
             e.printStackTrace();
         }

         if (jarFile != null) {
             classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion));
         }
     }

     return classNames;
 }
 
 /**
  * 从项目文件获取某包下�?有类
  *
  * @param filePath    文件路径
  * @param className   类名集合
  * @param isRecursion 是否遍历子包
  * @return 类的完整名称
  */
 private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
     Set<String> className = new HashSet<String>();
     File file = new File(filePath);
     File[] files = file.listFiles();
     for (File childFile : files) {
         //�?查一个对象是否是文件�?
         if (childFile.isDirectory()) {
             if (isRecursion) {
                 className.addAll(getClassNameFromDir(childFile.getPath(), packageName + "." + childFile.getName(), isRecursion));
             }
         } else {
             String fileName = childFile.getName();
             //endsWith() 方法用于测试字符串是否以指定的后�?结束�?  !fileName.contains("$") 文件名中不包�? '$'
             if (fileName.endsWith(".class") && !fileName.contains("$")) {
                 className.add(packageName + "." + fileName.replace(".class", ""));
             }
         }
     }

     return className;
 }

 /**

  * 递归获取所有class文件的名字

  * @param file 

  * @param flag  是否需要迭代遍历

  * @return List

  */

 private static List<String> getAllClassNameByFile(File file, boolean flag) {

     List<String> result =  new ArrayList<>();

     if(!file.exists()) {

         return result;

     }

     if(file.isFile()) {

         String path = file.getPath();

         // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题

         if(path.endsWith(CLASS_SUFFIX)) {

             path = path.replace(CLASS_SUFFIX, "");

             // 从"/classes/"后面开始截取

             String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())

                     .replace(File.separator, PACKAGE_SEPARATOR);

             if(-1 == clazzName.indexOf("$")) {

                 result.add(clazzName);

             }

         }

         return result;

         

     } else {

         File[] listFiles = file.listFiles();

         if(listFiles != null && listFiles.length > 0) {

             for (File f : listFiles) {

                 if(flag) {

                     result.addAll(getAllClassNameByFile(f, flag));

                 } else {

                     if(f.isFile()){

                         String path = f.getPath();

                         if(path.endsWith(CLASS_SUFFIX)) {

                             path = path.replace(CLASS_SUFFIX, "");

                             // 从"/classes/"后面开始截取

                             String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())

                                     .replace(File.separator, PACKAGE_SEPARATOR);

                             if(-1 == clazzName.indexOf("$")) {

                                 result.add(clazzName);

                             }

                         }

                     }

                 }

             }

         } 

         return result;

     }

 }

 

 

    /**
     * 递归获取jar所有class文件的名字
     * @param jarFile 
     * @param packageName 包名
     * @param flag  是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {
        List<String> result =  new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if(name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if(flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if(name.startsWith(packageName) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if(packageName.equals(name.substring(0, name.lastIndexOf("."))) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<String> list = ClazzUtils.getClazzName("com.mysql.fabric", false);
        for (String string : list) {
            System.out.println(string);
        }
    }
    
    /**
     * 以文件的形式来获取包下的所有Class
     * 
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName,
            String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                        + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    //classes.add(Class.forName(packageName + '.' + className));
                                         //经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));  
                                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 从包package中获取所有的Class
     * 
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(String pack) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(
                    packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath,
                            recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")
                                            && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(
                                                packageName.length() + 1, name
                                                        .length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class
                                                    .forName(packageName + '.'
                                                            + className));
                                        } catch (ClassNotFoundException e) {
                                            // log
                                            // .error("添加用户自定义视图类错误 找不到此类的.class文件");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    } 
    
}
