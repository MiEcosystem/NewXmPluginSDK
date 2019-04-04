package com.xiaomi.smarthome.camera;

public interface XmDecrypt {

    /**
     * 获取自己的私钥和公钥,插件使用填充P2P info
     *
     * @param publicKey  公钥
     * @param privateKey 私钥
     * @see P2PInfo
     */
    void getKeyPair(byte[] publicKey, byte[] privateKey);

    /**
     * 使用自己的私钥和对方的公钥计算出加密秘钥
     *
     * @param remotePublicKey 对方的公钥
     * @param myPrivateKey    自己的私钥
     * @return 加密秘钥
     */
    byte[] getShareKey(byte[] remotePublicKey, byte[] myPrivateKey);

    /**
     * 秘钥转换为字符串 HEX 编码方式
     *
     * @param key 秘钥字节流
     * @return 转换的字符串
     */
    String byteToString(byte[] key);

    /**
     * 字符串转换为秘钥 HEX 编码方式
     *
     * @param key 秘钥字符串
     * @return 转换的字节流
     */
    byte[] stringToByte(String key);

}
