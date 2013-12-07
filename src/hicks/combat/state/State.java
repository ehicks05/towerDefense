package hicks.combat.state;

import hicks.combat.entities.Unit;

public abstract class State
{
    public abstract void enter(Unit unit);
    public abstract void execute(Unit unit);
    public abstract void exit(Unit unit);
}
