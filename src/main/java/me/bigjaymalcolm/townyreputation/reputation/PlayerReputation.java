package me.bigjaymalcolm.townyreputation.reputation;

import java.util.HashMap;
import java.util.Map;

public class PlayerReputation
{
    public Map<String, Integer> Reputations;

    public PlayerReputation()
    {
        Reputations = new HashMap<String, Integer>();
    }
}