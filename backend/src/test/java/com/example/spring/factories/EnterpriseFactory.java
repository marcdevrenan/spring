package com.example.spring.factories;

import com.example.spring.dto.EnterpriseDTO;
import com.example.spring.models.Department;
import com.example.spring.models.Enterprise;

public class EnterpriseFactory {

    public static Enterprise createEnterprise() {
        Enterprise enterprise = new Enterprise(1L, "Kojima Productions Co.", "Roppongi, Tokyo", "555-888-5432");
        enterprise.getDepartments().add(new Department(1L, "IT", "Lorem ipsum dolor sit amet.", "555-555-1234"));

        return enterprise;
    }

    public static EnterpriseDTO createEnterpriseDTO() {
        Enterprise enterprise = createEnterprise();

        return new EnterpriseDTO(enterprise, enterprise.getDepartments());
    }
}
