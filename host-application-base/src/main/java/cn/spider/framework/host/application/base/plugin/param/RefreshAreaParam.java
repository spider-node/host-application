package cn.spider.framework.host.application.base.plugin.param;

import java.util.List;

public class RefreshAreaParam {
    private List<RefreshAreaModel> areaModelList;

    private String pluginKey;

    public String getPluginKey() {
        return pluginKey;
    }

    public void setPluginKey(String pluginKey) {
        this.pluginKey = pluginKey;
    }

    public List<RefreshAreaModel> getAreaModelList() {
        return areaModelList;
    }

    public void setAreaModelList(List<RefreshAreaModel> areaModelList) {
        this.areaModelList = areaModelList;
    }
}
