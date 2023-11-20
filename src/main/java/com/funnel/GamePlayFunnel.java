/* (C) Games24x7 */
package com.funnel;

import java.util.Map;

public class GamePlayFunnel {
    private Map<String, GamePlayEventDetails> events;

    public Map<String, GamePlayEventDetails> getEvents() {
        return events;
    }

    public void setEvents(Map<String, GamePlayEventDetails> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "GamePlayFunnel{" +
                "events=" + events +
                '}';
    }
}
