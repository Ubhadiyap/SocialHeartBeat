package heartbeat.social.tcs.socialhb.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ark on 10/17/2016.
 */
public class ModuleItem {
    @SerializedName("module_name")
    @Expose
    private String moduleName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("module_icon")
    @Expose
    private String moduleIcon;

    /**
     * @return The moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName The module_name
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The moduleIcon
     */
    public String getModuleIcon() {
        return moduleIcon;
    }

    /**
     * @param moduleIcon The module_icon
     */
    public void setModuleIcon(String moduleIcon) {
        this.moduleIcon = moduleIcon;
    }


}
