package org.manuel.lang.srnapp.tokenGenerator;

import org.manuel.lang.srnapp.base32.Base32;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Manu on 18.05.15.
 */
public class TokenGeneration {

    /**
     * Generates a token using:
     * the current minutes of the system's unix time
     * a shared secret that is hardcoded as a private attribute
     * base32 to get a useful byte array from the shared secret
     * sha1 to generate hash values
     * generates a Keyed-Hash Message Authentication Code (hmac) that are useful for SSL and TLS
     * @param sharedSecret a string value with that the hmac is generated
     * @return a token that contains not more than six characters
     */
    public static int getToken(String sharedSecret){
        return getShorterIntValue(convertByteArrayToInt(getFourBytes(stringToByteArray(getHMACAsString(getBase32String(sharedSecret))),getIndex(getBase32String(sharedSecret)))));
    }

    /**
     * cuts four bytes out of an byte array according to a given index value
     * @param input
     * @param index
     * @return
     */
    private static byte[] getFourBytes(byte[] input, int index){
        int i = index;
        int j = 0;
        byte[] result = new byte[4];
        while(j < 4){
            result[j++] = input[i++];
        }
        return result;
    }

    /**
     * get the byte array from an integer value
     * @param bytes
     * @return
     */
    private static int convertByteArrayToInt(byte[] bytes){
        ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
        return wrapped.getInt();
    }

    /**
     * generates an int value that contains 6 characters maximum
     * cuts the token
     * @param value
     * @return a short token value
     */
    private static int getShorterIntValue(int value){
        return value%1000000;
    }

    /**
     * generates an index value depending on the hmac
     * @param sharedSecret
     * @return
     */
    private static int getIndex(String sharedSecret) {
        String hMAC = "";
        hMAC = getHMACAsString(sharedSecret);
        String lastChar = "";
        if(hMAC.length() > 4) {
            lastChar += hMAC.charAt(hMAC.length() - 1);
        }
        return Integer.parseInt(lastChar, 16);
    }

    /**
     * generates the Keyed-Hash Message Authentication Code using the unix time and the shared secret combined
     * with SHA1
     * @param sharedSecret
     * @return
     */
    private static String getHMACAsString(String sharedSecret) {
        return sha1(sharedSecret + sha1(sharedSecret + getCurrentUnixTimeMinutesAsString()));
    }

    /**
     * converts a byte array to a String value
     * @param string
     * @return
     */
    private static byte[] stringToByteArray(String string) {
        return string.getBytes(Charset.forName("UTF-8"));
    }

    /**
     *
     * @return the current system time as string
     * //FIXME use global time (use NTP)
     */
    private static String getCurrentUnixTimeMinutesAsString(){
        long unixTime = System.currentTimeMillis();
        long timestampValue = unixTime / 60000;
        return timestampValue + "";
    }

    /**
     * SHA1
     * @param input
     * @return
     */
    private static String sha1(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private static String getBase32String(String string) {
        return Base32.encode(string.getBytes());
    }
}
