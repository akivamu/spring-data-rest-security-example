# spring-data-rest-security-example
## Use case
There are 3 models:
- Wc
- MeetingRoom
- Laptop

And roles:
- Employee
- Manager

This example will solve these constrains, according to CRUD:
- Anyone can `Read` and `Update` Wc, but no one can't `Create` or `Delete`.
- About meeting rooms (restricted only to managers and employees):
 - Employees and managers can `Read` and `Update` any meeting rooms.
 - Only managers can `Create` and `Delete` meeting rooms.
- About laptops (restricted only to managers and employees):
 - Managers can `Create` laptops, but can't `Delete` laptops.
 - Managers can `Read` and `Update` any laptops.
 - Employees can't `Create` laptops.
 - Only employee that owns the laptop can `Read`, `Update` and `Delete` it.

## Implementation

### Rules for Wc model
```
// Don't allow anyone to create/delete WCs
.antMatchers(HttpMethod.POST, "/wcs").denyAll()
.antMatchers(HttpMethod.DELETE, "/wcs").denyAll()
```

### Rules for MeetingRoom model

CrudRepository: restrict to employee only
```
@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
public interface MeetingRoomRepository extends CrudRepository<MeetingRoom, Long> {
}
```

spring-security: restrict to managers to Create and Delete
```
// Only MANAGERs can create/delete meeting rooms
.antMatchers(HttpMethod.POST, "/meetingRooms").hasRole("MANAGER")
.antMatchers(HttpMethod.DELETE, "/meetingRooms").hasRole("MANAGER")
```

### Rules for Laptop model

spring-security
```
// Only MANAGERs can create laptops
.antMatchers(HttpMethod.POST, "/laptops").hasRole("MANAGER")

// Only EMPLOYEEs can delete `his own` laptops
// Here only check ROLE, specific (`his own` term) will be checked in repo declaration
.antMatchers(HttpMethod.DELETE, "/laptops").hasRole("EMPLOYEE")
```

CrudRepository
```
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
```
