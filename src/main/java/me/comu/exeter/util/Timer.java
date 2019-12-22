package me.comu.exeter.util;

public class Timer
{
    private long previousMS;

    public Timer()
    {
        reset();
    }

    public boolean hasCompleted(long milliseconds)
    {
        return getCurrentMS() - getPreviousMS() >= milliseconds;
    }

    public void reset()
    {
        previousMS = getCurrentMS();
    }

    public long getPreviousMS()
    {
        return previousMS;
    }

    public long getCurrentMS()
    {
        return System.nanoTime() / 1000000;
    }
}

