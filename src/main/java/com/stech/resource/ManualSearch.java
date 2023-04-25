package com.stech.resource;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.stech.dto.SearchResultDto;
import com.stech.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ManualSearch {

    Logger logger = LoggerFactory.getLogger(ManualSearch.class);

    @Autowired
    private ElasticsearchClient esClient;

    public List<SearchResultDto> getAll(String searchText) throws IOException {


        SearchResponse<Employee> response = esClient.search(s -> s
                        .index("employeeidx")
                        .query(q -> q
                                .match(t -> t
                                        .field("lastname")
                                        .fuzziness("2")
                                        .query(searchText)
                                )
                        ),
                Employee.class
        );

//        TotalHits total = response.hits().total();
//        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
//
//        if (isExactResult) {
//            logger.info("There are " + total.value() + " results");
//        } else {
//            logger.info("There are more than " + total.value() + " results");
//        }

        List<SearchResultDto> searchResultDtoList = new ArrayList<>();
        List<Hit<Employee>> hits = response.hits().hits();

        for (Hit<Employee> hit: hits) {
            Employee employee = hit.source();

            SearchResultDto searchResultDto = new SearchResultDto();
            searchResultDto.setEmployee(employee);
            searchResultDto.setScore(hit.score());
            searchResultDtoList.add(searchResultDto);

//            logger.info("Found employee \"" + employee.getFirstname() + " " + employee.getLastname() + "\", score " + hit.score());
        }
//        System.out.println(hits);
        return searchResultDtoList;
    }

}
