package com.gmail.justdontdiebusiness.badlionmod.utils;

import com.gmail.justdontdiebusiness.badlionmod.BadlionMod;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class CipherUtil {

    private KeyPair keyPair;
    private Cipher cipher;

    public CipherUtil(BadlionMod mod) {
        if (mod.getConfig().getKeyPair() == null) {
            try {
                KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
                gen.initialize(1024);
                this.keyPair = gen.generateKeyPair();

                mod.getConfig().setKeyPair(this.keyPair);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            this.keyPair = mod.getConfig().getKeyPair();
        }

        try {
            this.cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public String encryptKey(String key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] bytes = key.getBytes("UTF-8");

            byte[] encrypted = blockCipher(bytes, Cipher.ENCRYPT_MODE);
            char[] encryptedTranspherable = Hex.encodeHex(encrypted);

            return new String(encryptedTranspherable);
        } catch (InvalidKeyException | BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decryptKey(String encryptedKey) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            byte[] bts = Hex.decodeHex(encryptedKey.toCharArray());

            byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);

            return bytesToString(decrypted);
        } catch (InvalidKeyException | DecoderException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException{
        // string initialize 2 buffers.
        // scrambled will hold intermediate results
        byte[] scrambled;

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        // if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
        int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 128;

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[length];

        for (int i=0; i< bytes.length; i++){

            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0)){
                //execute the operation
                scrambled = cipher.doFinal(buffer);
                // add the result to our total result.
                toReturn = append(toReturn,scrambled);
                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than remaining bytes in the bytes array we shorten it.
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i%length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen when encrypting.
        // example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = cipher.doFinal(buffer);

        // final step before we can return the modified data.
        toReturn = append(toReturn,scrambled);

        return toReturn;
    }

    private byte[] append(byte[] prefix, byte[] suffix){
        byte[] toReturn = new byte[prefix.length + suffix.length];
        System.arraycopy(prefix, 0, toReturn, 0, prefix.length);
        System.arraycopy(suffix, 0, toReturn, prefix.length, suffix.length);
        return toReturn;
    }

    private String bytesToString(byte[] data) {
        StringBuilder dataOut = new StringBuilder();
        for (byte aData : data) {
            if (aData != 0x00)
                dataOut.append((char) aData);
        }
        return dataOut.toString();
    }
}
