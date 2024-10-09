package be.pxl.services.client;


import be.pxl.services.domain.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name="department-service")
public interface DepartmentClient {

    @GetMapping("/api/department/organization/{organizationId}")
     List<Department> getDepartmentsByOrganizationId(@PathVariable Long organizationId);
}
