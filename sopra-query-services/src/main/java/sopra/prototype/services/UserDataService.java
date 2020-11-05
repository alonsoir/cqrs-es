package sopra.prototype.services;

import java.util.List;

import sopra.prototype.vo.UserData;

public interface UserDataService {

	List<UserData> listAll();

	UserData getById(Integer id);

	UserData saveOrUpdate(UserData product);

	void delete(Integer id);

}
