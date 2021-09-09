package com.chweb.library.service.notification;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chroman <br>
 * 09.09.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
class SenderNotificationTest {
    @Autowired
    private SenderNotification senderMailNotification;

    @Test
    void sendSimpleMessage() {
        senderMailNotification.sendSimpleMessage("Test Subject", "Test body text", "test@library.ru");
    }
}