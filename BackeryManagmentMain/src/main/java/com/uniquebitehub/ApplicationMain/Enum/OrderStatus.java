package com.uniquebitehub.ApplicationMain.Enum;

public enum OrderStatus {
    PLACED,
    CONFIRMED,
    BAKING,
    READY,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

	 public boolean isEditable() {
	        return this == PLACED || this == CONFIRMED;
	    }
}
