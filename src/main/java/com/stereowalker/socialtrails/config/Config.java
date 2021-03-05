package com.stereowalker.socialtrails.config;

import com.stereowalker.socialtrails.blocks.TrailConversions;
import com.stereowalker.unionlib.config.UnionConfig;

@UnionConfig(name = "socialtrails")
public class Config
{
    @UnionConfig.Entry(group = "Social Trails", name = "Trail Softening Modifier")
    public static float trailSofteningModifier = 1f;

    @UnionConfig.Entry(group = "Social Trails", name = "Ground Erosion Chance")
    @UnionConfig.Range(min = 0, max = 1000)
    public static int chanceForGroundToErode = 700;

    @UnionConfig.Entry(group = "Social Trails", name = "Sneaking Prevents Trail Formation")
    public static boolean sneakPreventsTrail = true;

//    @UnionConfig.Entry(group = "", name = "Path Transformations")
    public static TrailConversions[] conversions = new TrailConversions[]{
            new TrailConversions("minecraft:grass_block", "minecraft:dirt", 10f),
            new TrailConversions("minecraft:dirt", "minecraft:coarse_dirt", 10f),
            new TrailConversions("minecraft:coarse_dirt", "minecraft:grass_path", 10f),
    };

    public static class PathConfigEntry
    {
        public String fromID;
        public String toID;
        public float requiredDepth;

        public PathConfigEntry(String from, String to, float depth)
        {
            fromID = from;
            toID = to;
            requiredDepth = depth;
        }
    }
}
