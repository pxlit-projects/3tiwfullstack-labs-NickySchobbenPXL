package be.pxl.services.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "organizations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String address;

    @Transient
    private List<Employee> employees = new ArrayList<>();

    @Transient
    private List<Department> departments = new ArrayList<>();
}
