package be.pxl.services.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponse {

    private String name;

    private String address;

    // private List<Employee> employees;
    // private List<Department> departments;
}
