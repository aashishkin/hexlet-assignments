package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// BEGIN
@Getter
@Setter
public class GuestCreateDTO {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\+([0-9]{11,13})")
    private String phoneNumber;

    @Pattern(regexp = "^\\d([0-9]{2})\\d$")
    private String clubCard;

    @Future
    private LocalDate cardValidUntil;
}
// END
