package hicks.td.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import hicks.td.components.Position;
import hicks.td.components.Velocity;

public class MovementSystem extends EntityProcessingSystem
{
    @Mapper
    ComponentMapper<Position> pm;
    @Mapper
    ComponentMapper<Velocity> vm;

    public MovementSystem()
    {
        super(Aspect.getAspectForAll(Position.class, Velocity.class));
    }

    protected void process(Entity e)
    {
        // Get the components from the entity using component mappers.
        Position position = pm.get(e);
        Velocity velocity = vm.get(e);

        // Update the position.
        position.addX(velocity.getX() * world.getDelta());
        position.addY(velocity.getY() * world.getDelta());
    }
}
