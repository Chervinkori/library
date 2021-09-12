package com.chweb.library.service.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chroman <br>
 * 10.09.2021
 */
@Data
@AllArgsConstructor
public class Notification {
    private String[] to;
    private String subject;
    private String text;
}
