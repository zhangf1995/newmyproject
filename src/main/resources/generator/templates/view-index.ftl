<div class="tableGroup">
	<table class="easyui-edatagrid"
				data-options="toolbar:'#${tableClass.variableName}TB',url:'/${tableClass.variableName}/json',updateUrl:'/${tableClass.variableName}/store'">
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<#assign isCommonData=true/>
					<#assign thText=""/>
					<#if tableClass.baseFields??>
					<#list tableClass.baseFields as field>
					<#if field.fieldName!='dataState'>
					<#if field.fieldName=='createTime' || field.fieldName=='updateTime' || field.fieldName=='createUser' || field.fieldName=='updateUser' || field.fieldName=='dataState' || field.fieldName=='systemData'>
						<#assign isCommonData=false/>
						<#assign thText>th:text="${r'#{'}page.domain.common.${field.fieldName}}"</#assign>
					<#else>
						<#assign isCommonData=true/>
						<#assign thText>th:text="${r'#{'}page.domain.${tableClass.variableName}.${field.fieldName}}"</#assign>
					</#if>
					<#if field.ext??>
		            <#if field.ext.optionDataType == 'const'>
		            <th data-options="field:'${field.fieldName}',width:100,sortable:true,consts:'${field.ext.data}'" <#if isCommonData>editor="{type:'combobox',options:{required:<#if field.nullable>false<#else>true</#if>,valueField: 'k',textField: 'v',data: MXF.sys.consts.data.${field.ext.data}.kv}}"</#if> ${thText} formatter="MXF.sys.consts.fmt"><#if field.remarkName??>${field.remarkName}</#if></th>
		            <#elseif field.ext.optionDataType == 'domain'>
		            <th data-options="field:'${field.fieldName}',width:100,sortable:true" editor="{objEditor:true,type:'combobox',options:{required:<#if field.nullable>false<#else>true</#if>,url:'/${field.ext.data}/list',valueField:'id',textField:'name'}}" ${thText} formatter="objfmt"><#if field.remarkName??>${field.remarkName}</#if></th>
		            </#if>
		            <#else>
		            <#if field.jdbcType=='TIMESTAMP'>
		            <th data-options="field:'${field.fieldName}',width:100,sortable:true" <#if isCommonData>editor="{type:'datetimebox',options:{required:<#if field.nullable>false<#else>true</#if>,showSeconds:true}}"</#if> ${thText}><#if field.remarkName??>${field.remarkName}</#if></th>
		            <#elseif field.jdbcDateColumn>
		            <th data-options="field:'${field.fieldName}',width:100,sortable:true" <#if isCommonData>editor="{type:'datebox',options:{required:<#if field.nullable>false<#else>true</#if>}}"</#if> ${thText}><#if field.remarkName??>${field.remarkName}</#if></th>
		            <#elseif field.jdbcType?contains('INT') || field.jdbcType?contains('DECIMAL') || field.jdbcType?contains('NUMERIC') >
		            <th data-options="field:'${field.fieldName}',width:100,sortable:true" editor="{type:'numberbox',options:{required:<#if field.nullable>false<#else>true</#if>,min:0,max:100,precision:0}}" ${thText}><#if field.remarkName??>${field.remarkName}</#if></th>
		            <#else>
		            <th data-options="field:'${field.fieldName}',width:100,sortable:true" <#if isCommonData>editor="{type:'textbox',options:{required:<#if field.nullable>false<#else>true</#if>,validType:['length[0,${field.length}]']}}"</#if> ${thText}><#if field.remarkName??>${field.remarkName}</#if></th>
		            </#if>
		            </#if>
		            </#if>
					</#list>
					</#if>
				</tr>
			</thead>
	</table>
	<div id="${tableClass.variableName}TB">
			<div class="grid-btn-groups">
				<a href="#" data-cmd="add" height="480" class="easyui-linkbutton" plain="true"><i class="fa fa-plus font-darkblue"></i> <b th:text="${r'#{'}page.grid.toolbar.buttons.add}">新增</b></a>
				<a href="#" data-cmd="edit" height="480" mustsel="true" remote="false" data-options="disabled:true" class="easyui-linkbutton" plain="true"><i class="fa fa-edit font-green"></i> <b th:text="${r'#{'}page.grid.toolbar.buttons.edit}">编辑</b></a>
				<a href="#" data-cmd="del" mustsel="true" data-options="disabled:true" class="easyui-linkbutton" plain="true"><i class="fa fa-trash font-red"></i> <b th:text="${r'#{'}page.grid.toolbar.buttons.del}">删除</b></a>
				<a href="#" data-cmd="show" height="480" mustsel="true" data-options="disabled:true" class="easyui-linkbutton" plain="true"><i class="fa fa-info-circle font-teal"></i> <b th:text="${r'#{'}page.grid.toolbar.buttons.show}">查看</b></a>
			</div>
			<div class="searchForm">
				<form>
					<txt th:text="${r'#{'}page.domain.common.keyword}">关键字</txt>: 
					<input class="easyui-textbox theme-textbox-radius" name="q" style="width:100px;"/>
					<a href="javascript:;" data-cmd="search" class="easyui-linkbutton button-blue"><i class="fa fa-search"></i> <txt th:text="${r'#{'}page.grid.toolbar.buttons.search}">搜索</txt></a>
					<a href="javascript:;" data-cmd="resetSearch" class="easyui-linkbutton"><txt th:text="${r'#{'}page.grid.toolbar.buttons.reset}">重置</txt></a>
				</form>
			</div>
	</div>
</div>
<script src="/public/js/model/${tableClass.variableName}.js" type="text/javascript"></script>