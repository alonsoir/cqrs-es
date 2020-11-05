package sopra.prototype.services.mapper;

import sopra.prototype.dto.response.UserDataResponseDto;
import sopra.prototype.vo.UserData;
import org.springframework.stereotype.Component;

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
