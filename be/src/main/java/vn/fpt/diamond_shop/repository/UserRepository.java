package vn.fpt.diamond_shop.repository;

<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> origin/Nhat
=======
>>>>>>> origin/Khang
import vn.fpt.diamond_shop.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

<<<<<<< HEAD
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/Nhat
=======
}
>>>>>>> origin/Khang
