package com.myproject.service.impl;

import org.springframework.stereotype.Service;

import com.myproject.domain.${tableClass.shortClassName};
import com.myproject.service.I${tableClass.shortClassName}Service;

@Service
public class ${tableClass.shortClassName}ServiceImpl extends BaseServiceImpl<${tableClass.shortClassName}> implements I${tableClass.shortClassName}Service{
}
