package es.sopra.prototype.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDataResponseDto {

    private Long idUserData;
    private String name;
    private String dateRegister;
}
