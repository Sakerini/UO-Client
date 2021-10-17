package com.noetic.client.handlers;

import com.noetic.client.UODisplay;
import com.noetic.client.UOEngine;
import com.noetic.client.enums.AuthStatus;
import com.noetic.client.gui.UONotification;
import com.noetic.client.gui.UONotificationConfirmation;
import com.noetic.client.network.connections.AuthConnection;
import com.noetic.client.states.CharacterCreationState;
import com.noetic.client.states.CharacterSelectionState;
import com.noetic.client.states.LoginScreenState;
import com.noetic.client.utils.NetworkUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class NotificationHandler {

    private static UONotification notification;
    private static UONotificationConfirmation notificationConfirmation;


    public static void handle(int stateId, UOEngine engine, UODisplay display, Graphics2D graphics) {
        if (stateId == LoginScreenState.ID) {
            handleLoginScreenNotifications(engine, display, graphics);
        } else if (stateId == CharacterCreationState.ID) {
            handleCreationCharacterNotifications(engine, display, graphics);
        }
    }

    private static void handleCreationCharacterNotifications(UOEngine engine, UODisplay display, Graphics2D graphics) {
        AuthConnection connection = NetworkUtil.getAuthConnection();
        if (Objects.nonNull(connection) && Objects.nonNull(connection.getClient())) {
            AuthStatus status = NetworkUtil.getAuthConnection().getStatus();

            if (status.equals(AuthStatus.CharacterCreateServerError)) {
                showBasicNotification(display, "Server error occured.");
            } else if (status.equals(AuthStatus.CharacterCreateExists)) {
                showBasicNotification(display, "Unable to create character.\nPlease try again.");
            } else if (status.equals(AuthStatus.CharacterCreateOk)) {
                NetworkUtil.getAuthConnection().setStatus(AuthStatus.Waiting);
                display.enterState(CharacterSelectionState.ID);
            }

            if (notificationConfirmation != null) {
                notificationConfirmation.tick(engine, display, 0);
                notificationConfirmation.render(engine, display, graphics);
            }
        }
    }

    private static void handleLoginScreenNotifications(UOEngine engine, UODisplay display, Graphics2D graphics) {
        AuthConnection connection = NetworkUtil.getAuthConnection();
        if (Objects.nonNull(connection) && Objects.nonNull(connection.getClient())) {
            switch (NetworkUtil.getAuthConnection().getStatus()) {
                case CONNECTING:
                    showBasicNotification(display, "Connecting...");
                    break;
                case CONNECTION_FAILED:
                   showConfirmNotification(display, "Failed to connect.");
                   break;
                case AUTHENTICATING:
                   showBasicNotification(display, "Authenticating...");
                   break;
                case UNKNOWN:
                    showConfirmNotification(display,
                            "Unable to verify this account.\nPlease try again or contact a developer.");
                    break;
                case INCORRECT:
                    showConfirmNotification(display,
                            "Invalid username/password combination.\nPlease try again or contact a developer.");
                    break;
                case OK:
                    connection.setStatus(AuthStatus.Waiting);
                    notificationConfirmation = null;
                    notification = null;
                    display.enterState(CharacterSelectionState.ID);
                    break;
                case ALREADY_ONLINE:
                    showConfirmNotification(display, "Account is already online.");
                    break;
            }


            if (Objects.nonNull(notification)) {
                notification.render(engine, display, graphics);
            } else if (Objects.nonNull(notificationConfirmation)) {
                notificationConfirmation.tick(engine, display, 0);
                notificationConfirmation.render(engine, display, graphics);
            }
        }
    }

    private static void showBasicNotification(UODisplay display, String message) {
        if (notification != null)
            notification = null;
        notification = new UONotification(message);
        notification.setLocation(display.getWidth() / 2 - notification.getWidth() / 2,
                display.getHeight() / 2 - notification.getHeight() / 2);
    }

    private static void showConfirmNotification(UODisplay display, String message) {
        if (notificationConfirmation != null)
            notificationConfirmation = null;
        notification = null;
        notificationConfirmation = new UONotificationConfirmation(message);
        notificationConfirmation.setLocation(display.getWidth() / 2 - notificationConfirmation.getWidth() / 2, display.getHeight() / 2 - notificationConfirmation.getHeight() / 2);
        notificationConfirmation.getButton().addActionListener(e -> NetworkUtil.setAuthConnection(null));
    }
}
