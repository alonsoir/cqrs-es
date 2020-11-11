package sopra.prototype.utils.mapper;

import sopra.prototype.dto.response.UserDataResponseDto;
import sopra.prototype.vo.UserData;
import org.springframework.stereotype.Component;
// TODO llevar ésto a un proyecto sopra-utils
@Component
public class UserDataMapper {

    public UserDataResponseDto userDataToUserDataResponseDto(UserData userData) {
        return UserDataResponseDto
                .builder()
                .dateRegister(userData.getDateRegister())
                .idUserData(userData.getIdUserData())
                .name(userData.getName())
                .build();
    }
}
