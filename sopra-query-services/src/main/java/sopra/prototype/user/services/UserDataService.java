package sopra.prototype.user.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import sopra.prototype.vo.UserData;

public interface UserDataService {

	List<UserData> listAll();

	UserData getById(Integer id);

	UserData saveOrUpdate(UserData product);

	void delete(Integer id);

	List<UserData> findByName(@Param("name")String name);

}
