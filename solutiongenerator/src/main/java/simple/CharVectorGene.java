/**
 * CharVectorGene.java
 *
 * This is an example only! Use it for anything else at your own risk!
 * You have been warned! Coder/user beware!
 */
package simple;

import ec.EvolutionState;
import ec.util.Parameter;
import ec.vector.VectorGene;

/**
 * @author carlos
 */
public class CharVectorGene extends VectorGene {
    public final static String P_ALPHABET = "alphabet";

    private static char[]      alphabet;
    private char               allele;

    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);

        Parameter def = defaultBase();

        String alphabetStr = state.parameters.getStringWithDefault(
                          base.push(P_ALPHABET), def.push(P_ALPHABET), "");
        if (alphabetStr.length() == 0)
            state.output.fatal(
                       "CharVectorGene must have a default alphabet",
                       base.push(P_ALPHABET));

        alphabet = alphabetStr.toCharArray();
    }

    /*
     * (non-Javadoc)
     * @see ec.vector.VectorGene#reset(ec.EvolutionState, int)
     */
    @Override
    public void reset(EvolutionState state, int thread) {
        int idx = state.random[thread].nextInt(alphabet.length);
        allele = alphabet[idx];
    }

    public char getAllele() {
        return allele;
    }

    /*
     * (non-Javadoc)
     * @see ec.vector.VectorGene#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (!this.getClass().isInstance(other)) {
            return false;
        }

        CharVectorGene that = (CharVectorGene) other;

        return allele == that.allele;
    }

    /*
     * @see ec.vector.VectorGene#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = this.getClass().hashCode();

        hash = (hash << 1 | hash >>> 31) ^ allele;

        return hash;
    }

    @Override
    public Object clone() {
        CharVectorGene charVectorGene = (CharVectorGene) (super.clone());

        return charVectorGene;
    }

    @Override
    public String toString() {
        return Character.toString(allele);
    }
}