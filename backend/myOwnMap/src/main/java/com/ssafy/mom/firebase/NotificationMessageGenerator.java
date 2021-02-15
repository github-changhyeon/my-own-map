package com.ssafy.mom.firebase;

import com.ssafy.mom.model.UserDto;

@FunctionalInterface
public interface NotificationMessageGenerator {

    String generateNotificationMessage(UserDto sender, UserDto receiver);

}
