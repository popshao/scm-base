package com.gangling.scm.base.middleware.datasource.generator.plugin;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;

import javax.persistence.Column;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LombokPlugin extends PluginAdapter {

    private FullyQualifiedJavaType dataAnnotation;

    public LombokPlugin() {
        dataAnnotation = new FullyQualifiedJavaType("lombok.Data");
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 拦截 普通字段
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @SneakyThrows
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        generateModelFile(topLevelClass, introspectedTable, "DTO");
        generateModelFile(topLevelClass, introspectedTable, "Param");
        return true;
    }

    private void generateModelFile(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String fileType) throws IOException, TemplateException {
        String packageDto = "com.gangling.scm.base.middleware.datasource.domain";

        String packageDomain = topLevelClass.getType().getPackageName();
        String[] mulu = packageDto.split("\\.");
        String moduleName = mulu[mulu.length - 1];
        String fileName = topLevelClass.getType().getShortName();
        String path = introspectedTable.getContext().getJavaModelGeneratorConfiguration().getTargetProject() + "/" + packageDto.replaceAll("\\.", "/");
        File catalog = new File(path);
        catalog.mkdirs();
        File mapperFile = new File(path + '/' + fileName + fileType + ".java");

        Template template = FreeMarkerTemplateUtils.getTemplate(fileType.toLowerCase() + ".ftl") ;
        try (FileOutputStream fos = new FileOutputStream(mapperFile)) {
            Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
            Map<String, Object> dataMap = new HashMap<>();

            dataMap.put("package_dto", packageDto);
            dataMap.put("package_domain", packageDomain);
            dataMap.put("module_name", moduleName);
            dataMap.put("file_name", fileName);
            dataMap.put("date", new Date());
            dataMap.put("file_package", builPackage(topLevelClass.getImportedTypes()));
            dataMap.put("file_data", buildContent(topLevelClass.getFormattedContent()));

            template.process(dataMap, out);
        }
    }

    private String builPackage(Set<FullyQualifiedJavaType> importedTypes) {
        final StringBuilder sb = new StringBuilder();

        Set<String> importStrings = OutputUtilities.calculateImports(importedTypes);
        importStrings.forEach(importedType -> {
            if (importedType.contains("com.gangling.scm.base.common.entity.BaseEntity")
                || importedType.contains("javax.persistence.*")) {
                return;
            }
            sb.append(importedType);
            OutputUtilities.newLine(sb);
        });

        return sb.toString();
    }

    private String buildContent(String content) {
        content = content.substring(content.indexOf("BaseEntity {") + 12, content.length() - 1);
        while(content.contains("@Column")) {
            String tempContent = content.substring(content.indexOf("@Column"), content.indexOf("\")") + 2);
            content = content.replace(tempContent, "");
        }

        return content;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (topLevelClass != null) {
            topLevelClass.setSuperClass(new FullyQualifiedJavaType("com.gangling.scm.base.middleware.datasource.mapper.BaseMapper"));
        }
        return true;
    }

    /**
     * 拦截 主键
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    /**
     * 拦截 blob 类型字段
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    /**
     * Prevents all getters from being generated.
     * See SimpleModelGenerator
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        // 忽略id 使用基类
        if (field.getName().equals("id")) return false;
        return true;
    }

    /**
     * Prevents all setters from being generated
     * See SimpleModelGenerator
     *
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return false;
    }

    /**
     * Adds the @Data lombok import and annotation to the class
     *
     * @param topLevelClass
     */
    protected void addDataAnnotation(TopLevelClass topLevelClass) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Getter");
        topLevelClass.addImportedType("lombok.Setter");
        topLevelClass.addImportedType("com.gangling.scm.base.common.entity.BaseEntity");

        //添加domain的注解
        topLevelClass.addAnnotation("@Getter");
        topLevelClass.addAnnotation("@Setter");

        // 设置model继承基类
        topLevelClass.setSuperClass(new FullyQualifiedJavaType("com.gangling.scm.base.common.entity.BaseEntity"));
    }

}
