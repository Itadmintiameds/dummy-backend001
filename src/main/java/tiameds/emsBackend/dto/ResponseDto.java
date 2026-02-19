package tiameds.emsBackend.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private Long user_id;
    private String username;
    private String email;
    private boolean verified;
    private String message;
}