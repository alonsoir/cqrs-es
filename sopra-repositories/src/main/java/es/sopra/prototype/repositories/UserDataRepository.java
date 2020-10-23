package es.sopra.prototype.repositories;

import org.springframework.data.repository.CrudRepository;

import es.sopra.prototype.vo.UserData;

public interface UserDataRepository extends CrudRepository<UserData, Long> {
}
