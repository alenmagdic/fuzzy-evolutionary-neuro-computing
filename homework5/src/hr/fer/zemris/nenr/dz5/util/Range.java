package hr.fer.zemris.nenr.dz5.util;

public class Range {
    private int rangeStart;
    private int rangeEnd;
    private int rangeStep;

    public Range(int rangeStart, int rangeEnd, int rangeStep) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.rangeStep = rangeStep;
    }

    public Range(int rangeStart, int rangeEnd) {
        this(rangeStart,rangeEnd,1);
    }

    public Range(int singleValue) {
        this(singleValue,singleValue+1,1);
    }

    public int getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(int rangeStart) {
        this.rangeStart = rangeStart;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(int rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getRangeStep() {
        return rangeStep;
    }

    public void setRangeStep(int rangeStep) {
        this.rangeStep = rangeStep;
    }
}
