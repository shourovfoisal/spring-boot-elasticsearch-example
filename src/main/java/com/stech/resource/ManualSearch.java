package com.stech.resource;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.stech.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ManualSearch {

    Logger logger = LoggerFactory.getLogger(ManualSearch.class);

    @Autowired
    private ElasticsearchClient esClient;

    public void getAll(String searchText) throws IOException {


        SearchResponse<Employee> response = esClient.search(s -> s
                        .index("employeeidx")
                        .query(q -> q
                                .match(t -> t
                                        .field("lastname")
                                        .query(searchText)
                                )
                        ),
                Employee.class
        );

        System.out.println(response);

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            logger.info("There are " + total.value() + " results");
        } else {
            logger.info("There are more than " + total.value() + " results");
        }

        List<Hit<Employee>> hits = response.hits().hits();
        for (Hit<Employee> hit: hits) {
            Employee employee = hit.source();
            logger.info("Found employee \"" + employee.getFirstname() + " " + employee.getLastname() + "\", score " + hit.score());
        }
    }

}
