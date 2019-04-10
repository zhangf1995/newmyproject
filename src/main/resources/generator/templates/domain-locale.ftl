<#list tableClassSet as tableClass>
<#if tableClass.baseFields??>
#${tableClass.variableName}
<#list tableClass.baseFields as field>
<#if field.fieldName=='createTime' || field.fieldName=='updateTime' || field.fieldName=='createUser' || field.fieldName=='updateUser' || field.fieldName=='dataState' || field.fieldName=='systemData'>
<#else>
page.domain.${tableClass.variableName}.${field.fieldName}=<#if field.remarkName??>${field.remarkName}</#if>
</#if>
</#list>
</#if>

</#list>