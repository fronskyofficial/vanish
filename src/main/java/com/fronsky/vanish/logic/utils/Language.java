package com.fronsky.vanish.logic.utils;

import com.fronsky.vanish.module.VanishModule;

public enum Language {
    DEFAULT("Please choose a language message. The current message is a default message."), // id = 0
    NO_PERMISSION("&cYou do not have permissions to perform this action. Please contact your system administrator for assistance."), // id = 1
    PLAYER_JOINED_VANISHED("&e<player> has joined vanished and silently."),
    PLAYER_QUIT_VANISHED("&e<player> has quit vanished."),
    PLAYER_VANISHED("&e<player> has vanished. Poof!"),
    PLAYER_VISIBLE("&e<player> has become visible."),
    SELF_PLAYER_VANISHED("&aYou have successfully vanished &2<player>."),
    SELF_PLAYER_VISIBLE("&aYou have successfully made &2<player> &avisible."),
    SELF_JOINED_VANISHED("&3You have joined vanished. To appear type: /vanish!"),
    SELF_VANISHED("&3You have vanished. Poof!"),
    SELF_VISIBLE("&3You have become visible."),
    SOUND_ENABLE("&aYou have enabled the vanish sound!"),
    SOUND_DISABLE("&aYou have disabled the vanish sound!"),
    RELOAD("&aThe Vanish plugin has been reloaded!");

    private final String message;

    Language(String message) {
        this.message = message;
    }

    public int getId() {
        return ordinal();
    }

    public String getMessage() {
        String _message = VanishModule.getData().getMessages().get().getString(name().toLowerCase());
        if (_message == null) {
            _message = message;
        }
        return _message;
    }

    public String getMessageWithColor() {
        return ColorUtils.colorize(message);
    }

    public Language getLanguage(String name) {
        Language language = null;
        for (Language obj : Language.values()) {
            if (name.equalsIgnoreCase(obj.name())) {
                language = obj;
                break;
            }
        }
        return language;
    }
}
