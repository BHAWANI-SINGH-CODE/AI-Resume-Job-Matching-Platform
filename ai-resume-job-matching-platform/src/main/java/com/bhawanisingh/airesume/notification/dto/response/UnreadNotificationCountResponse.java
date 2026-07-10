package com.bhawanisingh.airesume.notification.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnreadNotificationCountResponse {

    private long unreadCount;
}