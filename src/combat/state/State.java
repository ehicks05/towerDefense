package combat.state;

import combat.Unit;

public abstract class State
{
    public abstract void enter(Unit unit);
    public abstract void execute(Unit unit);
    public abstract void exit(Unit unit);
}
