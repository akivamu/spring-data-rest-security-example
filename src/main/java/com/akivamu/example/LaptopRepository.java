package com.akivamu.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

// Any manager can find,create and edit laptops
// Only employee that owns laptop can find, edit and delete laptop
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface LaptopRepository extends CrudRepository<Laptop, Long> {
    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER') or #laptop?.employeeName == principal?.username")
    Laptop save(@Param("laptop") Laptop laptop);

    @Override
    @PostFilter("hasRole('ROLE_MANAGER') or filterObject.employeeName == principal.username")
    Iterable<Laptop> findAll();

    @Override
    @PostAuthorize("hasRole('ROLE_MANAGER') or returnObject?.employeeName == principal?.username")
    Laptop findOne(@Param("id") Long id);

    @Override
    @PreAuthorize("@laptopRepository.findOne(#id)?.employeeName == principal?.username")
    void delete(Long id);

    @Override
    @PreAuthorize("#laptop?.employeeName == principal?.username")
    void delete(@Param("laptop") Laptop laptop);
}
