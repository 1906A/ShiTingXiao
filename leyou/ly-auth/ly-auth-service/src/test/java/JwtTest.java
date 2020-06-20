import com.leyou.common.JwtUtils;
import com.leyou.common.RsaUtils;
import com.leyou.common.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    /**
     * 生成密钥
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    /**
     * 生成公钥 私钥的方法
     * @throws Exception
     */
    @Before  //在执行其他方法值钱,先执行
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 生成token的方法
     * @throws Exception
     */
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU5MjUzMTE3Mn0.khCMAwlbpAaBfT1zxp8Fti3-v0gRRsHAzErVrOmCRry065s1sRwj_bgdkCY7vZ_EBY7x-GUYbAF4jSMj4HcndntJpy0blx1d_XL0obQBqGOENSiknJasma4dLSNrD0LlaNJF6UzkYFSLd-1JAvUwmXNudDx5u3p5ExytSNGjbsI";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }

}
