package com.liber.sun.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.liber.sun.constant.Constant;
import com.liber.sun.domain.DataUploadInfo;
import com.liber.sun.domain.MyResponse;
import com.liber.sun.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.bcel.Const;
import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunlingzhi on 2017/12/21.
 */
public class MyHttpUtils {

    public static String GET(String urlString, String encode, Map<String, String> headers, String... m) throws IOException, URISyntaxException {
        String body = "";
        CloseableHttpClient client = checkAuth(m);
        if (client == null) {
            return "验证输入参数存在问题";
        }
        //若地址中设计到了特殊字符，就不能使用String代替URI来访问。必须用%0xxx的方式来代替特殊字符
        //但这种方式不直观，所有先将string转为URL，在通过URL生成URI。
        URL url=new URL(urlString);
        URI uri=new URI(url.getProtocol(), url.getHost()+":"+url.getPort(), url.getPath(), url.getQuery(),null);

        HttpGet httpGet = new HttpGet(uri);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encode);
        }
        EntityUtils.consume(entity);

        //释放链接
        response.close();
        client.close();

        return body;

    }

    public static String POST(String url, String encode, Map<String, String> headers, Map<String, String> params,
                              List<MultipartFile> multipartFiles, String... m) throws IOException {
        String body = "";
        CloseableHttpClient client = checkAuth(m);
        if (client == null) {
            return "验证输入参数存在问题";
        }
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //构建body
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //文件数据
        if (multipartFiles != null&& multipartFiles.size() > 0) {
            for(MultipartFile multipartFile:multipartFiles){
                builder.addBinaryBody("file", multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, multipartFile.getOriginalFilename());
            }
        }
        //parameter
        ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));//解决中文乱码
        if (params != null&& params.size() > 0) {
            for (Map.Entry<String, String> key : params.entrySet()) {
                builder.addTextBody(key.getKey(), key.getValue(),contentType);
            }
        }

        HttpEntity entityIn = builder.build();
        //设置参数到请求对象中
        httpPost.setEntity(entityIn);


        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entityOut = response.getEntity();
        if (entityOut != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entityOut, encode);
        }
        EntityUtils.consume(entityOut);
        //释放链接
        response.close();
        client.close();
        return body;
    }

    public static String postToDataResource(String author,String fileName,String sourceStoreId,String suffix,String type) throws IOException, JSONException {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        String requestUrl ="http://" + Constant.DATA_CONTAINER_IP+":"+Constant.DATA_CONTAINER_PORT+Constant.DATA_CONTAINER_DATARESOURCE;
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setHeader("Content-Type","application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("author",author);
        jsonObject.put("fileName",fileName);
        jsonObject.put("sourceStoreId",sourceStoreId);
        jsonObject.put("suffix",suffix);
        jsonObject.put("type",type);

        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");

        httpPost.setEntity(stringEntity);
        CloseableHttpResponse httpResponse = client.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        httpResponse.close();
        client.close();

        org.json.JSONObject bodyJson = new org.json.JSONObject(body);
        int code = bodyJson.getInt("code");
        if(code!=0){
            return "";
        }else{
            return bodyJson.getString("data");
        }
    }

    public static String postOutputFiletoDC(String recordId) throws IOException, JSONException {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        String requestUrl ="http://" + Constant.SAGA_SERVER_IP+":"+Constant.SAGA_SERVER_PORT+Constant.MODEL_RUN_OUTPUT+"/"+recordId;
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setHeader("Content-Type","application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip",Constant.DATA_CONTAINER_IP);
        jsonObject.put("port",Constant.DATA_CONTAINER_PORT);
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");

        httpPost.setEntity(stringEntity);
        CloseableHttpResponse httpResponse = client.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        httpResponse.close();
        client.close();

        org.json.JSONObject bodyJson = new org.json.JSONObject(body);
        int code = bodyJson.getInt("code");
        if(code!=1){
            return "";
        }else{
            return bodyJson.getString("data");
        }
    }

    public static String PostFileUrlToMC(String requestUrl, List<DataUploadInfo> dataUploadInfos) throws IOException, JSONException {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setHeader("Content-Type","application/json");
        String dataUrl = "";
        String fileName = "";
        DataUploadInfo dataUploadInfo = dataUploadInfos.get(0);
        fileName = dataUploadInfo.getFileName();
        // 如果是单个数据：
        if(dataUploadInfos.size()==1){
            dataUrl = "http://" + Constant.DATA_CONTAINER_IP+":"+ Constant.DATA_CONTAINER_PORT+Constant.DOWNLOAD_ONE_DATA_ROUTER+"?fileName="+dataUploadInfo.getFileName()+"&sourceStoreId="+dataUploadInfo.getSourceStoreId()+"&suffix="+dataUploadInfo.getSuffix();
        }else if(dataUploadInfos.size()>1){
            dataUrl = "http://" + Constant.DATA_CONTAINER_IP+":"+ Constant.DATA_CONTAINER_PORT + Constant.DOWNLOAD_MULTI_DATA_ROUTER+"?sourceStoreId="+dataUploadInfo.getSourceStoreId();
            for(int i = 1; i<dataUploadInfos.size();i++){
                dataUrl = dataUrl + "&sourceStoreId="+ dataUploadInfos.get(i).getSourceStoreId();
                fileName = fileName+"_list";
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gd_tag",fileName);
        jsonObject.put("data",dataUrl);
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse httpResponse = client.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        httpResponse.close();
        client.close();

        org.json.JSONObject bodyJson = new org.json.JSONObject(body);
        int code = bodyJson.getInt("code");
        if(code!=1){
            return "";
        }else{
            return bodyJson.getString("data");
        }
    }

    public static String POSTRawString(String url, String encode,Map<String, String> headers, String stringJson,  String... m) throws IOException {
        String body = "";
        CloseableHttpClient client = checkAuth(m);
        if (client == null) {
            return "验证输入参数存在问题";
        }
        HttpPost httpost = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        StringEntity stringEntity = new StringEntity(stringJson, encode);

        httpost.setEntity(stringEntity);


        CloseableHttpResponse httpResponse = client.execute(httpost);
        HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encode);
        }
        EntityUtils.consume(entity);
        //释放链接
        httpResponse.close();
        client.close();
        return body;
    }

    public static String DELETE(String url,String encode,Map<String,String> headers,String ... m) throws IOException {
        String body = "";
        CloseableHttpClient client = checkAuth(m);
        if (client == null) {
            return "验证输入参数存在问题";
        }
        HttpDelete httpdelete = new HttpDelete(url);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpdelete.setHeader(entry.getKey(), entry.getValue());
            }
        }

        CloseableHttpResponse response = client.execute(httpdelete);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encode);
        }
        EntityUtils.consume(entity);

        //释放链接
        response.close();
        client.close();

        return body;

    }

    public static String PUTRawString(String url, String encode,Map<String, String> headers, String stringJson,  String... m) throws IOException {
        String body = "";
        CloseableHttpClient client = checkAuth(m);
        if (client == null) {
            return "验证输入参数存在问题";
        }
        HttpPut httpput = new HttpPut(url);
        httpput.setHeader("Content-type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(), entry.getValue());
            }
        }

        StringEntity stringEntity = new StringEntity(stringJson, encode);
        httpput.setEntity(stringEntity);


        CloseableHttpResponse httpResponse = client.execute(httpput);
        HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encode);
        }
        EntityUtils.consume(entity);
        //释放链接
        httpResponse.close();
        client.close();
        return body;

    }

    public static String PUTRawFile(String url, String encode, Map<String, String> headers, File file, String... m) throws IOException {
        String body = "";
        CloseableHttpClient client = checkAuth(m);
        if (client == null) {
            return "验证输入参数存在问题";
        }
        HttpPut httpput = new HttpPut(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(), entry.getValue());
            }
        }


        FileEntity fileEntity=new FileEntity(file);
        httpput.setEntity(fileEntity);


        CloseableHttpResponse httpResponse = client.execute(httpput);
        HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encode);
        }
        EntityUtils.consume(entity);
        //释放链接
        httpResponse.close();
        client.close();
        return body;

    }

    public static CloseableHttpClient checkAuth(String... m) {
        if (m.length == 2) {//需要验证
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(m[0], m[1]);
            AuthScope scope = new AuthScope(AuthScope.ANY);
            credsProvider.setCredentials(scope, creds);
            return HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        } else if (m.length == 0) {//不用验证
            return HttpClients.createDefault();
        } else {
            return null;
        }
    }




    /**
     *
     * @param url
     * @param encode
     * @param paramTag   服务容器接受的参数是Tag
     * @return
     * @throws IOException
     */
    public static String POSTFileToModelContainer(String url, String encode, String paramTag,File file) throws IOException {
        String body = "";
        CloseableHttpClient client =HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        //设置header
        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.setHeader("Account-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
        //构建body
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addBinaryBody("myfile", MyFileUtils.getFileContentByInputStream(file), ContentType.MULTIPART_FORM_DATA, file.getName());

        //parameter
        ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));//解决中文乱码
        builder.addTextBody("tag", paramTag,contentType);


        HttpEntity entityIn = builder.build();
        //设置参数到请求对象中
        httpPost.setEntity(entityIn);


        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entityOut = response.getEntity();
        if (entityOut != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entityOut, encode);
        }
        EntityUtils.consume(entityOut);
        //释放链接
        response.close();
        client.close();
        return body;
    }

    public static String getShpMeta(String id) throws IOException, URISyntaxException {
        String body = "";
        CloseableHttpClient client =HttpClients.createDefault();
        String reqUrl = "http://"+Constant.DATA_CONTAINER_IP+":"+Constant.DATA_CONTAINER_PORT+Constant.DATA_CONTAINER_DATARESOURCE+"/"+id+Constant.DATA_CONTAINER_GETMETA;
        URL url=new URL(reqUrl);
        URI uri=new URI(url.getProtocol(), url.getHost()+":"+url.getPort(), url.getPath(), url.getQuery(),null);
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);

        //释放链接
        response.close();
        client.close();

        return body;
    }


    public static String getModelRecordJson(String recordId) throws IOException, URISyntaxException {
        String body = "";
        CloseableHttpClient client =HttpClients.createDefault();
        String reqUrl = "http://"+Constant.SAGA_SERVER_IP+":"+Constant.SAGA_SERVER_PORT+Constant.MODEL_RECORD_JSON_ROUTER+"/"+recordId;
        URL url=new URL(reqUrl);
        URI uri=new URI(url.getProtocol(), url.getHost()+":"+url.getPort(), url.getPath(), url.getQuery(),null);
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);

        //释放链接
        response.close();
        client.close();

        return body;
    }


    public static MyResponse getMyResponse(String urlString) throws IOException {
        URL url=new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);

        return new MyResponse(connection.getContentLength(),
                connection.getContentType(),
                connection.getHeaderField("Content-Disposition"),
                connection.getInputStream()
        );
    }

    /**
     * 将response的InputStream写到指定basicPath路径中去
     *
     * @param basicPath 存储的目录

     * @return 返回Reponse中的文件名
     * @throws IOException
     */
    public static String getMyFile(String basicPath, String urlString) throws IOException {
        URL url=new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);

        //attachment; filename=fileName
        String Content = connection.getHeaderField("Content-Disposition");
        String filename = Content.substring(StringUtils.lastIndexOf(Content, "=") + 1);
        MyFileUtils.writeInputStreamToFile(connection.getInputStream(), new File(basicPath + filename));
        return filename;
    }


    public static String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    /**
     * 创建jwt
     * @param id
     * @param subject
     * @param ttlMillis 过期的时间长度
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        Map<String,Object> claims = new HashMap<String,Object>();//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claims.put("uid", "DSSFAWDWADAS...");
        claims.put("user_name", "admin");
        claims.put("nick_name","DASDA121");
        SecretKey key = generalKey();//生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();           //就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }

    /**
     * 解密jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)         //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();//设置需要解析的jwt
        return claims;
    }
    /**
     * 由字符串生成加密key
     * @return
     */
    public static SecretKey generalKey(){
        String stringKey = "7786df7fc3a34e61361c034d5ec8245d";//本地配置文件中加密的密文
        byte[] encodedKey = Base64.decodeBase64(stringKey);//本地的密码解码
        System.out.println(encodedKey);//[B@152f6e2
        System.out.println(Base64.encodeBase64URLSafeString(encodedKey));
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
        return key;
    }
}
