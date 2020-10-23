package es.sopra.prototype.services;

import java.util.List;

import es.sopra.prototype.vo.UserData;

public interface UserDataService {

	List<UserData> listAll();

	UserData getById(Long id);

	UserData saveOrUpdate(UserData product);

	void delete(Long id);

}
