package com.owncloud.android.datamodel;

import com.google.gson.GsonBuilder;

public class SyncDelay {
    private boolean enabled;
    private TimestampType type;
    private long olderThan;

    public SyncDelay(boolean enabled, TimestampType type, long olderThan) {
        this.enabled = enabled;
        this.type = type;
        this.olderThan = olderThan;
    }

    public boolean satisfies(long timestamp) {
        return toAbsoluteTimestamp(type, olderThan) > timestamp;
    }

    private long toAbsoluteTimestamp(TimestampType dateType, long timestamp) {
        if (TimestampType.RELATIVE == dateType) {
            return System.currentTimeMillis() - timestamp;
        }
        return timestamp;
    }

    public static SyncDelay deserialize(String config) {
        String[] parts = config.split("|");
        return new SyncDelay(
            Boolean.valueOf(parts[0]),
            TimestampType.valueOf(parts[1]),
            Long.valueOf(parts[2])
        );
    }

    public String serialize() {
        return String.format("%s|%s|%d", enabled, type.name(), olderThan);
    }

    private static SyncDelay deserializeJson(String config) {
        return new GsonBuilder().create().fromJson(config, SyncDelay.class);
    }

    private String serializeJson() {
        return new GsonBuilder().create().toJson(this, SyncDelay.class);
    }

    private enum TimestampType {
        RELATIVE, ABSOLUTE
    }

}
