package libs.espressif.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EspRSA {
    private static final String ALGORITHM = "RSA";

    private RSAPublicKey mPublicKey;
    private RSAPrivateKey mPrivateKey;

    private Cipher mEncryptCipher;
    private Cipher mDecryptCipher;

    public EspRSA(byte[] publicKey, byte[] privateKey) {
        try {
            if (publicKey != null) {
                mPublicKey = getPublicKey(publicKey);
            }
            if (privateKey != null) {
                mPrivateKey = getPrivateKey(privateKey);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Generate RSA Key error");
        }

        try {
            initCipher();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Generate RSA Cipher error");
        }
    }

    public EspRSA(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        mPublicKey = publicKey;
        mPrivateKey = privateKey;

        try {
            initCipher();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Generate RSA Cipher error");
        }
    }

    private void initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (mPublicKey != null) {
            mEncryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            mEncryptCipher.init(Cipher.ENCRYPT_MODE, mPublicKey);
        }

        if (mPrivateKey != null) {
            mDecryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            mDecryptCipher.init(Cipher.DECRYPT_MODE, mPrivateKey);
        }
    }

    public byte[] encrypt(byte[] data) {
        try {
            return mEncryptCipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] decrypt(byte[] data) {
        try {
            return mDecryptCipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static RSAPublicKey getPublicKey(byte[] keyData)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyData);
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    private static RSAPrivateKey getPrivateKey(byte[] keyData)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyData);
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    public static byte[] encryptWithPublicKey(byte[] publicKey, byte[] data) {
        try {
            PublicKey rsaPB = getPublicKey(publicKey);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPB);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] decryptWithPrivateKey(byte[] privateKey, byte[] data) {
        try {
            Key rsaPV = getPrivateKey(privateKey);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPV);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] decryptWithPrivateKey(PrivateKey privateKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static PrivateKey getPrivateKeyWithPKCS1(byte[] privateKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
//        RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(privateKeyBytes));
//        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
//        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
//        PrivateKey priKey= keyFactory.generatePrivate(rsaPrivKeySpec);
//        return priKey;
//    }
}
