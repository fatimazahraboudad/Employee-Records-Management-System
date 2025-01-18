package ui.service;

import com.example.EmployeeRecordsManagementSystem.dtos.EmployeeDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
public class EmployeeService {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public EmployeeService(String token) {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Ajouter l'en-tête Authorization
        headers.setContentType(MediaType.APPLICATION_JSON); // Ajouter un type de contenu JSON si nécessaire
    }

    public List<EmployeeDto> getAllEmployees() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        ResponseEntity<List<EmployeeDto>> response = restTemplate.exchange(
                "http://localhost:8081/employee/all", HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<EmployeeDto>>() {}
        );
        return response.getBody();
    }

    public void addEmployee(EmployeeDto employeeDto) {
        // Préparer l'entité HTTP avec le corps et les en-têtes
        HttpEntity<EmployeeDto> entity = new HttpEntity<>(employeeDto, headers);

        // Appeler l'API backend pour ajouter un employé
        restTemplate.postForObject("http://localhost:8081/employee/add", entity, EmployeeDto.class);
    }

    public void updateEmployee(EmployeeDto employeeDto) {
        // Préparer l'entité HTTP avec le corps et les en-têtes
        HttpEntity<EmployeeDto> entity = new HttpEntity<>(employeeDto, headers);

        // Appeler l'API backend pour mettre à jour un employé
        restTemplate.exchange(
                "http://localhost:8081/employee/update", HttpMethod.PUT, entity, Void.class);
    }

    public void deleteEmployee(String employeeId) {
        // Préparer l'entité HTTP avec les en-têtes
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Appeler l'API backend pour supprimer un employé
        restTemplate.exchange(
                "http://localhost:8081/employee/delete/" + employeeId, HttpMethod.DELETE, entity, Void.class);
    }
}
