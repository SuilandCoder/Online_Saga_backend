package com.liber.sun.controller;

import com.alibaba.fastjson.JSON;
import com.liber.sun.annotation.UserLoginToken;
import com.liber.sun.constant.Constant;
import com.liber.sun.domain.*;
import com.liber.sun.enums.ResultEnum;
import com.liber.sun.service.ToolRecordService;
import com.liber.sun.service.UserService;
import com.liber.sun.utils.MyHttpUtils;
import com.liber.sun.utils.ResultUtil;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.DataInput;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by SongJie on 2019/3/8 22:41
 */
@RestController //默认返回json格式
@RequestMapping("/user")
public class UserControl {
    @Autowired
    private UserService userService;

    @Autowired
    private ToolRecordService toolRecordService;

    @RequestMapping(value = "/sign-in",method=RequestMethod.POST)
    public Result userSignIn(@RequestBody User user){
        User user_in_db = userService.getUserByUserId(user.getUserId());
        if(user_in_db==null){
            return ResultUtil.error(ResultEnum.NOT_EXISTS);
        }else{
            if(!user_in_db.getPassword().equals(user.getPassword())){
                return ResultUtil.error(ResultEnum.PASSWORD_ERROR);
            }else{
                String token = null;
                try {
                    token = MyHttpUtils.createJWT(user_in_db.getId(),user_in_db.toString(),172800000);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultUtil.error(ResultEnum.CREATE_TOKEN_FAILED);
                }
                user_in_db.setPassword("");
                LoginRes loginRes = new LoginRes(user_in_db,token);
                return ResultUtil.success(loginRes);
            }
        }
    }

    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }


    @RequestMapping(value = "/sign-up",method = RequestMethod.POST)
    public Result userSignUp(@RequestBody User user){
        //如果用户名或者密码为空
        if(null==user.getUserId()||null==user.getPassword()){
            return ResultUtil.error(ResultEnum.MISSING_REQUIRED_DATA);
        }
        //todo 判断用户名及密码是否符合规则

        //判断用户是否已经存在
        User user_in_db = userService.getUserByUserId(user.getUserId());
        if(user_in_db!=null){
            return ResultUtil.error(ResultEnum.ALREADY_EXISTS);
        }
        User user_insert = userService.addUser(user);
        if(user_insert!=null){
            String token = null;
            try {
                token = MyHttpUtils.createJWT(user_insert.getId(),user_insert.toString(),172800000);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultUtil.error(ResultEnum.CREATE_TOKEN_FAILED);
            }
            user_insert.setPassword("");
            LoginRes loginRes = new LoginRes(user_insert,token);
            return ResultUtil.success(loginRes);
        }else{
            return ResultUtil.error(ResultEnum.ADD_USER_FAILED);
        }
    }

    //todo  增加通过用户id查询用户toolrecord 列表的接口
    @RequestMapping(value = "/getToolRecord", method = RequestMethod.GET)
    public Result getToolRecord(@RequestParam("userId") String userId){
        List<ToolRecord> toolRecordList = toolRecordService.getToolRecordByUserId(userId);

        return ResultUtil.success(toolRecordList);
    }


    /**
     *
     * @param userId 用户名
     * @param recordId 模型记录id
     * @param status 判断模型是否运行成功，成功后发送模型容器上的输出数据上传数据容器的请求
     * @param output 输出数据参数，主要是获取输出数据类型，在模型刚运行时将输出数据类型存入数据库，用于数据上传数据容器后，发送DataResource请求时指定数据类型
     * @param
     * @return
     * @throws IOException
     * @throws JSONException
     * @throws URISyntaxException
     */
    @RequestMapping(value="/addToolRecord",method = RequestMethod.GET)
    public Result addToolRecord(@RequestParam("userId") String userId,
                                @RequestParam("recordId") String recordId,
                                @RequestParam("status") int status,
                                @RequestParam("outputParams") String output,
                                @RequestParam("inputParams") String input) throws IOException, JSONException, URISyntaxException {

        List<ToolDataParam> outputParams = null;
        if(output!=null&&!TextUtils.isEmpty(output)){
            outputParams = JSON.parseArray(output,ToolDataParam.class);
        }

        List<ToolDataParam> inputParams = null;
        if(input!=null&&!TextUtils.isEmpty(input)){
            inputParams = JSON.parseArray(input,ToolDataParam.class);
        }

        // 先从数据库取记录
        ToolRecord toolRecord_db = toolRecordService.getToolRecordByRecordId(recordId);

        ToolRecord tr = new ToolRecord();
        String toolRecord = MyHttpUtils.getModelRecordJson(recordId);

        org.json.JSONObject recordJson = new org.json.JSONObject(toolRecord);
        int code = recordJson.getInt("code");

        JSONObject dataJson = recordJson.getJSONObject("data");
        String excuteTime = dataJson.getString("msr_datetime");
        String timeSpan = dataJson.getString("msr_span");
        JSONObject msr_ms = dataJson.getJSONObject("msr_ms");
        String toolDescription = msr_ms.getString("ms_des");
        JSONObject ms_model = msr_ms.getJSONObject("ms_model");
        String toolName = ms_model.getString("m_name");

        JSONArray msr_output = dataJson.getJSONArray("msr_output");
        List<ToolData> outputList = getRecordInOutputList(msr_output,outputParams);
        if(toolRecord_db!=null){
            outputList = mergeToolDataList(toolRecord_db.getOutputList(),outputList);
        }
        JSONArray msr_input = dataJson.getJSONArray("msr_input");
        List<ToolData> inputList = getRecordInOutputList(msr_input,inputParams);
        if(toolRecord_db!=null){
            inputList = toolRecord_db.getInputList();
            //融合两个集合
        }
        tr.setUserId(userId);
        tr.setRecordId(recordId);
        tr.setExcuteTime(excuteTime);
        tr.setTimeSpan(timeSpan);
        tr.setExcuteState(code);
        tr.setToolDescription(toolDescription);
        tr.setToolName(toolName);
        tr.setInputList(inputList);
        tr.setOutputList(outputList);
        //判断模型是否运行成功：
        boolean runResult = getRunResult(tr);
        //判断模型是否运行成功，将输出数据上传到数据容器(向模型容器发送请求，通过模型容器上传)
        if(runResult){
            String data = MyHttpUtils.postOutputFiletoDC(recordId);
            if(data!=""){
                //更新outputlist
//                JSONArray dataJsonArray = JSON.parseArray(data);
                JSONArray dataJsonArray = new org.json.JSONArray(data);
                List<ToolData> outputUpdatedList = updataRecordOutputList(userId,outputList, dataJsonArray);
                tr.setOutputList(outputUpdatedList);
            }
        }

        if(toolRecord_db == null){
            // 添加新数据
            toolRecordService.addToolRecord(tr);
        }else{
            // 更新已有数据
            toolRecordService.updateToolRecord(tr);
        }
        return ResultUtil.success(tr);
    }

    public List<ToolData> mergeToolDataList(List<ToolData> dbList,List<ToolData> newList){
        for(ToolData dbData:dbList){
            for(ToolData newData:newList){
                if(newData.getDataName().equals(dbData.getDataName())){
                    newData.setType(dbData.getType());
                }
            }
        }
        return newList;
    }

    // 获取模型记录中输入输出列表
    public List<ToolData> getRecordInOutputList(JSONArray jsonArray,List<ToolDataParam> dataParams) throws JSONException {
        List<ToolData> dataList = new ArrayList<>();
        for(int i = 0;i<jsonArray.length();i++){
            ToolData td = new ToolData();
            JSONObject json = jsonArray.getJSONObject(i);
            String dataId = json.getString("DataId");
            String dataName = json.getString("Event");
            String stateName = json.getString("StateName");
            String tag = json.getString("Tag");
            //第一次调用时dataParams不为空,后面调用时为空
            if(dataParams!=null && dataParams.size()>0){
                for(ToolDataParam tdp:dataParams){
                    if(tdp.identifier.equals(dataName)){
                        td.setType(tdp.getType());
                    }
                }
            }
            td.setDataId(dataId);
            td.setDataName(dataName);
            td.setStateName(stateName);
            td.setTag(tag);
            dataList.add(td);
        }
        return dataList;
    }

    //更新输出列表
    public List<ToolData> updataRecordOutputList(String userId,List<ToolData> oldList,JSONArray outputJsonArray) throws JSONException, IOException, URISyntaxException {
        System.out.println("更新前的数据列表："+oldList.toString());
        for(int i = 0; i<outputJsonArray.length();i++){
            JSONObject json = outputJsonArray.getJSONObject(i);
            String dataName = json.getString("Event");
            String stateName = json.getString("StateName");
            String tag = json.getString("Tag");
            String url = json.getString("Url");
            //获取url中的sourceStoreId,将数据上传至DataResource
            String sourceStoreId ="";
            if(url.contains("&sourceStoreId=")){
                sourceStoreId = url.substring(url.indexOf("&sourceStoreId=")+15,url.lastIndexOf("&"));
            }
            for(ToolData td : oldList){
                if(td.getDataId()!=null&&!td.getDataId().isEmpty()&&td.getDataName().equals(dataName)){
                    td.setDownloadUrl(url);
                    //将输出数据发送到dataResource
                    String data = MyHttpUtils.postToDataResource(userId, td.getDataId(), sourceStoreId, "zip", getDataType(td.getType()));
                    if(data!=""){
                        org.json.JSONObject dataJson = new org.json.JSONObject(data);
                        String dataResourceId = dataJson.getString("id");
                        td.setDataResourceId(dataResourceId);
                        //如果输出数据为shp 获取meta
                        if("SHAPEFILE".equals(getDataType(td.getType()))){
                            MyHttpUtils.getShpMeta(dataResourceId);
                        }
                    }
                    break;
                }
            }
        }
        System.out.println("更新后的数据列表："+oldList.toString());
        return oldList;
    }

    public boolean getRunResult(ToolRecord tr){
        boolean runResult = false;
        if(tr.getExcuteState()==1){
            List<ToolData> outputList = tr.getOutputList();
            for(int i =0;i<outputList.size();i++){
                ToolData toolData = outputList.get(i);
                if(toolData.getDataId()!=null && !toolData.getDataId().isEmpty()){
                    runResult = true;
                }
            }
        }
        return runResult;
    }


    public String getDataType(String dataType){
        String type = "";
        if(dataType == null){
            return "OTHER";
        }
        if(dataType.contains("Shapes list")){
            type="OTHER";
        }else if(dataType.contains("Shapes")){
            type="SHAPEFILE";
        }else if(dataType.contains("Grid list")){
            type="OTHER";
        }else if(dataType.contains("Grid")){
            type="GEOTIFF";
        }else{
            type = "OTHER";
        }
        return type;
    }
}
