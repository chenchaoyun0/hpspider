package com.megvii.dzh.spider.common.utils;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CookieUtils {

  public static Map<String, String> getCookiesMap(String cookieStrs) {
    String[] split = StringUtils.split(cookieStrs, ';');
    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < split.length; i++) {
      String cookieMap = split[i];
      String key = StringUtils.substringBefore(cookieMap, "=");
      String value = StringUtils.substringAfter(cookieMap, "=");
      map.put(key, value);

    }
    return map;
  }

  public static void main(String args[]) {
    String cookieStrs = "_dacevid3=f2f994b1.5bad.47cb.9630.a884c6abed3f; __gads=ID=3d444c38736e39ba:T=1525453594:S=ALNI_MadENIKu0QwTvgGLLdj2P8yQiY6bQ; _HUPUSSOID=79521e1e-fec2-4647-b2c8-e4dc84dd8ac4; _CLT=918ebe7bb324d8673460f7af1d701a5c; AUM=dgR7lnYDBW69CyJJpHu_3es3jN5nt-yqlWtWpTno5j7lw; lastvisit=276%091538559002%09%2Ferror%2F%40_%40.php%3F; u=26414109|6Im+54m557Gz5pav54m5|22f3|486fd412f17389a79e54ca6d893d80f5|f17389a79e54ca6d|6Im+54m557Gz5pav54m5; us=47d47f6d4078d22f86432e59e24915d2694220a778a038e9a0c2e578d423c549db33db4cad17cf0113229b9a5c9d95c55e94f664be867556d0aef5af2cc32cac; _cnzz_CV30020080=buzi_cookie%7Cf2f994b1.5bad.47cb.9630.a884c6abed3f%7C-1; Hm_lvt_39fc58a7ab8a311f2f6ca4dc1222a96e=1538568189,1538568350,1538568979,1538631445; PHPSESSID=2fbd8f3df5d23b9daa83ba2928519431; _fmdata=4y926Zzt8Ck%2BeVTmL2EVELFeapXRbe75dF7sIOPhFNT8plhPmUexOiapwqzpK0%2BclXnJrJNvTPj0O5cmEfdyInDQZhjI4wiYtf0fVY3CSQE%3D; ua=153863973; __dacevst=53ac8b5d.fe349230|1538646071200; Hm_lpvt_39fc58a7ab8a311f2f6ca4dc1222a96e=1538644271";
    System.out.println(JSONObject.toJSONString(getCookiesMap(cookieStrs)));
  }
}
