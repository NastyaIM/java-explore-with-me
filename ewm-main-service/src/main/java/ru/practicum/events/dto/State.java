package ru.practicum.events.dto;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static State from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return state;
            }
        }
        return null;
    }
}
