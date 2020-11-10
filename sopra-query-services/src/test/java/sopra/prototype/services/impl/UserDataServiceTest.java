package sopra.prototype.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.services.impl.utils.SopraUtils;
import sopra.prototype.user.services.UserDataService;
import sopra.prototype.vo.UserData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes={sopra.prototype.services.config.QueryConfig.class})
public class UserDataServiceTest {

    @Autowired
    private UserDataService userData;

    @Autowired
    private UserDataRepository userDataRepository;

    @Test
    public void whenSavingUserUsingDataServiceICanSaveAndSearch(){
        // GIVEN
        UserData user = new UserData();
        user.setName("Jose");
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        // WHEN
        UserData saved = userData.saveOrUpdate(user);
        // THEN
        assertNotNull(saved,"saved should not be null");
        Iterable<UserData> listUsers = this.userDataRepository.findAll();
        listUsers.forEach(userSaved -> assertNotNull(userSaved));
    }

    @Test
    public void whenUsingRepositoryICanSaveAndSearch(){
        // GIVEN
        UserData user = new UserData();
        user.setName("Alonso");
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        // WHEN
        UserData saved = this.userDataRepository.save(user);
        assertNotNull(saved,"saved should not be null");
        // THEN
        Iterable<UserData> listUsers = this.userDataRepository.findAll();
        listUsers.forEach(userSaved -> assertNotNull(userSaved));
    }

    @Test
    public void whenUsingRepositoryIcanSaveSearchAndDelete(){
        // GIVEN
        UserData user = new UserData();
        String name = "Marcos";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        // WHEN
        UserData saved = userDataRepository.save(user);
        // THEN
        assertNotNull(saved,"saved should not be null");
        Iterable<UserData> listUsers = this.userDataRepository.findAll();
        listUsers.forEach(userSaved -> assertNotNull(userSaved));

        List<UserData> someOptionalUser = this.userDataRepository.findByName(name);
        UserData recovered = someOptionalUser.parallelStream().findFirst().get();
        assertNotNull(recovered,"recovered should not be null");

        this.userDataRepository.delete(recovered);

        List<UserData> someOptionalUserShouldBeDeleted = this.userDataRepository.findByName(name);
// LO ODIO!!!!
        Assertions.assertThrows(RuntimeException.class,
                () -> { someOptionalUserShouldBeDeleted.parallelStream().findFirst().orElseThrow(()->new RuntimeException());});
    }

    @Test
    public void whenUsingServiceIcanSaveSearchAndDelete(){
        // GIVEN
        UserData user = new UserData();
        String name = "Papa";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());

        // WHEN
        UserData saved = userData.saveOrUpdate(user);
        assertNotNull(saved,"recovered should not be null");
        assertTrue(saved.getName().equals(name),"Should be true");

        List<UserData> listUsers = userData.findByName(name);

        // THEN
        UserData recovered = listUsers.parallelStream().findFirst().get();
        assertNotNull(recovered,"recovered should not be null");
        assertTrue(recovered.getName().equals(name),"Should be true");
    }
}
