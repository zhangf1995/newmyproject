<div class="showDetail">
<table class="table table-celled table-structured table-definition">
  <thead>
  	<th width="120"></th><th></th>
  </thead>
  <tbody>
    <tr>
      <td>ID</td>
      <td th:text="${r'${'}o.id}"></td>
    </tr>
    <#assign thText=""/>
    <#if tableClass.baseFields??>
	<#list tableClass.baseFields as field>
	<#if field.fieldName=='createTime' || field.fieldName=='updateTime' || field.fieldName=='createUser' || field.fieldName=='updateUser' || field.fieldName=='dataState' || field.fieldName=='systemData'>
		<#assign thText>th:text="${r'#{'}page.domain.common.${field.fieldName}}"</#assign>
	<#else>
		<#assign thText>th:text="${r'#{'}page.domain.${tableClass.variableName}.${field.fieldName}}"</#assign>
	</#if>
    <tr>
      <td ${thText}><#if field.remarkName??>${field.remarkName}</#if></td>
      <#if field.jdbcType=='TIMESTAMP'>
      <td th:text="${r'${'}o.${field.fieldName}}? ${r'${'}#dates.format(o.${field.fieldName},'yyyy-MM-dd HH:mm:ss')}"></td>
      <#elseif field.jdbcDateColumn>
      <td th:text="${r'${'}o.${field.fieldName}}? ${r'${'}#dates.format(o.${field.fieldName},'yyyy-MM-dd')}"></td>
      <#elseif field.ext?? && field.ext.optionDataType == 'const'>
      <td field="${field.fieldName}" th:text="${r'${'}o.${field.fieldName}}" fmt-consts="${field.ext.data}"></td>
      <#else>
      <td field="${field.fieldName}" th:text="${r'${'}o.${field.fieldName}}"></td>
      </#if>
    </tr>
    </#list>
	</#if>
  </tbody>
</table>
</div>