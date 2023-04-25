package com.stech;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.stech.dto.SearchResultDto;
import com.stech.model.Employee;
import com.stech.repository.EmployeeRepository;
import com.stech.resource.ManualSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@RestController
public class SpringBootElasticsearchExampleApplication {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private ManualSearch manualSearch;

	@PostMapping("/saveEmployee")
	public int saveEmployee(@RequestBody List<Employee> employees) {
		System.out.println(employees.toString());
		repository.saveAll(employees);
		return employees.size();
	}

	@GetMapping("/findEverything")
	public Iterable<Employee> findAllEmployees() {
		return repository.findAll();
	}

	@GetMapping("/findEverything1")
	public List<SearchResultDto> findAllEmployees1() throws IOException {
		return manualSearch.getAll("Epp");
	}

	@GetMapping("/findByFName/{firstName}")
	public List<Employee> findByFirstName(@PathVariable String firstName) {
		return repository.findByFirstname(firstName);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootElasticsearchExampleApplication.class, args);
	}
}

