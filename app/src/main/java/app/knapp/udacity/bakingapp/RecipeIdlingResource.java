package app.knapp.udacity.bakingapp;

import android.support.annotation.Nullable;


import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeIdlingResource implements android.support.test.espresso.IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;

    private AtomicBoolean idleFlag = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return idleFlag.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdleState(boolean idleState) {
        idleFlag.set(idleState);
        if (idleState && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}