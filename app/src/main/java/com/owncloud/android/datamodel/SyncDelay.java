package com.owncloud.android.datamodel;

import com.google.gson.GsonBuilder;

import java.util.Date;

public class SyncDelay {
    private boolean enabled;
    private RangeType type;
    private long olderThan;

    public SyncDelay(boolean enabled, RangeType type, long olderThan) {
        this.enabled = enabled;
        this.type = type;
        this.olderThan = olderThan;
    }

    public boolean satisfies(long timestamp) {
        return toRangeEdge(type, olderThan).getTime() > timestamp;
    }

    private Date toRangeEdge(RangeType dateType, long timestamp) {
        if(RangeType.RELATIVE == dateType) {
            return new Date(System.currentTimeMillis() - timestamp);
        }
        return new Date(timestamp);
    }

    public static SyncDelay deserialize(String config) {
        String[] parts = config.split("|");
        return new SyncDelay(
            Boolean.valueOf(parts[0]),
            RangeType.valueOf(parts[1]),
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

    private enum RangeType {
        RELATIVE, ABSOLUTE
    }

}
