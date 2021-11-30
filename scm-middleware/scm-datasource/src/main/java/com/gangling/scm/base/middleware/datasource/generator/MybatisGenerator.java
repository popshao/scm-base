package com.gangling.scm.base.middleware.datasource.generator;


import org.mybatis.generator.api.ShellRunner;


public class MybatisGenerator {

    /**
     * 如果你的main方法启动后 没有达到预期的效果，而是
     * The specified target project directory src/main/resources does not exist
     * The specified target project directory src/main/java does not exist
     * The specified target project directory src/main/java does not exist
     * <p>
     * 报以上的错是因为 idea 多module 工程 user.dir 还是 工程的根目录,工程的根目录没有 src/main/resources 这些目录
     * 所以报错。我们需要做的是 将这个user.dir 设置成 demo/demo-persistence
     * 点击 EDIT CONFIGURATION ,里面有一列是 Working directory ,
     * 再加上 demo-persistence 即可
     * 或者可以直接写  $ModuleFileDir$ 也一样
     * <p>
     * <p>
     * 默认会生成batchInsert语句，如果不需要,可以注释掉 generatorConfig.xml 里面的
     * <plugin type="com.itfsw.mybatis.generator.plugins.BatchInsertPlugin"></plugin>
     * <plugin type="com.itfsw.mybatis.generator.plugins.ModelColumnPlugin"></plugin>
     *
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(System.getProperty("user.dir"));

        String file = Thread.currentThread().getContextClassLoader().getResource("generatorConfig.xml").getPath();
        String arg = "-configfile " + file + " -overwrite";
        ShellRunner.main(arg.split(" "));
    }

}
