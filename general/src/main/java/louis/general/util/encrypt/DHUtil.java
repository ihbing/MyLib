package louis.general.util.encrypt;
/**
 * Created by Administrator on 2018/1/12 0012.
 */
import javax.crypto.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
/**
 * Created by xiang.li on 2015/3/4.
 * DH 加解密工具类
 */
public class DHUtil {

    /**
     * 定义加密方式
     */
    private static final String KEY_DH = "DH";

    /**
     * DH加密下需要一种对称加密算法对数据加密，这里我们使用DES，也可以使用其他对称加密算法
     */
    private static final String KEY_DH_DES = "DES";

    /**
     * 构建本地密钥
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     */
    private static SecretKey getSecretKey(String publicKey, String privateKey) {
        SecretKey secretKey = null;
        try {
            // 初始化公钥
            byte[] publicBytes = decryptBase64(publicKey);
            KeyFactory factory = KeyFactory.getInstance(KEY_DH);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            PublicKey localPublicKey = factory.generatePublic(keySpec);

            // 初始化私钥
            byte[] privateBytes = decryptBase64(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateBytes);
            PrivateKey localPrivateKey = factory.generatePrivate(spec);

            KeyAgreement agreement = KeyAgreement.getInstance(factory.getAlgorithm());
            agreement.init(localPrivateKey);
            agreement.doPhase(localPublicKey, true);

            // 生成本地密钥
            secretKey = agreement.generateSecret(KEY_DH_DES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public static String encryptDH(String data){
        return Base64Util.encode(encryptDH(data.getBytes()));
    }

    public static byte[] encryptDH(byte[] data){
        String publicKeyA ="MIIBHzCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECA4GEAAKBgG3V2PnU0B/uuXcPCIrvsmKLi5Xnuu0JvoDodbZcAn6Q0MNKPitFHvRaPbSU14wYmjQ0czkQE7T2GeOm5MTg+wNryfSssax/iP7/vtdoZT8d5M0Oi/wC+lR3K1Ao+Jqp1RKCajgFttovFHcBhbAYMeKKfTj4tr3oehAKDMCqOfr6";
        String privateKeyB = "MIIBIQIBADCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECBIGDAoGAVgNJKWYjunh9I9S+PQkvA7tysUrgOF+493DhbXicelLRgKhQMBW9hAb26t6vVQLBGfw79Yq0urrhS8GNKpUFlAOpgxFgQNVeJU/V7hYlIlFpRpjfzURtnDOIt8zKOSEM6rKg1sId83G8FPeP9YhDquw393wuve0ZffgRRh4P86U=";
        // 由甲方公钥，乙方私钥构建密文
        return encryptDH(data, publicKeyA, privateKeyB);

    }
    /**
     * DH 加密
     * @param data 带加密数据
     * @param publicKey 甲方公钥
     * @param privateKey 乙方私钥
     * @return
     */
    public static byte[] encryptDH(byte[] data, String publicKey, String privateKey) {
        byte[] bytes = null;
        try {
            // 生成本地密钥
            SecretKey secretKey = getSecretKey(publicKey, privateKey);
            // 数据加密
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            bytes = cipher.doFinal(data);
        } catch (Throwable t){
            t.printStackTrace();
        }
        return bytes;
    }

    public static String decryptDH(String data){
        return  new String(decryptDH(Base64Util.decode(data)));
    }

    public static byte[] decryptDH(byte[] encWord){
        String privateKeyA ="MIIBIQIBADCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECBIGDAoGAfGp6pyufygR0UGeRLuXSVD7QPRzA5czFpsYiIqDFxsA4EKsw1sqhd8UhOfaJBDVYl4Gg5WTB/hF+mJR/tnpZPDyb00TkaElqQ3re7D5SoLRIL3ljyrOMX2K2M46j/X9/45FmTzfSxhRqLap19SzFvyariD9yDN86pTjQ8zyqjMo=" ;
        String publicKeyB = "MIIBHzCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECA4GEAAKBgFZel0ZK72YMCCjgYeq/I2aHr8S3RBkOt5vOaYVXvS2njtKRDTmHfKcjyfZM/HcIcTs4v6IpBt/YVESaU4HRZ7DZ6yAT0gr9V3lhWNoWbLdDyT0uGa7/8aYijcEEcFZ8kM9BT5GO9vC1dzrVPMUJU9HeqAh++RsjuYq0HpZHNbf6";
        // 由乙方公钥，甲方私钥解密
        return decryptDH(encWord, publicKeyB, privateKeyA);
    }
    /**
     * DH 解密
     * @param data 待解密数据
     * @param publicKey 乙方公钥
     * @param privateKey 甲方私钥
     * @return
     */
    public static byte[] decryptDH(byte[] data, String publicKey, String privateKey) {
        byte[] bytes = null;
        try {
            // 生成本地密钥
            SecretKey secretKey = getSecretKey(publicKey, privateKey);
            // 数据解密
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            bytes = cipher.doFinal(data);
        } catch (Throwable t){
            t.printStackTrace();
        }
        return bytes;
    }



    /**
     * BASE64 解密
     * @param key 需要解密的字符串
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return Base64Util.decode(key);
    }

    /**
     * BASE64 加密
     * @param key 需要加密的字节数组
     * @return 字符串
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return Base64Util.encode(key);
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        // 生成甲方密钥对
        // Map<String, Object> mapA = init();
        String publicKeyA ="MIIBHzCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECA4GEAAKBgG3V2PnU0B/uuXcPCIrvsmKLi5Xnuu0JvoDodbZcAn6Q0MNKPitFHvRaPbSU14wYmjQ0czkQE7T2GeOm5MTg+wNryfSssax/iP7/vtdoZT8d5M0Oi/wC+lR3K1Ao+Jqp1RKCajgFttovFHcBhbAYMeKKfTj4tr3oehAKDMCqOfr6"; //getPublicKey(mapA);
        String privateKeyA ="MIIBIQIBADCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECBIGDAoGAfGp6pyufygR0UGeRLuXSVD7QPRzA5czFpsYiIqDFxsA4EKsw1sqhd8UhOfaJBDVYl4Gg5WTB/hF+mJR/tnpZPDyb00TkaElqQ3re7D5SoLRIL3ljyrOMX2K2M46j/X9/45FmTzfSxhRqLap19SzFvyariD9yDN86pTjQ8zyqjMo=" ;//getPrivateKey(mapA);
        //  System.out.println("甲方公钥:\n" + publicKeyA);
        //  System.out.println("甲方私钥:\n" + privateKeyA);

        // 由甲方公钥产生本地密钥对
        // Map<String, Object> mapB = init(publicKeyA);
        String publicKeyB = "MIIBHzCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECA4GEAAKBgFZel0ZK72YMCCjgYeq/I2aHr8S3RBkOt5vOaYVXvS2njtKRDTmHfKcjyfZM/HcIcTs4v6IpBt/YVESaU4HRZ7DZ6yAT0gr9V3lhWNoWbLdDyT0uGa7/8aYijcEEcFZ8kM9BT5GO9vC1dzrVPMUJU9HeqAh++RsjuYq0HpZHNbf6";//getPublicKey(mapB);
        String privateKeyB = "MIIBIQIBADCBlQYJKoZIhvcNAQMBMIGHAoGBAObfWykKca1h/JkSNf7ihri6kR2v3Z5Hmdm12OtscjoWGMQsBndyYfk7CLxkus7MYpkMO6muZYM6Rs325JH1MfBPSKrpBOXrlGc1cZ+y0zbacT4jRqHRf7/FyLnCjhJEC1406pd12peE7SGfYLDxzm5SiB/qOxhnQk1vIvpJi2cbAgECBIGDAoGAVgNJKWYjunh9I9S+PQkvA7tysUrgOF+493DhbXicelLRgKhQMBW9hAb26t6vVQLBGfw79Yq0urrhS8GNKpUFlAOpgxFgQNVeJU/V7hYlIlFpRpjfzURtnDOIt8zKOSEM6rKg1sId83G8FPeP9YhDquw393wuve0ZffgRRh4P86U=";//getPrivateKey(mapB);
        //  System.out.println("乙方公钥:\n" + publicKeyB);
        // System.out.println("乙方私钥:\n" + privateKeyB);

        String word = "1512215694016";
        //  System.out.println("原文: " + word);

        // 由甲方公钥，乙方私钥构建密文
        byte[] encWord = encryptDH(word.getBytes(), publicKeyA, privateKeyB);

        // 由乙方公钥，甲方私钥解密
        byte[] decWord = decryptDH(encWord, publicKeyB, privateKeyA);

    }
}
