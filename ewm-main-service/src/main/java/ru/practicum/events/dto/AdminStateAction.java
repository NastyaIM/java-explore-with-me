package ru.practicum.events.dto;

public enum AdminStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static AdminStateAction from(String stringState) {
        for (AdminStateAction state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return state;
            }
        }
        return null;
    }
}
