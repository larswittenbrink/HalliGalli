package lars.wittenbrink.halligalli.game;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;

public class MyDeque<E> extends ArrayDeque {
    @Override
    public void addFirst(Object o) {
        func();
        super.addFirst(o);

    }

    @Override
    public void addLast(Object o) {
        func();
        super.addLast(o);
    }

    @Nullable
    @Override
    public Object pollFirst() {
        func();
        return super.pollFirst();
    }

    @Nullable
    @Override
    public Object pollLast() {
        func();
        return super.pollLast();
    }

    public void func(){
        // TODO: 09.03.2020 Refresh UI
    }
}