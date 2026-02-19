package tiameds.emsBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tiameds.emsBackend.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
