package com.example.surplus_management_system;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestStatusRequest {

    @NotNull
    private RequestStatus status;
}