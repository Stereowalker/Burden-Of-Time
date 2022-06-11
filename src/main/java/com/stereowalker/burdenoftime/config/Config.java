package com.stereowalker.burdenoftime.config;

import com.stereowalker.unionlib.config.UnionConfig;

@UnionConfig(name = "burdenoftime")
public class Config
{
    @UnionConfig.Entry(group = "Social Trails", name = "Trail Softening Modifier")
    public static float trailSofteningModifier = 1f;

    @UnionConfig.Entry(group = "Social Trails", name = "Ground Erosion Chance")
    @UnionConfig.Range(min = 0, max = 1000)
    public static int chanceForGroundToErode = 700;
    
    @UnionConfig.Entry(group = "Social Trails", name = "Sneaking Prevents Trail Formation")
    public static boolean sneakPreventsTrail = true;
    
    @UnionConfig.Entry(group = "Ageing", name = "Ageing Chance")
    @UnionConfig.Range(min = 0, max = 1000)
    public static int chanceForBlockToAge = 800;
    
    @UnionConfig.Entry(group = "Fluid Erosion", name = "Fluid Erosion Chance")
    @UnionConfig.Range(min = 0, max = 1000)
    public static int chanceForBlockToErode = 800;
    
    @UnionConfig.Entry(group = "Logging", name = "Send All Changes To Chat")
    public static boolean sendToChat = false;
}
