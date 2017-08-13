package ru.o2genum.coregame.game;

import android.view.*;
import android.graphics.*;

import ru.o2genum.coregame.framework.*;
import ru.o2genum.coregame.framework.impl.*;

public class GameActivity extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new GameScreen(this); 
    }
}
