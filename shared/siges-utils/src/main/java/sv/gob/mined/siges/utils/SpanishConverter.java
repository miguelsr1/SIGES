/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.utils;

public class SpanishConverter
        extends AbstractDecimalConverter {

    /**
     * String array to define the Spanish conversion of numbers for 1 till 29.
     *
     * @see #getNumNames()
     * @see #convertLessThanOneThousand(int)
     */
    private static final String[] NUM_NAMES = {
        "", "un", "dos", "tres", "cuatro", "cinco",
        "seis", "siete", "ocho", "nueve",
        "diez", "once", "doce", "trece", "catorce",
        "quince", "diecis\u00E9is", "diecisiete", "dieciocho", "diecinueve", "veinte",
        "veintiun", "veintid\u00F3s", "veintitr\u00E9s", "veinticuatro", "veinticinco",
        "veintis\u00E9is", "veintisiete", "veintiocho", "veintinueve"};

    /**
     * String array to define the conversion for the numbers 10, 20, 30, 40, 50,
     * 60, 70, 80 and 90.
     *
     * @see #getTensNames()
     * @see #convertLessThanOneThousand(int)
     */
    private static final String[] TENS_NAMES = {
        "", "diez", "veinte", "treinta", "cuarenta", "cincuenta",
        "sesenta", "setenta", "ochenta", "noventa", "ciento"};

    /**
     * String array to define the conversion for the hundred numbers 100, 200,
     * 300, 400, 500, 600, 700, 800 and 900. In Spanish for some hundred numbers
     * exists special wordings.
     *
     * @see #convertLessThanOneThousand(int)
     */
    private static final String[] HUNDREDS_NAMES = {
        "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos",
        "seiscientos", "setecientos", "ochocientos", "novecientos"};

    /**
     * String array to define the conversion for the log numbers 100,
     * 1&nbsp;000, 1&nbsp;000&nbsp;000 and 1&nbsp;000&nbsp;000&nbsp;000.
     *
     * @see #getPowerNames()
     */
    private static final String[] POWER_NAMES = {
        "mil", "millones", "mil millones", "billones", "mil billones", "trillones"};

    /**
     * String array to define the conversion of power numbers with exact one.
     * The array contains the Spanish words for
     * <ul>
     * <li>one thousand</li>
     * <li>one million</li>
     * <li>one billion</li>
     * <li>one trillion</li>
     * <li>one quadrillion</li>
     * <li>one quintillion</li>
     * </ul>
     *
     * @see #convertPower(int, int)
     */
    private static final String[] SINGLE_POWER_NAMES = {
        "un mil", "un mill\u00F3n", "mil millones", "un bill\u00F3n", "mil billones", "un trill\u00F3n"};

    /**
     * Method to convert the numbers from 1 to 999. The original method must be
     * overwritten because of some specialties of the Spanish languages for
     * numbers below 30 and for all hundred numbers (100, 200, ...).
     *
     * @param _number number to be converted
     * @return return words for number
     */
    @Override
    protected String convertLessThanOneThousand(final int _number) {
        final StringBuilder ret = new StringBuilder();
        if (_number == 100) {
            ret.append("cien");
        } else {
            ret.append(SpanishConverter.HUNDREDS_NAMES[_number / 100])
                    .append(' ')
                    .append(convertLessThanOneHundred(_number % 100));
        }
        return ret.toString();
    }

    /**
     * The method converts the numbers from 1 to 99 into words. The method is
     * used from
     * {@link AbstractDecimalConverter#convertLessThanOneThousand(int)}.
     *
     * @param _number number less than one hundred to convert
     * @return converted <code>_number</code> in words
     */
    @Override
    protected String convertLessThanOneHundred(final int _number) {
        final StringBuilder ret = new StringBuilder();

        // between 1 and 29
        if (_number < 30) {
            ret.append(" ").append(SpanishConverter.NUM_NAMES[_number]);
            // all others
        } else {
            ret.append(" ").append(SpanishConverter.TENS_NAMES[_number / 10]);
            final String num = SpanishConverter.NUM_NAMES[_number % 10];
            if (!"".equals(num)) {
                ret.append(" y ").append(SpanishConverter.NUM_NAMES[_number % 10]);
            }
        }
        return ret.toString();
    }

    /**
     * The method converts the given <code>_number</code> depending on the
     * <code>_power</code> to words. The real number to convert is
     * &quot;<code>_number * (10 ^ _power)</code>&quot;. The original method is
     * overwritten because if <code>_number</code> is equal one, the values from
     * {@link #SINGLE_POWER_NAMES} must be used.
     *
     * @param _number number to convert
     * @param _power power of the number
     * @return converted string
     * @see #SINGLE_POWER_NAMES
     */
    @Override
    protected String convertPower(final int _number,
            final int _power) {
        return (_number == 1)
                ? SpanishConverter.SINGLE_POWER_NAMES[_power]
                : super.convertPower(_number, _power);
    }

    /**
     * Returns the string array to define the conversion of numbers for 1 till
     * 19.
     *
     * @return string array of numbers
     * @see AbstractDecimalConverter#getNumNames()
     * @see #NUM_NAMES
     */
    @Override
    protected String[] getNumNames() {
        return SpanishConverter.NUM_NAMES;
    }

    /**
     * Returns the string array for the numbers 10, 20, 30, 40, 50, 60, 70, 80
     * and 90.
     *
     * @return string array of tens names
     * @see AbstractDecimalConverter#getTensNames()
     * @see #TENS_NAMES
     */
    @Override
    protected String[] getTensNames() {
        return SpanishConverter.TENS_NAMES;
    }

    /**
     * Returns the string array for log numbers 100, 1000, 1000000 and
     * 1000000000.
     *
     * @return string array of log numbers
     * @see AbstractDecimalConverter#getPowerNames()
     * @see #POWER_NAMES
     */
    @Override
    protected String[] getPowerNames() {
        return SpanishConverter.POWER_NAMES;
    }

    /**
     * Returns the related Spanish word for the number zero ('0').
     *
     * @return &quot;cero&quot;
     */
    @Override
    protected String getZero() {
        return "cero";
    }

    /**
     * Returns the related Spanish word for &quot;minus&quot; needed for
     * negative numbers.
     *
     * @return always the text string &quot;menos&quot;
     */
    @Override
    protected String getMinus() {
        return "menos";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convert(final long _number) {
        final StringBuilder ret = new StringBuilder();
        ret.append(super.convert(_number));
        if (Long.valueOf(_number).toString().endsWith("1") && _number != 11) {
            ret.append("o");
        }
        return ret.toString();
    }
}
