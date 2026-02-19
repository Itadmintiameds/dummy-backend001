package tiameds.emsBackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tiameds.emsBackend.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
