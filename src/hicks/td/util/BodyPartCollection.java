package hicks.td.util;

public class BodyPartCollection
{
    private BodyPart m_body;
    private BodyPart m_back;
    private BodyPart m_belt;
    private BodyPart m_feet;
    private BodyPart m_hands;
    private BodyPart m_head;
    private BodyPart m_legs;
    private BodyPart m_torsoBottomLayer;
    private BodyPart m_torsoTopLayer;
    private BodyPart m_torsoShoulders;
    private BodyPart m_torsoBracers;

    public BodyPartCollection(BodyPart body, BodyPart back, BodyPart belt, BodyPart feet, BodyPart hands,
                              BodyPart head, BodyPart legs, BodyPart torsoBottomLayer, BodyPart torsoTopLayer,
                              BodyPart torsoShoulders, BodyPart torsoBracers)
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

    public BodyPart getBody()
    {
        return m_body;
    }

    public void setBody(BodyPart body)
    {
        m_body = body;
    }

    public BodyPart getBack()
    {
        return m_back;
    }

    public void setBack(BodyPart back)
    {
        m_back = back;
    }

    public BodyPart getBelt()
    {
        return m_belt;
    }

    public void setBelt(BodyPart belt)
    {
        m_belt = belt;
    }

    public BodyPart getFeet()
    {
        return m_feet;
    }

    public void setFeet(BodyPart feet)
    {
        m_feet = feet;
    }

    public BodyPart getHands()
    {
        return m_hands;
    }

    public void setHands(BodyPart hands)
    {
        m_hands = hands;
    }

    public BodyPart getHead()
    {
        return m_head;
    }

    public void setHead(BodyPart head)
    {
        m_head = head;
    }

    public BodyPart getLegs()
    {
        return m_legs;
    }

    public void setLegs(BodyPart legs)
    {
        m_legs = legs;
    }

    public BodyPart getTorsoBottomLayer()
    {
        return m_torsoBottomLayer;
    }

    public void setTorsoBottomLayer(BodyPart torsoBottomLayer)
    {
        m_torsoBottomLayer = torsoBottomLayer;
    }

    public BodyPart getTorsoTopLayer()
    {
        return m_torsoTopLayer;
    }

    public void setTorsoTopLayer(BodyPart torsoTopLayer)
    {
        m_torsoTopLayer = torsoTopLayer;
    }

    public BodyPart getTorsoShoulders()
    {
        return m_torsoShoulders;
    }

    public void setTorsoShoulders(BodyPart torsoShoulders)
    {
        m_torsoShoulders = torsoShoulders;
    }

    public BodyPart getTorsoBracers()
    {
        return m_torsoBracers;
    }

    public void setTorsoBracers(BodyPart torsoBracers)
    {
        m_torsoBracers = torsoBracers;
    }
}
