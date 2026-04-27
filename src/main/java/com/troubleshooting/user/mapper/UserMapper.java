package com.troubleshooting.user.mapper;

import com.troubleshooting.user.dto.GetUserDto;
import com.troubleshooting.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public GetUserDto toDto(User user) {
        return new GetUserDto(
                user.getUserId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }
}
