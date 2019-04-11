package com.myproject.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class MyPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	
	/**   
     * 生成mapping 添加自定义sql   
     */   
    @Override  
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {  
        //创建Select查询  
        XmlElement whereSql = new XmlElement("sql");  
        whereSql.addAttribute(new Attribute("id", "whereSql"));
        XmlElement whereElement=new XmlElement("where");
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "q!=null and q!=''"));
        ifElement.addElement(new TextElement("and name like CONCAT(\"%\",#{q},\"%\")"));
        whereElement.addElement(ifElement);
        
        XmlElement ifElement1 = new XmlElement("if");
        ifElement1.addAttribute(new Attribute("test", "dataState!=null"));
        ifElement1.addElement(new TextElement("and dataState=#{dataState}"));
        whereElement.addElement(ifElement1);
        
        whereSql.addElement(whereElement);
  
        XmlElement queryPage = new XmlElement("select");  
        queryPage.addAttribute(new Attribute("id", "query"));  
        queryPage.addAttribute(new Attribute("resultMap", "BaseResultMap"));  
        queryPage.addAttribute(new Attribute("parameterType", "BaseQuery"));  
        queryPage.addElement(new TextElement("select * from "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()+" <include refid=\"whereSql\"/> <if test=\"sort!=null and sort!=''\">ORDER BY ${sort} ${order} </if> limit #{start},#{rows}"));  
        
        XmlElement queryTotal = new XmlElement("select");  
        queryTotal.addAttribute(new Attribute("id", "queryTotal"));  
        queryTotal.addAttribute(new Attribute("resultType", "int"));  
        queryTotal.addAttribute(new Attribute("parameterType", "BaseQuery"));  
        queryTotal.addElement(new TextElement("select count(*) from "+ introspectedTable.getFullyQualifiedTableNameAtRuntime()+" <include refid=\"whereSql\"/>"));  
        
        XmlElement deleteBatch = new XmlElement("delete");  
        deleteBatch.addAttribute(new Attribute("id", "deleteBatch"));  
        deleteBatch.addElement(new TextElement("delete from "+introspectedTable.getFullyQualifiedTableNameAtRuntime()+" where id in "));
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "array"));
        foreachElement.addAttribute(new Attribute("item", "id"));
        foreachElement.addAttribute(new Attribute("open", "("));
        foreachElement.addAttribute(new Attribute("close", ")"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addElement(new TextElement("#{id}"));
        deleteBatch.addElement(foreachElement);
        
        
        XmlElement deleteLogic = new XmlElement("update");  
        deleteLogic.addAttribute(new Attribute("id", "deleteLogic"));  
        deleteLogic.addElement(new TextElement("update "+introspectedTable.getFullyQualifiedTableNameAtRuntime()+" set dataState = 0 where id in "));
        XmlElement foreachElementLogicDelete = new XmlElement("foreach");
        foreachElementLogicDelete.addAttribute(new Attribute("collection", "array"));
        foreachElementLogicDelete.addAttribute(new Attribute("item", "id"));
        foreachElementLogicDelete.addAttribute(new Attribute("open", "("));
        foreachElementLogicDelete.addAttribute(new Attribute("close", ")"));
        foreachElementLogicDelete.addAttribute(new Attribute("separator", ","));
        foreachElementLogicDelete.addElement(new TextElement("#{id}"));
        deleteLogic.addElement(foreachElementLogicDelete);
        
        XmlElement parentElement = document.getRootElement();  
        parentElement.addElement(whereSql);  
        parentElement.addElement(queryTotal);
        parentElement.addElement(queryPage);
        parentElement.addElement(deleteBatch);
        parentElement.addElement(deleteLogic);
        return super.sqlMapDocumentGenerated(document, introspectedTable);  
    }  
	
	/**
	 * 生成dao
	 */
	@Override
	public boolean clientGenerated(Interface interfaze,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("BaseMapper<"
				+ introspectedTable.getBaseRecordType() +">");
		FullyQualifiedJavaType imp = new FullyQualifiedJavaType(
				"com.myproject.mapper.BaseMapper");
		/**
		 * 添加 extends BaseMapper
		 */
		interfaze.addSuperInterface(fqjt);

		/**
		 * 添加import com.myproject.mapper.BaseMapper;
		 */
		interfaze.addImportedType(imp);
		/**
		 * 方法不需要
		 */
		interfaze.getMethods().clear();
		interfaze.getAnnotations().clear();
		return true;
	}
}
