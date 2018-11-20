package core;

import java.io.Serializable;

public enum RenderOrder implements Serializable
{
    Default(50), BackGround(100), Main(150), Player(170), ForeGround(200);
    public int order;
    RenderOrder(int order)
    {
        this.order = order;
    }
}
