package com.myproject.es.esService;

import com.myproject.es.esDomain.EsTest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public interface EsTestService extends ElasticsearchRepository<EsTest,Long>{
}
