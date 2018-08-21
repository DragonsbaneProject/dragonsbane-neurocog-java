package io.dragonsbane.neurocog.tests;

import io.onemfive.data.JSONSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImpairmentTest implements JSONSerializable {

    public enum Impairment {Unknown, Unimpaired, Borderline, Impaired, Gross}

    private String name;
    private Boolean baseline;
    private Date timeStarted;
    private Date timeEnded;
    private Impairment impairment = Impairment.Unknown;

    private int difficulty = 1;

    protected int successes = 0; // Clicked when should have
    protected List<Long> successResponseTimes = new ArrayList<>();
    protected long cumulativeSuccessResponseTimeMs = 0L;
//    protected double successWeight = 0D; // TODO: Weights need to come from population

    protected int misses = 0; // Should have clicked but did not
    protected long cumulativeMissesResponseTimeMs = 0L;
    protected List<Long> missesResponseTimes = new ArrayList<>();
//    protected double missWeight = 0D; // TODO: Weights need to come from population

    protected int negative = 0; // Clickable but should not have clicked
    protected long cumulativeNegativeResponseTimeMs = 0L;
    protected List<Long> negativeResponseTimes = new ArrayList<>();
//    protected double negativeWeight = 0D; // TODO: Weights need to come from population

    protected int inappropriate = 0; // Not clickable but clicked
    protected long cumulativeInappropriateResponseTimeMs = 0L;
    protected List<Long> inappropriateResponseTimes = new ArrayList<>();
//    protected double inappropriateWeight = 0D; // TODO: Weights need to come from population

    private List<Integer> cardsUsed = new ArrayList<>();

    /**
     * Blood Alcohol Content (BAC) Impairment Model
     *
     * BAC is used for training the model due to the ability to easily measure it.
     *
     * Widely expected levels of impairment based solely on BAC:
     * Male Female BAC% Effects
     *   1    1    .020 Light to moderate drinkers begin to feel some effects
     *             .040 Most people begin to feel relaxed
     *  2-3  1-2   .050 Though, judgment and restraint more lax. Steering errors increase. Vision impaired.
     *             .060 Judgment is somewhat impaired, people are less able to make rational decisions about their capabilities (for example, driving)
     *  3-4  2-4   .080 There is a definite impairment of muscle coordination and driving skills; this is legal level for intoxication in some states
     *  3-5  2-5   .100 There is clear deterioration of reaction time and control; this is legally drunk in most states
     *             .120 Vomiting usually occurs. Unless this level is reached slowly or a person has developed a tolerance to alcohol
     *  4-7  3-7   .150 Balance and movement are impaired. This blood-alcohol level means the equivalent of 1/2 pint of whiskey is circulating in the blood stream
     *             .300 Many people lose consciousness
     *             .400 Most people lose consciousness; some die
     *             .450 Breathing stops; this is a fatal dose for most people
     *
     */
    private double bloodAlcoholContent = 0.0D;
    private double score = 0.0D;

    public ImpairmentTest() {}

    public ImpairmentTest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBaseline() {
        return baseline;
    }

    public void setBaseline(Boolean baseline) {
        this.baseline = baseline;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(Date timeEnded) {
        this.timeEnded = timeEnded;
    }

    public List<Integer> cardsUsed() {
        return cardsUsed;
    }

    public void setCardsUsed(List<Integer> cardsUsed) {
        this.cardsUsed = cardsUsed;
    }

    public double getBloodAlcoholContent() {
        return bloodAlcoholContent;
    }

    public void setBloodAlcoholContent(double bloodAlcoholContent) {
        this.bloodAlcoholContent = bloodAlcoholContent;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getSuccesses() {
        return successes;
    }

    public void addSuccess(long responseTimeMs) {
        successes += 1;
        successResponseTimes.add(responseTimeMs);
        cumulativeSuccessResponseTimeMs += responseTimeMs;
    }

//    public double getSuccessWeight() {
//        return successWeight;
//    }
//
//    public void setSuccessWeight(double successWeight) {
//        this.successWeight = successWeight;
//    }

    public int getMisses() {
        return misses;
    }

    public void addMiss(long responseTimeMs) {
        misses += 1;
        missesResponseTimes.add(responseTimeMs);
        cumulativeMissesResponseTimeMs += responseTimeMs;
    }

//    public double getMissWeight() {
//        return missWeight;
//    }
//
//    public void setMissWeight(double missWeight) {
//        this.missWeight = missWeight;
//    }

    public int getNegative() {
        return negative;
    }

    public void addNegative(long responseTimeMs) {
        negative += 1;
        negativeResponseTimes.add(responseTimeMs);
        cumulativeNegativeResponseTimeMs += responseTimeMs;
    }

//    public double getNegativeWeight() {
//        return negativeWeight;
//    }
//
//    public void setNegativeWeight(double negativeWeight) {
//        this.negativeWeight = negativeWeight;
//    }

    public int getInappropriate() {
        return inappropriate;
    }

    public void addInappropriate(long responseTimeMs) {
        inappropriate += 1;
        inappropriateResponseTimes.add(responseTimeMs);
        cumulativeInappropriateResponseTimeMs += responseTimeMs;
    }

//    public double getInappropriateWeight() {
//        return inappropriateWeight;
//    }
//
//    public void setInappropriateWeight(double inappropriateWeight) {
//        this.inappropriateWeight = inappropriateWeight;
//    }

    public long getMaxResponseTimeSuccessMs() {
        if(successResponseTimes == null || successResponseTimes.size() == 0)
            return 0L;
        long maxResponseTimeSuccessMs = 0L;
        for(long responseTime : successResponseTimes) {
            if(responseTime > maxResponseTimeSuccessMs)
                maxResponseTimeSuccessMs = responseTime;
        }
        return maxResponseTimeSuccessMs;
    }

    public long getAvgResponseTimeSuccessMs() {
        if(successes == 0) return 0L;
        return cumulativeSuccessResponseTimeMs / successes;
    }

    public long getMinResponseTimeSuccessMs() {
        if(successResponseTimes == null || successResponseTimes.size() == 0)
            return 0L;
        long minResponseTimeSuccessMs = 999999999999999999L;
        for(long responseTime : successResponseTimes) {
            if(responseTime < minResponseTimeSuccessMs)
                minResponseTimeSuccessMs = responseTime;
        }
        return minResponseTimeSuccessMs;
    }

    public long getMaxResponseTimeMissMs() {
        if(missesResponseTimes == null || missesResponseTimes.size() == 0)
            return 0L;
        long maxResponseTimeMissMs = 0L;
        for(long responseTime : missesResponseTimes) {
            if(responseTime > maxResponseTimeMissMs)
                maxResponseTimeMissMs = responseTime;
        }
        return maxResponseTimeMissMs;
    }

    public long getAvgResponseTimeMissMs() {
        if(misses == 0) return 0L;
        return cumulativeMissesResponseTimeMs / misses;
    }

    public long getMinResponseTimeMissMs() {
        if(missesResponseTimes == null || missesResponseTimes.size() == 0)
            return 0L;
        long minResponseTimeMissesMs = 999999999999999999L;
        for(long responseTime : missesResponseTimes) {
            if(responseTime < minResponseTimeMissesMs)
                minResponseTimeMissesMs = responseTime;
        }
        return minResponseTimeMissesMs;
    }

    public long getMaxResponseTimeNegativeMs() {
        if(negativeResponseTimes == null || negativeResponseTimes.size() == 0)
            return 0L;
        long maxResponseTimeNegativeMs = 0L;
        for(long responseTime : negativeResponseTimes) {
            if(responseTime > maxResponseTimeNegativeMs)
                maxResponseTimeNegativeMs = responseTime;
        }
        return maxResponseTimeNegativeMs;
    }

    public long getAvgResponseTimeNegativeMs() {
        if(negative == 0) return 0L;
        return cumulativeNegativeResponseTimeMs / negative;
    }

    public long getMinResponseTimeNegativeMs() {
        if(negativeResponseTimes == null || negativeResponseTimes.size() == 0)
            return 0L;
        long minResponseTimeNegativeMs = 999999999999999999L;
        for(long responseTime : negativeResponseTimes) {
            if(responseTime < minResponseTimeNegativeMs)
                minResponseTimeNegativeMs = responseTime;
        }
        return minResponseTimeNegativeMs;
    }

    public long getMaxResponseTimeInappropriateMs() {
        if(inappropriateResponseTimes == null || inappropriateResponseTimes.size() == 0)
            return 0L;
        long maxResponseTimeInappropritaeMs = 0L;
        for(long responseTime : inappropriateResponseTimes) {
            if(responseTime > maxResponseTimeInappropritaeMs)
                maxResponseTimeInappropritaeMs = responseTime;
        }
        return maxResponseTimeInappropritaeMs;
    }

    public long getAvgResponseTimeInappropriateMs() {
        if(inappropriate == 0) return 0L;
        return cumulativeInappropriateResponseTimeMs / inappropriate;
    }

    public long getMinResponseTimeInappropriateMs() {
        if(inappropriateResponseTimes == null || inappropriateResponseTimes.size() == 0)
            return 0L;
        long maxResponseTimeInappropriateMs = 0L;
        for(long responseTime : inappropriateResponseTimes) {
            if(responseTime > maxResponseTimeInappropriateMs)
                maxResponseTimeInappropriateMs = responseTime;
        }
        return maxResponseTimeInappropriateMs;
    }

    @Override
    public Map<String, Object> toMap() {
        return null;
    }

    @Override
    public void fromMap(Map<String, Object> map) {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=");
        sb.append(name);
        sb.append(", baseline=");
        sb.append(baseline.toString());
        sb.append(", timeStarted=");
        sb.append(timeStarted.toString());
        sb.append(", timeEnded=");
        sb.append(timeEnded.toString());
        sb.append(", difficulty=");
        sb.append(difficulty);
        sb.append(", cardsUsed=");
        sb.append(cardsUsed.toString());
        sb.append(", successes=");
        sb.append(successes);
        sb.append(", successResponseTimes=");
        sb.append(successResponseTimes.toString());
        sb.append(", minResponseTimeSuccessMs=");
        sb.append(getMinResponseTimeSuccessMs());
        sb.append(", avgResponseTimeSuccessMs=");
        sb.append(getAvgResponseTimeSuccessMs());
        sb.append(", maxResponseTimeSuccessMs=");
        sb.append(getMaxResponseTimeSuccessMs());
        sb.append(", misses=");
        sb.append(misses);
        sb.append(", missesResponseTimes=");
        sb.append(missesResponseTimes.toString());
        sb.append(", minResponseTimeMissMs=");
        sb.append(getMinResponseTimeMissMs());
        sb.append(", avgResponseTimeMissMs=");
        sb.append(getAvgResponseTimeMissMs());
        sb.append(", maxResponseTimeMissMs=");
        sb.append(getMaxResponseTimeMissMs());
        sb.append(", negative=");
        sb.append(negative);
        sb.append(", negativeResponseTimes=");
        sb.append(negativeResponseTimes.toString());
        sb.append(", minResponseTimeNegativeMs=");
        sb.append(getMinResponseTimeNegativeMs());
        sb.append(", avgResponseTimeNegativeMs=");
        sb.append(getAvgResponseTimeNegativeMs());
        sb.append(", maxResponseTimeNegativeMs=");
        sb.append(getMaxResponseTimeNegativeMs());
        sb.append(", inappropriate=");
        sb.append(inappropriate);
        sb.append(", inappropriateResponseTimes=");
        sb.append(inappropriateResponseTimes.toString());
        sb.append(", minResponseTimeInappropriateMs=");
        sb.append(getMinResponseTimeInappropriateMs());
        sb.append(", avgResponseTimeInappropriateMs=");
        sb.append(getAvgResponseTimeInappropriateMs());
        sb.append(", maxResponseTimeInappropriateMs=");
        sb.append(getMaxResponseTimeInappropriateMs());
        return sb.toString();
    }
}
