package com.xiaomi.smarthome.device.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PluginRecommendSceneInfo {
    public static class RecommendSceneItem {
        public String sr_id;
        public String intro;
        public String timeSpan;
        public int st_id;
        public int enable;
        public int enable_push;
        public int ua;
        //推荐的静态图片
        public String url;
        //推荐的动态图片
        public String gifUrl;

        /**
         * 插件入口文案
         */
        public String entryDesc;
        /**
         * 推荐卡片上的文字
         */
        public String cardDesc;
        /**
         * 卡片上的图片需要非本地，非拟物图的情况
         */
        public String cardImgUrl;

        public List<ConditionActionItem> mConditionList = new ArrayList<>();
        public List<ConditionActionItem> mActionList = new ArrayList<>();

        public List<RecommendBuy> mBuyLinks = new ArrayList<>();

        public static RecommendSceneItem parseFrom(String jsonStr) {
            RecommendSceneItem result = new RecommendSceneItem();
            try {
                JSONObject itemObj = new JSONObject(jsonStr);
                result.ua = itemObj.optInt("ua", -1);
                if (!isSupportAndroid(result.ua)) {
                    return null;
                }
                result.sr_id = itemObj.optString("sr_id");
                result.intro = itemObj.optString("intro");


//                result.entryDesc = itemObj.optString("entry_desc");
//                result.cardDesc = itemObj.optString("desc");
//                result.cardImgUrl = itemObj.optString("img_url");


                result.st_id = itemObj.optInt("st_id");
                result.enable = itemObj.optInt("enable");
                result.enable_push = itemObj.optInt("enable_push");
                result.url = itemObj.optString("jpg", "");
                result.gifUrl = itemObj.optString("gif");
                JSONArray conditionArray = itemObj.optJSONArray("launch");
                if (conditionArray != null) {
                    result.mConditionList.addAll(ConditionActionItem.parseList(conditionArray));
                }

                JSONArray actionArray = itemObj.optJSONArray("action");
                if (actionArray != null) {
                    result.mActionList.addAll(ConditionActionItem.parseList(actionArray));
                }
            } catch (JSONException e) {
                return null;
            }
            return result;
        }
    }

    public JSONObject mActionSaIds = new JSONObject();
    public JSONObject mConditionScIds = new JSONObject();
    public List<RecommendSceneItem> mSceneItems = new ArrayList<>();

    public static PluginRecommendSceneInfo parse(JSONObject object) {
        PluginRecommendSceneInfo info = new PluginRecommendSceneInfo();
        JSONArray array = object.optJSONArray("scene_recom");
        List<RecommendSceneItem> sceneItems = new ArrayList<>();
        if (array == null) {
            return info;
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject recomObj = array.optJSONObject(i).optJSONObject("info");
            if (recomObj == null) {
                continue;
            }
            RecommendSceneItem item = new RecommendSceneItem();
            item.entryDesc = array.optJSONObject(i).optString("entry_desc");
            item.cardDesc = array.optJSONObject(i).optString("desc");
            item.cardImgUrl = array.optJSONObject(i).optString("img_url");

            item.ua = recomObj.optInt("ua", -1);
            if (!isSupportAndroid(item.ua)) {
                continue;
            }
            item.sr_id = recomObj.optString("sr_id");
            item.intro = recomObj.optString("intro");
            item.st_id = recomObj.optInt("st_id");
            item.enable = recomObj.optInt("enable");
            item.enable_push = recomObj.optInt("enable_push");
            item.url = recomObj.optString("jpg", "");
            item.gifUrl = recomObj.optString("gif");
            JSONArray conditionArray = recomObj.optJSONArray("launch");
            if (conditionArray != null) {
                item.mConditionList.addAll(ConditionActionItem.parseList(conditionArray));
            }

            JSONArray actionArray = recomObj.optJSONArray("action");
            if (actionArray != null) {
                item.mActionList.addAll(ConditionActionItem.parseList(actionArray));
            }
            sceneItems.add(item);
        }
        info.mSceneItems.addAll(sceneItems);
        info.mConditionScIds = object.optJSONObject("sc_ids");
        info.mActionSaIds = object.optJSONObject("sa_ids");
        return info;

    }

    private static boolean isSupportAndroid(int ua) {
        if (ua == 0 || ua == 1) {
            return true;
        }
        return false;
    }

    public static class ConditionActionItem {
        public JSONObject modelListJobj = new JSONObject();
        public String name;
        public JSONArray mGidJArray = new JSONArray();
        public String mConditionSrc;
        public String mConditionKey;

        public int actionType;

        public static List<ConditionActionItem> parseList(JSONArray array) {
            List<ConditionActionItem> list = new ArrayList<>();
            if (array == null || array.length() == 0) {
                return list;
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                ConditionActionItem item = new ConditionActionItem();
                item.name = object.optString("name", "");
                item.modelListJobj = object.optJSONObject("model_list");
                item.mGidJArray = object.optJSONArray("gid");
                item.actionType = object.optInt("type", 0);
                item.mConditionSrc = object.optString("src", "");
                item.mConditionKey = object.optString("key", "");
                list.add(item);
            }
            return list;
        }
    }

    public static class RecommendBuy {
        public String model;
        public String url;

        public RecommendBuy(String model, String url) {
            this.model = model;
            this.url = url;
        }
    }
}
