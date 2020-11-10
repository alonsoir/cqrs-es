package sopra.prototype.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sopra.prototype.vo.UserData;

import java.util.List;
import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Integer> {
    @Query("FROM UserData WHERE name=:name")
    List<UserData> findByName(@Param("name")String name);
}
