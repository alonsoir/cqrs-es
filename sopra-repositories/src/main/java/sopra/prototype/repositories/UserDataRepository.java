package sopra.prototype.repositories;

import org.springframework.data.repository.CrudRepository;

import sopra.prototype.vo.UserData;

public interface UserDataRepository extends CrudRepository<UserData, Integer> {
}
