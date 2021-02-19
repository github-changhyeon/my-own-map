package com.ssafy.mom.firebase;

import com.ssafy.mom.model.UserDto;

public enum NotificationType {
	LIKE_RECEIVED(((sender, receiver)
			-> sender.getUsername() + "님이 " + receiver.getUsername() + "님의 글을 찜하였습니다.")),
	COMMENT_RECEIVED(((sender, receiver)
			-> sender.getUsername() + "님이 " + receiver.getUsername() + "님에게 댓글을 달았습니다.")),
	FOLLOW_RECEIVED(((sender, receiver)
			-> sender.getUsername() + "님이 " + receiver.getUsername() + "님을 팔로우했습니다."));

	private NotificationMessageGenerator notificationMessageGenerator;

	NotificationType(NotificationMessageGenerator notificationMessageGenerator) {
		this.notificationMessageGenerator = notificationMessageGenerator;
	}

	public String generateNotificationMessage(UserDto sender, UserDto receiver) {
		return notificationMessageGenerator.generateNotificationMessage(sender, receiver);
	}

}
