package stream.model;

import java.math.BigDecimal;

public enum MusicalRange {
    C(new BigDecimal(2.973095938), 11),
    Db(new BigDecimal(3.149876222), 11),
    D(new BigDecimal(3.337167863), 11),
    Eb(new BigDecimal(3.535595864), 11),
    E(new BigDecimal(3.745822394), 11),
    F(new BigDecimal(3.968548993), 11),
    Gb(new BigDecimal(4.204518916), 11),
    G(new BigDecimal(7), 7),
    Ab(new BigDecimal(4.719385347), 11),
    A(new BigDecimal(5), 11),
    Bb(new BigDecimal(5.2973), 11),
    B(new BigDecimal(5.612277458), 11);

    private final BigDecimal baseStepInFreq;
    private final int baseIndex;

    MusicalRange(final BigDecimal baseStepInHertz, final int baseIndex) {
        this.baseStepInFreq = baseStepInHertz;
        this.baseIndex = baseIndex;
    }

    public BigDecimal getBaseStepInFreq() {
        return this.baseStepInFreq;
    }

    public float getSampleRate(final int bufferSize) {
        return this.baseStepInFreq.multiply(new BigDecimal(bufferSize)).floatValue();
    }

    public int getBaseIndex() {
        return this.baseIndex;
    }
}
