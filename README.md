# spring-data-rest-security-example

There are 3 models:
- Wc
- MeetingRoom
- Laptop

And roles:
- Employee
- Manager
- Other authenticated roles

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
