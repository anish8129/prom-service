/* (C) Games24x7 */
package com.funnel;

import java.util.List;

public abstract class EventDetails {
    List<EventAttribute> attributes;

    @Override
    public String toString() {
        return "EventDetails{" +
                "attributes=" + attributes +
                '}';
    }
}
