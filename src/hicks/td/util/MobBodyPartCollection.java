package hicks.td.util;

public class MobBodyPartCollection
{
    private MobBodyPart m_body;
    private MobBodyPart m_back;
    private MobBodyPart m_belt;
    private MobBodyPart m_feet;
    private MobBodyPart m_hands;
    private MobBodyPart m_head;
    private MobBodyPart m_legs;
    private MobBodyPart m_torsoBottomLayer;
    private MobBodyPart m_torsoTopLayer;
    private MobBodyPart m_torsoShoulders;
    private MobBodyPart m_torsoBracers;

    public MobBodyPartCollection(MobBodyPart body, MobBodyPart back, MobBodyPart belt, MobBodyPart feet, MobBodyPart hands,
                                 MobBodyPart head, MobBodyPart legs, MobBodyPart torsoBottomLayer, MobBodyPart torsoTopLayer,
                                 MobBodyPart torsoShoulders, MobBodyPart torsoBracers)
    {
        m_body = body;
        m_back = back;
        m_belt = belt;
        m_feet = feet;
        m_hands = hands;
        m_head = head;
        m_legs = legs;
        m_torsoBottomLayer = torsoBottomLayer;
        m_torsoTopLayer = torsoTopLayer;
        m_torsoShoulders = torsoShoulders;
        m_torsoBracers = torsoBracers;
    }

    public MobBodyPart getBody()
    {
        return m_body;
    }

    public void setBody(MobBodyPart body)
    {
        m_body = body;
    }

    public MobBodyPart getBack()
    {
        return m_back;
    }

    public void setBack(MobBodyPart back)
    {
        m_back = back;
    }

    public MobBodyPart getBelt()
    {
        return m_belt;
    }

    public void setBelt(MobBodyPart belt)
    {
        m_belt = belt;
    }

    public MobBodyPart getFeet()
    {
        return m_feet;
    }

    public void setFeet(MobBodyPart feet)
    {
        m_feet = feet;
    }

    public MobBodyPart getHands()
    {
        return m_hands;
    }

    public void setHands(MobBodyPart hands)
    {
        m_hands = hands;
    }

    public MobBodyPart getHead()
    {
        return m_head;
    }

    public void setHead(MobBodyPart head)
    {
        m_head = head;
    }

    public MobBodyPart getLegs()
    {
        return m_legs;
    }

    public void setLegs(MobBodyPart legs)
    {
        m_legs = legs;
    }

    public MobBodyPart getTorsoBottomLayer()
    {
        return m_torsoBottomLayer;
    }

    public void setTorsoBottomLayer(MobBodyPart torsoBottomLayer)
    {
        m_torsoBottomLayer = torsoBottomLayer;
    }

    public MobBodyPart getTorsoTopLayer()
    {
        return m_torsoTopLayer;
    }

    public void setTorsoTopLayer(MobBodyPart torsoTopLayer)
    {
        m_torsoTopLayer = torsoTopLayer;
    }

    public MobBodyPart getTorsoShoulders()
    {
        return m_torsoShoulders;
    }

    public void setTorsoShoulders(MobBodyPart torsoShoulders)
    {
        m_torsoShoulders = torsoShoulders;
    }

    public MobBodyPart getTorsoBracers()
    {
        return m_torsoBracers;
    }

    public void setTorsoBracers(MobBodyPart torsoBracers)
    {
        m_torsoBracers = torsoBracers;
    }
}
