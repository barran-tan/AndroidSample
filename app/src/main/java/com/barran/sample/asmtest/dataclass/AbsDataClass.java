package com.barran.sample.asmtest.dataclass;

import android.util.Log;

import java.util.Formatter;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;

public abstract class AbsDataClass {

    private static final String TAG = "AbsData";

    public abstract Object[] getObjects();

    @Override
    public final boolean equals(Object other) {
        Log.v(TAG, "equals called");
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(this.getClass().isAssignableFrom(other.getClass()))) {
            return false;
        }

        Object[] origins = (this).getObjects();
        Object[] others = ((AbsDataClass) other).getObjects();
        for (int i = 0; i < origins.length; i++) {
            if (!Intrinsics.areEqual(origins[i], others[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        Log.v(TAG, "hashCode called");
        return Objects.hash(getObjects());
    }

    @Override
    public final String toString() {
        Log.v(TAG, "toString called");
        Object[] objects = getObjects();
        int length = objects.length;
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                sb.append(":%s");
            } else {
                sb.append(",%s");
            }
        }
        return new Formatter().format(sb.toString(), objects).toString();
    }
}
