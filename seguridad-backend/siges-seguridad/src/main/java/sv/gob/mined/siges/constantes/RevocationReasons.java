package sv.gob.mined.siges.constantes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fabricio
 */
public enum RevocationReasons {
    NOT_REVOKED(-1, "NOT_REVOKED", "The Certificate Is Not Revoked"),
    UNSPECIFIED(0, "UNSPECIFIED", "Unspecified"),
    KEYCOMPROMISE(1, "KEY_COMPROMISE", "Key Compromise"),
    CACOMPROMISE(2, "CA_COMPROMISE", "CA Compromise"),
    AFFILIATIONCHANGED(3, "AFFILIATION_CHANGED", "Affiliation Changed"),
    SUPERSEDED(4, "SUPERSEDED", "Superseded"),
    CESSATIONOFOPERATION(5, "CESSATION_OF_OPERATION", "Cessation of Operation"),
    CERTIFICATEHOLD(6, "CERTIFICATE_HOLD", "Certificate Hold"),
    REMOVEFROMCRL(8, "REMOVE_FROM_CRL", "Remove from CRL"),
    PRIVILEGESWITHDRAWN(9, "PRIVILEGES_WITHDRAWN", "Privileges Withdrawn"),
    AACOMPROMISE(10, "AA_COMPROMISE", "AA Compromise");
    
    private final int databaseValue;
    private final String stringValue;
    private final String humanReadable;

    private static final Map<Integer, RevocationReasons> databaseLookupMap = new HashMap<>();
    private static final Map<String, RevocationReasons> cliLookupMap = new HashMap<>();
    private static final Collection<Integer> reasonableRevocationReasons;
    
    static {
        for(RevocationReasons reason : RevocationReasons.values()) {
            databaseLookupMap.put(reason.getDatabaseValue(), reason);
            cliLookupMap.put(reason.getStringValue(), reason);
        }
        Collection<Integer> reason = new ArrayList<>();
        reason.add(RevocationReasons.AFFILIATIONCHANGED.getDatabaseValue());
        reason.add(RevocationReasons.CESSATIONOFOPERATION.getDatabaseValue());
        reason.add(RevocationReasons.KEYCOMPROMISE.getDatabaseValue());
        reason.add(RevocationReasons.PRIVILEGESWITHDRAWN.getDatabaseValue());
        reason.add(RevocationReasons.SUPERSEDED.getDatabaseValue());
        reason.add(RevocationReasons.UNSPECIFIED.getDatabaseValue());
        reasonableRevocationReasons = Collections.unmodifiableCollection(reason);
    }

    private RevocationReasons(final int databaseValue, final String stringValue, String humanReadable) {
        this.databaseValue = databaseValue;
        this.stringValue = stringValue;
        this.humanReadable = humanReadable;
    }
    
    public int getDatabaseValue() {
        return databaseValue;
    }
    
    public String getHumanReadable() {
        return humanReadable;
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    /**
     * 
     * @param databaseValue the database value
     * @return the relevant RevocationReasons object, null if none found. 
     */
    public static RevocationReasons getFromDatabaseValue(int databaseValue) {
        return databaseLookupMap.get(databaseValue);
    }
    
    /**
     * 
     * @param cliValue the database value
     * @return the relevant RevocationReasons object, null if none found. 
     */
    public static RevocationReasons getFromCliValue(String cliValue) {
        if(cliValue == null) {
            return null;
        }
        return cliLookupMap.get(cliValue);
    }
    
    /**
     * Returns the collection of reasonable revocation reason codes.
     * 
     * Verify that the revocation reason code is allowed (see https://tools.ietf.org/html/rfc8555#section-7.6)
     * ACME server MAY disallow a subset may of reasonCodes ... defined in RFC5820 ...
     *  
     * @return the collection of revocation reason codes.
     */
    public static Collection<Integer> getReasonableRevocationReasonCodes() {
        return reasonableRevocationReasons;
    }
}
