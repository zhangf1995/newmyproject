<form  method="post" action="/${tableClass.variableName}/store">
		<input type="hidden" name="id" />
		<#if tableClass.baseFields??>
		<#list tableClass.baseFields as field>
		<#if field.fieldName=='createTime' || field.fieldName=='updateTime' || field.fieldName=='createUser' || field.fieldName=='updateUser' || field.fieldName=='dataState' || field.fieldName=='systemData'>
		<#else>
		<div class="input-div"> 
            <label class="label-top" th:text="${r'#{'}page.domain.${tableClass.variableName}.${field.fieldName}}"><#if field.remarkName??>${field.remarkName}</#if></label>
            <#if field.ext??>
            <#if field.ext.optionDataType == 'const'>
            <input class="easyui-combobox theme-textbox-radius" name="${field.fieldName}" data-options="required:<#if field.nullable>false<#else>true</#if>,valueField: 'k',textField: 'v',data: MXF.sys.consts.data.${field.ext.data}.kv" />
            <#elseif field.ext.optionDataType == 'domain'>
            <input class="easyui-combobox theme-textbox-radius" name="${field.fieldName}.id" data-options="required:<#if field.nullable>false<#else>true</#if>,url:'/${field.ext.data}/list',valueField:'id',textField:'name'" />
            </#if>
            <#else>
            <#if field.jdbcType=='TIMESTAMP'>
            <input class="easyui-datetimebox theme-textbox-radius" name="${field.fieldName}" data-options="required:<#if field.nullable>false<#else>true</#if>,showSeconds:true" />
            <#elseif field.jdbcDateColumn>
            <input class="easyui-datebox theme-textbox-radius" name="${field.fieldName}" data-options="required:<#if field.nullable>false<#else>true</#if>" />
            <#elseif field.jdbcType?contains('INT') || field.jdbcType?contains('DECIMAL') || field.jdbcType?contains('NUMERIC') >
            <input class="easyui-numberbox theme-textbox-radius" name="${field.fieldName}" data-options="required:<#if field.nullable>false<#else>true</#if>,min:0,max:100,precision:0" />
            <#else>
            <input class="easyui-textbox theme-textbox-radius" name="${field.fieldName}" data-options="required:<#if field.nullable>false<#else>true</#if>,validType:['length[0,${field.length}]']" />
            </#if>
            </#if>
        </div>
        </#if>
		</#list>
		</#if>
</form>