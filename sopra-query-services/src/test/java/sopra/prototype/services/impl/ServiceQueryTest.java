package sopra.prototype.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sopra.prototype.repositories.UserDataRepository;
import sopra.prototype.services.bd.ServiceQuery;
import sopra.prototype.services.impl.utils.SopraUtils;
import sopra.prototype.user.services.UserDataService;
import sopra.prototype.vo.UserData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes={sopra.prototype.services.config.QueryConfig.class})
@Slf4j
public class ServiceQueryTest {

    @Autowired
    private ServiceQuery serviceQuery;

    @Test
    public void whenSavingUserUsingServiceQuery_ICanSaveAndSearch(){
        // GIVEN
        UserData user = new UserData();
        user.setName("Jose");
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        // WHEN
        boolean saved = serviceQuery.saveOrUpdateIntoDB(user);
        // THEN
        assertNotNull(saved,"saved should not be null");

        Iterable<UserData> listUsers = this.serviceQuery.listAll();
        listUsers.forEach(userSaved -> assertNotNull(userSaved));
    }

    @Test
    public void whenUsingServiceQueryICanSaveAndSearch(){
        // GIVEN
        UserData user = new UserData();
        user.setName("Alonso");
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        // WHEN
        boolean saved = this.serviceQuery.saveOrUpdateIntoDB(user);
        assertNotNull(saved,"saved should not be null");
        // THEN
        Iterable<UserData> listUsers = this.serviceQuery.listAll();
        listUsers.forEach(userSaved -> assertNotNull(userSaved));
    }

    @Test
    public void whenUsingServiceQueryIcanSaveSearchAndDelete(){
        // GIVEN
        UserData user = new UserData();
        String name = "Marcos";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());
        // WHEN
        boolean saved = this.serviceQuery.saveOrUpdateIntoDB(user);
        // THEN
        assertNotNull(saved,"saved should not be null");
        Iterable<UserData> listUsers = this.serviceQuery.listAll();
        listUsers.forEach(userSaved -> assertNotNull(userSaved));

        List<UserData> someOptionalUser = this.serviceQuery.findByName(name);
        UserData recovered = someOptionalUser.parallelStream().findFirst().get();
        assertNotNull(recovered,"recovered should not be null");

        this.serviceQuery.delete(recovered);

        List<UserData> someOptionalUserShouldBeDeleted = this.serviceQuery.findByName(name);
// LO ODIO!!!!
        Assertions.assertThrows(RuntimeException.class,
                () -> { someOptionalUserShouldBeDeleted.parallelStream().findFirst().orElseThrow(()->new RuntimeException());});
    }

    @Test
    public void whenUsingIcanSaveSearchAndDelete(){
        // GIVEN
        UserData user = new UserData();
        String name = "Papa";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());

        // WHEN
        boolean saved = serviceQuery.saveOrUpdateIntoDB(user);
        assertNotNull(saved,"recovered should not be null");
        assertTrue(saved,"Should be true");

        List<UserData> listUsers = serviceQuery.findByName(name);

        // THEN
        UserData recovered = listUsers.parallelStream().findFirst().get();
        assertNotNull(recovered,"recovered should not be null");
        assertTrue(recovered.getName().equals(name),"Should be true");
    }
}
