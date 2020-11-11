package sopra.prototype.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sopra.prototype.command.services.ServiceCommand;
import sopra.prototype.config.services.CommandConfig;
import sopra.prototype.services.impl.utils.SopraUtils;
import sopra.prototype.vo.UserData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes={CommandConfig.class})
@Slf4j
public class ServiceCommandTest {

    @Autowired
    private ServiceCommand serviceCommand;

    @Test
    public void testSaveOrUpdateIntoDB(){

        // GIVEN
        UserData user = new UserData();
        String name = "Papa";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());

        // WHEN
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);

        // THEN
        assertTrue(saved,"saved should be true");
        List<UserData> listUsers = serviceCommand.listAll();
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"Should be greater than zero.");

    }

    @Test
    public void testdeleteFromDB(){

        // GIVEN
        UserData user = new UserData();
        String name = "Mama";
        user.setName(name);
        user.setDateRegister(SopraUtils.getActualFormatedDate());

        // WHEN
        boolean saved = serviceCommand.saveOrUpdateIntoDB(user);
        assertTrue(saved,"saved should be true");

        // THEN
        List<UserData>  listUsers = serviceCommand.findByName(name);
        assertNotNull(listUsers,"listUsers should not be null");
        assertTrue(listUsers.size()>0,"listUsers size should be greater than zero");
        assertTrue(listUsers.size()==1,"listUsers size should be only one");
        UserData userRecovered = listUsers.get(0);
        assertNotNull(userRecovered,"userRecovered should not be null");
        boolean isDeleted = serviceCommand.deleteFromDB(userRecovered.getIdUserData());
        assertTrue(isDeleted,"isDeleted should be true");
    }
}
