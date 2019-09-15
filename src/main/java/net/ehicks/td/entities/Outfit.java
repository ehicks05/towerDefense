package net.ehicks.td.entities;

import java.util.ArrayList;
import java.util.List;

public class Outfit
{
    private List<BodyPart> m_bodyParts = new ArrayList<>();

    public Outfit(BodyPart... bodyParts)
    {
        for (BodyPart bodyPart : bodyParts)
            m_bodyParts.add(bodyPart);
    }

    public List<BodyPart> getBodyParts()
    {
        return m_bodyParts;
    }

    public void setBodyParts(List<BodyPart> bodyParts)
    {
        m_bodyParts = bodyParts;
    }
}
